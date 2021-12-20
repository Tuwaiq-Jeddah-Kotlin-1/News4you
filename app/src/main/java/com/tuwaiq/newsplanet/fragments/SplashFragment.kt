package com.tuwaiq.newsplanet.fragments

import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.window.SplashScreen
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.findNavController
import com.tuwaiq.newsplanet.R
import com.tuwaiq.newsplanet.ui.bottomNavView
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.android.synthetic.main.fragment_splash.*
import kotlinx.coroutines.delay

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_splash, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        compose_sp_main.setContent {
            SplashScreen()
        }
    }

    @Composable
    fun SplashScreen() {
        var startAnim by remember {
            mutableStateOf(false)

        }
        val offSetState by animateDpAsState(
            targetValue = if (startAnim) 0.dp else 100.dp,
            animationSpec = tween(
                durationMillis = 1000
            )
        )
        LaunchedEffect(key1 = true ){
            startAnim = true
            delay(3000L)
            findNavController().navigate(R.id.action_splashFragment_to_signInFragment)
        }
        Box(modifier = Modifier.fillMaxSize() , contentAlignment = Alignment.Center) {
            //Card(modifier = Modifier.size(width = 250.dp , height = 200.dp).offset(y=offSetState), elevation = 10.dp) {
                Image(modifier = Modifier.size(150.dp).offset(y=offSetState),painter = painterResource(id = R.drawable.newsplanetlogo2), contentDescription = "App Logo")
            //}
        }
    }
    @Preview
    @Composable
    fun PreviewSplash() {
        SplashScreen()
    }
}



