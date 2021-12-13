package com.tuwaiq.newsplanet.fragments


import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
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
import com.tuwaiq.newsplanet.R
import kotlinx.android.synthetic.main.sign_in_fragment.*


class SignInFragment : Fragment(R.layout.sign_in_fragment) {


    lateinit var emailET: TextInputEditText
    lateinit var passwordET: TextInputEditText
    lateinit var signInButton: Button
    lateinit var signUpTv: TextView
    lateinit var forgetPassTV : TextView

    lateinit var sharedPreferance : SharedPreferences
    var isRemember = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.sign_in_fragment, container, false)


        emailET = view.findViewById(R.id.emailET)
        passwordET = view.findViewById(R.id.passwordET)
        signInButton = view.findViewById(R.id.signInBtn)
        signUpTv = view.findViewById(R.id.signupTV)
        forgetPassTV = view.findViewById(R.id.forgetPassTV)
        signInButton.setOnClickListener {
            val email = emailET.editableText.toString()
            val password = passwordET.editableText.toString()
            Toast.makeText(context, "Data Stored", Toast.LENGTH_SHORT).show()
            when {
                TextUtils.isEmpty(emailET.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        context,
                        "Please Enter Email",
                        Toast.LENGTH_LONG
                    ).show()
                }
                TextUtils.isEmpty(passwordET.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        context,
                        "Please Enter Password",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> {
                    val email: String = emailET.text.toString().trim { it <= ' ' }
                    val password: String = passwordET.text.toString().trim { it <= ' ' }
                    // create an instance and create a register with email and password
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            // if the registration is sucessfully done
                            if (task.isSuccessful) {
                                //firebase register user
                                val firebaseUser: FirebaseUser = task.result!!.user!!
                                Toast.makeText(
                                    context,
                                    "You loged in successfully",
                                    Toast.LENGTH_LONG
                                ).show()
                                findNavController().navigate(R.id.action_signInFragment_to_breakingNewsFragment)
                            } else {
                                // if the registreation is not succsesful then show error massage
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
        signUpTv.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        forgetPassTV.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_forgotPassFragment)
        }

        return view
    }
}