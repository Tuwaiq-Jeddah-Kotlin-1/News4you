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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tuwaiq.newsplanet.R
import com.tuwaiq.newsplanet.models.User
import com.tuwaiq.newsplanet.ui.NewsActivity
import com.tuwaiq.newsplanet.ui.NewsViewModel
import kotlinx.android.synthetic.main.sign_up_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignUpFragment : Fragment(R.layout.sign_up_fragment) {

    private val userCollectionRef = Firebase.firestore.collection("users")


    lateinit var usernameET: TextInputEditText
    lateinit var emailET: TextInputEditText
    lateinit var passwordET: TextInputEditText
    lateinit var phoneNumberET: TextInputEditText
    lateinit var signInTV: TextView

    lateinit var signUpButton: Button

    lateinit var viewModel: NewsViewModel



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

        // to access the activity's ViewModel
        viewModel = (activity as NewsActivity).viewModel

        signUpButton.setOnClickListener {
            when {
                TextUtils.isEmpty(emailET.text.toString().trim { it <= ' ' }) -> {
                    emailTextInputSignIn.helperText = "* Required"
                }
                TextUtils.isEmpty(passwordET.text.toString().trim { it <= ' ' }) -> {
                    passwordTextInputSignIn.helperText = "* Required"
                }
                else -> {
                    val userName: String = usernameET.text.toString().trim { it <= ' ' }
                    val email: String = emailET.text.toString().trim { it <= ' ' }
                    val password: String = passwordET.text.toString().trim { it <= ' ' }
                    val phoneNumber: String = phoneNumberET.text.toString().trim { it <= ' ' }


                    // create an instance and create a register with email and password
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->

                            // if the registration is sucessfully done
                            if (task.isSuccessful) {
                                //firebase register user
                                val firebaseUser: FirebaseUser = task.result!!.user!!
                                val user = User(userName, email, phoneNumber)
                                saveUser(user)
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

        signInTV.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }
        return view
    }

    fun saveUser(user: User) = CoroutineScope(Dispatchers.IO).launch {
        val userUid = FirebaseAuth.getInstance().currentUser!!.uid
        try {
            userCollectionRef.document("$userUid").set(user).await()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Successfully saved data", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }



//    private fun addUser(user : User){
//        userCollectionRef.add(user).addOnCompleteListener { task ->
//            if(task.isSuccessful){
//                Toast.makeText(context, "Successfully saved data", Toast.LENGTH_LONG).show()
//            } else{
//                Toast.makeText(context, "error", Toast.LENGTH_LONG).show()
//            }
//        }.addOnFailureListener {
//            println(it.message)
//        }
//    }
}