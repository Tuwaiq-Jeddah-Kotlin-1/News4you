package com.tuwaiq.newsplanet.fragments


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.tuwaiq.newsplanet.R
import com.tuwaiq.newsplanet.ui.bottomNavView
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.android.synthetic.main.fragment_search_news.paginationProgressBar
import kotlinx.android.synthetic.main.fragment_top_headlines_news.*
import kotlinx.android.synthetic.main.sign_in_fragment.*
import kotlinx.android.synthetic.main.sign_in_fragment.emailTextInputSignIn
import kotlinx.android.synthetic.main.sign_up_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SignInFragment : Fragment(R.layout.sign_in_fragment) {

    lateinit var emailET: TextInputEditText
    lateinit var passwordET: TextInputEditText
    lateinit var signInButton: Button
    lateinit var signUpTv: TextView
    lateinit var forgetPassTV: TextView
    lateinit var emailTextInputLayout : TextInputLayout
    lateinit var passwordTextInputLayout : TextInputLayout
    lateinit var userSharedPreferance : SharedPreferences


    private lateinit var sharedPreferences: SharedPreferences

    var isRemembered = false
    private lateinit var rememberMe: CheckBox


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.sign_in_fragment, container, false)
        userSharedPreferance = this.requireActivity().getSharedPreferences("user" , Context.MODE_PRIVATE)


        emailET = view.findViewById(R.id.emailET)
        passwordET = view.findViewById(R.id.passwordET)
        signInButton = view.findViewById(R.id.signInBtn)
        signUpTv = view.findViewById(R.id.signupTV)
        forgetPassTV = view.findViewById(R.id.forgetPassTV)
        emailTextInputLayout = view.findViewById(R.id.emailTextInputSignIn)
        passwordTextInputLayout = view.findViewById(R.id.passwordTextInputSignIn)




        rememberMe = view.findViewById(R.id.cbRemember)
        sharedPreferences =
            this.requireActivity().getSharedPreferences("preference", Context.MODE_PRIVATE)
        isRemembered = sharedPreferences.getBoolean("CHECKBOX", false)

        bottomNavView.visibility = View.INVISIBLE


        if (isRemembered) {
            findNavController().navigate(R.id.action_signInFragment_to_mainFragment)
        }

        signInButton.setOnClickListener {
            val email = emailET.editableText.toString()
            val password = passwordET.editableText.toString()
            Toast.makeText(context, "Data Stored", Toast.LENGTH_SHORT).show()
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

                                val emailPreference: String = email
                                val passwordPreference: String = password

                                val checked: Boolean = rememberMe.isChecked

                                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                                editor.putString("EMAIL", emailPreference)
                                editor.putString("PASSWORD", passwordPreference)
                                editor.putBoolean("CHECKBOX", checked)
                                editor.apply()

                                retrieveUserData()

                                Toast.makeText(
                                    context,
                                    "You lodged in successfully",
                                    Toast.LENGTH_LONG
                                ).show()

                                findNavController().navigate(R.id.action_signInFragment_to_mainFragment)
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

    private fun retrieveUserData() = CoroutineScope(Dispatchers.IO).launch{
        val uId =FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        db.collection("Users").document("$uId")
            .get().addOnCompleteListener {
                if (it.result?.exists()!!) {
                    val name = it.result!!.getString("username")
                    val email = it.result!!.getString("email")
                    val phoneNumber = it.result!!.getString("phoneNumber")
                    userSharedPreferance = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
                    val editor:SharedPreferences.Editor = userSharedPreferance.edit()
                    editor.putString("refUsername",name.toString())
                    editor.putString("refEmail",email.toString())
                    editor.putString("refPhone",phoneNumber.toString())
                    editor.apply()
                }else {
                    Log.e("error \n", "Nope")
                }
            }
    }
}