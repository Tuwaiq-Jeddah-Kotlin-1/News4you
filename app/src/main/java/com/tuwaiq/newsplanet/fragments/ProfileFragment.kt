package com.tuwaiq.newsplanet.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.tuwaiq.newsplanet.R
import com.tuwaiq.newsplanet.ui.NewsActivity
import com.tuwaiq.newsplanet.ui.NewsViewModel

class ProfileFragment : Fragment(R.layout.profile_fragment) {

    lateinit var signOutButton : Button
    private lateinit var pref : SharedPreferences
    lateinit var viewModel: NewsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signOutButton = view.findViewById(R.id.btn_logout)


        signOutButton.setOnClickListener {
            pref =
                this.requireActivity().getSharedPreferences("preference", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = pref.edit()
            editor.clear()
            editor.apply()
            // Logout from app
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.action_profileFragment_to_signInFragment)
        }


        // to access the activity's ViewModel
        viewModel = (activity as NewsActivity).viewModel
    }
}