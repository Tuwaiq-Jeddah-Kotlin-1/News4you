package com.tuwaiq.newsplanet.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tuwaiq.newsplanet.R
import com.tuwaiq.newsplanet.ui.NewsActivity
import com.tuwaiq.newsplanet.ui.NewsViewModel
import kotlinx.android.synthetic.*

class ForgotPassFragment : Fragment(R.layout.forget_pass_fragment) {
    lateinit var viewModel: NewsViewModel
    lateinit var backBtn : Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backBtn = view.findViewById(R.id.backToLoginBtn)

        backBtn.setOnClickListener{
            findNavController().navigate(R.id.action_forgotPassFragment_to_signInFragment)
        }

        // to access the activity's ViewModel
        viewModel = (activity as NewsActivity).viewModel
    }
}