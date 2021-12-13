package com.tuwaiq.newsplanet.fragments

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

class SignUpFragment : Fragment() {

    //private val userCollectionRef = Firebase.firestore.collection("users")


    lateinit var usernameET: TextInputEditText
    lateinit var emailET: TextInputEditText
    lateinit var passwordET: TextInputEditText
    lateinit var phoneNumberET: TextInputEditText
    lateinit var signInTV: TextView

    lateinit var signUpButton: Button

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

        signUpButton.setOnClickListener {
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
                    val userID: String = FirebaseAuth.getInstance().currentUser?.uid.toString()
                    val userName: String = usernameET.text.toString().trim { it <= ' ' }
                    val email: String = emailET.text.toString().trim { it <= ' ' }
                    val password: String = passwordET.text.toString().trim { it <= ' ' }
                    val birthday: String = phoneNumberET.text.toString().trim { it <= ' ' }


                    // create an instance and create a register with email and password
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->

                            // if the registration is sucessfully done
                            if (task.isSuccessful) {
                                //firebase register user
                                val firebaseUser: FirebaseUser = task.result!!.user!!
                                //val user = User(userID, userName, email, birthday)
                                //saveUser(user)

                                Toast.makeText(
                                    context,
                                    "You were registered successfully",
                                    Toast.LENGTH_LONG
                                ).show()

                                findNavController().navigate(R.id.action_signUpFragment_to_topHeadlineFragment)
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

        phoneNumberET.setOnClickListener {

        }

        signInTV.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }
        return view
    }



//    fun saveUser(user: User) = CoroutineScope(Dispatchers.IO).launch {
//        val userUid = FirebaseAuth.getInstance().currentUser!!.uid
//        try {
//            userCollectionRef.document("$userUid").set(user).await()
//            withContext(Dispatchers.Main) {
//                Toast.makeText(context, "Successfully saved data", Toast.LENGTH_LONG).show()
//            }
//        } catch (e: Exception) {
//            withContext(Dispatchers.Main) {
//                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
//            }
//        }
//    }
}