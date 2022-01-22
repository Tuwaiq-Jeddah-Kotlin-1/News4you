package com.tuwaiq.newsplanet.fragments


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.tuwaiq.newsplanet.R
import com.tuwaiq.newsplanet.models.User
import com.tuwaiq.newsplanet.ui.bottomNavView
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.android.synthetic.main.fragment_top_headlines_news.*
import kotlinx.android.synthetic.main.profile_fragment.*
import kotlinx.android.synthetic.main.sign_in_fragment.*
import kotlinx.android.synthetic.main.sign_up_fragment.*


class SignInFragment : Fragment(R.layout.sign_in_fragment) {

    lateinit var emailET: TextInputEditText
    lateinit var passwordET: TextInputEditText
    lateinit var signInButton: Button
    lateinit var signUpTv: TextView
    lateinit var forgetPassTV: TextView
    lateinit var emailTextInputLayout : TextInputLayout
    lateinit var passwordTextInputLayout : TextInputLayout
    //lateinit var userSharedPreferance : SharedPreferences


    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var profileSharedPreferance: SharedPreferences

    var isRemembered = false
    var appLanguage = "en"
    private lateinit var rememberMe: CheckBox


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.sign_in_fragment, container, false)
        //userSharedPreferance = this.requireActivity().getSharedPreferences("user" , Context.MODE_PRIVATE)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = getString(R.string.sign_in)

        emailET = view.findViewById(R.id.emailET)
        passwordET = view.findViewById(R.id.passwordET)
        signInButton = view.findViewById(R.id.signInBtn)
        signUpTv = view.findViewById(R.id.signupTV)
        forgetPassTV = view.findViewById(R.id.forgetPassTV)
        emailTextInputLayout = view.findViewById(R.id.emailTextInputSignUp)
        passwordTextInputLayout = view.findViewById(R.id.passwordTextInputSignUp)
        val userCollectionRef = Firebase.firestore.collection("users")
        val db = FirebaseFirestore.getInstance()




        rememberMe = view.findViewById(R.id.cbRemember)
        sharedPreferences = this.requireActivity().getSharedPreferences("preference", Context.MODE_PRIVATE)
        profileSharedPreferance = this.requireActivity().getSharedPreferences("userSettings", Context.MODE_PRIVATE)
        isRemembered = sharedPreferences.getBoolean("CHECKBOX", false)


        bottomNavView.visibility = View.INVISIBLE


        if (isRemembered) {
            findNavController().navigate(R.id.action_signInFragment_to_mainFragment)
        }

        signInButton.setOnClickListener {
            when {
                TextUtils.isEmpty(emailET.text.toString().trim { it <= ' ' }) -> {
                    emailTextInputLayout.helperText = "* Required"
                }
                TextUtils.isEmpty(passwordET.text.toString().trim { it <= ' ' }) -> {
                    passwordTextInputLayout.helperText = "* Required"
                }
                else -> {
                    val email: String = emailET.text.toString().trim { it <= ' ' }
                    val password: String = passwordET.text.toString().trim { it <= ' ' }
                    // create an instance and create a register with email and password
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            // if the logIn is successfully done
                            if (task.isSuccessful) {

                                val checked: Boolean = rememberMe.isChecked

                                val userUID = FirebaseAuth.getInstance().currentUser?.uid
                                val docRef = db.collection("users").document("$userUID")


                                docRef.get().addOnSuccessListener { documentSnapshot ->
                                    val user = documentSnapshot.toObject<User>()
                                    val editor: SharedPreferences.Editor = profileSharedPreferance.edit()
                                    editor.putString("USERNAME" , user!!.username)
                                    editor.putString("EMAIL" , user.email)
                                    editor.putString("PHONENUMBER" , user.phoneNumber)
                                    editor.apply()
                                }

                                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                                editor.putBoolean("CHECKBOX", checked)
                                editor.apply()

                                findNavController().navigate(R.id.action_signInFragment_to_mainFragment)
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
        signUpTv.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        forgetPassTV.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_forgotPassFragment)
        }

        return view
    }
}