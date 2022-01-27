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
    lateinit var langSetting: LangSetting
    override fun onCreate(savedInstanceState: Bundle?) {
        val profileSharedPreferance = this.getSharedPreferences("userSettings", Context.MODE_PRIVATE)
        val language = profileSharedPreferance.getString("LANGUAGE", "")!!
        val isDarkMode = profileSharedPreferance.getBoolean("DARKMODE" , false)
        if(isDarkMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        langSetting = LangSetting(this)
        langSetting.setLocals(language)

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