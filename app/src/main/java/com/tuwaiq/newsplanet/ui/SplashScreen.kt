package com.tuwaiq.newsplanet.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.tuwaiq.newsplanet.R
import com.tuwaiq.newsplanet.db.ArticleDatabase
import com.tuwaiq.newsplanet.repo.NewsRepo
import com.tuwaiq.newsplanet.util.LangSetting

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {


        getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)

        setContentView(R.layout.splashscreen)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, NewsActivity::class.java)
            startActivity(intent)
            finish()
        }, 4500)
    }
}