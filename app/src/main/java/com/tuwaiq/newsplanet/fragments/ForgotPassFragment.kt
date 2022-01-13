package com.tuwaiq.newsplanet.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.tuwaiq.newsplanet.R
import com.tuwaiq.newsplanet.ui.NewsActivity
import com.tuwaiq.newsplanet.ui.NewsViewModel
import com.tuwaiq.newsplanet.ui.SplashScreenActivity
import com.tuwaiq.newsplanet.ui.bottomNavView

class ForgotPassFragment : Fragment(R.layout.forget_pass_fragment) {
    lateinit var viewModel: NewsViewModel
    lateinit var backBtn : Button
    lateinit var reseteBtn : Button
    lateinit var forgotPassET : EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backBtn = view.findViewById(R.id.backToLoginBtn)
        reseteBtn = view.findViewById(R.id.resetePassBtn)
        forgotPassET = view.findViewById(R.id.forgotEmailET)


        bottomNavView.visibility = View.INVISIBLE

        backBtn.setOnClickListener{
            findNavController().navigate(R.id.action_forgotPassFragment_to_signInFragment)
        }

        reseteBtn.setOnClickListener {
            val email = forgotPassET.text.toString().trim { it <= ' ' }
            if (email.isEmpty()) {
                Toast.makeText(
                    context,
                    "You need to fell the email field",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                context,
                                "Email sent successfully to reset your password ",
                                Toast.LENGTH_LONG
                            ).show()
                            findNavController().navigate(R.id.action_forgotPassFragment_to_signInFragment)
                        } else {
                            Toast.makeText(
                                context,
                                task.exception!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        }

        // to access the activity's ViewModel
        viewModel = (activity as NewsActivity).viewModel
    }
}