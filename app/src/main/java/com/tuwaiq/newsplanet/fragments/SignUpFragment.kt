package com.tuwaiq.newsplanet.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tuwaiq.newsplanet.R
import com.tuwaiq.newsplanet.models.User
import com.tuwaiq.newsplanet.ui.NewsActivity
import com.tuwaiq.newsplanet.ui.NewsViewModel
import com.tuwaiq.newsplanet.ui.SplashScreenActivity
import com.tuwaiq.newsplanet.ui.bottomNavView
import com.tuwaiq.newsplanet.validateEmail
import com.tuwaiq.newsplanet.validatePass
import kotlinx.android.synthetic.main.sign_up_fragment.*

class SignUpFragment : Fragment(R.layout.sign_up_fragment) {

    private val userCollectionRef = Firebase.firestore.collection("users")


    lateinit var usernameET: TextInputEditText
    lateinit var emailET: TextInputEditText
    lateinit var passwordET: TextInputEditText
    lateinit var phoneNumberET: TextInputEditText
    lateinit var signInTV: TextView
    lateinit var signUpButton: Button
    lateinit var viewModel: NewsViewModel
    lateinit var profileSharedPreferance : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.sign_up_fragment, container, false)

        usernameET = view.findViewById(R.id.usernameET)
        emailET = view.findViewById(R.id.emailET)
        passwordET = view.findViewById(R.id.passwordET)
        phoneNumberET = view.findViewById(R.id.phoneNumberET)
        signUpButton = view.findViewById(R.id.signUpBtn)
        signInTV = view.findViewById(R.id.signInTV)

        profileSharedPreferance = this.requireActivity().getSharedPreferences("userSettings", Context.MODE_PRIVATE)

        bottomNavView.visibility = View.INVISIBLE

        // to access the activity's ViewModel
        viewModel = (activity as NewsActivity).viewModel


        signUpButton.setOnClickListener {
            val emailTrim = emailET.text.toString().trim { it <= ' ' }
            val passTrim = passwordET.text.toString().trim { it <= ' ' }
            when {
                validateEmail(emailTrim) -> {
                    emailTextInputSignUp.helperText = "* Required"
                }
                validatePass(passTrim) -> {
                    emailTextInputSignUp.helperText = "* Required"
                }
                else -> {
                    val userName: String = usernameET.text.toString().trim { it <= ' ' }
                    val email: String = emailET.text.toString().trim { it <= ' ' }
                    val password: String = passwordET.text.toString().trim { it <= ' ' }
                    val phoneNumber: String = phoneNumberET.text.toString().trim { it <= ' ' }


                    // create an instance and create a register with email and password
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->

                            // if the registration is successfully done
                            if (task.isSuccessful) {
                                val user = User(userName, email, phoneNumber)
                                viewModel.saveUser(user)
                                val editor: SharedPreferences.Editor = profileSharedPreferance.edit()
                                editor.putString("USERNAME" , user!!.username)
                                editor.putString("EMAIL" , user.email)
                                editor.putString("PHONENUMBER" , user.phoneNumber)
                                editor.apply()
                                findNavController().navigate(R.id.action_signUpFragment_to_mainFragment)
                            } else {
                                // if the registration is not successful then show error massage
                                Toast.makeText(
                                    context,
                                    task.exception?.localizedMessage,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                }
            }
        }

        signInTV.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }
        return view
    }
}