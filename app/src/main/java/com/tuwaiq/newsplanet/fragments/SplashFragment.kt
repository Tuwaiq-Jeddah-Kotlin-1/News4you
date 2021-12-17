package com.tuwaiq.newsplanet.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tuwaiq.newsplanet.R
import com.tuwaiq.newsplanet.ui.NewsActivity
import com.tuwaiq.newsplanet.ui.NewsViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SplashFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_splash, container, false)



        Handler(Looper.myLooper()!!).postDelayed({
            findNavController().navigate(R.id.action_splashFragment_to_signInFragment)
        }, 5000)

        return view
    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        launch {
//            delay(3000)
//            withContext(Dispatchers.Main){
//                (activity as NewsActivity).viewModel
//            }
//        }
//    }
}