package com.tuwaiq.newsplanet.fragments

import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tuwaiq.newsplanet.R
import com.tuwaiq.newsplanet.ui.bottomNavView
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.fragment_search_news.*

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_splash, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        Handler().postDelayed({
            findNavController().navigate(R.id.action_splashFragment_to_signInFragment)
        }, 5000)

    }
}