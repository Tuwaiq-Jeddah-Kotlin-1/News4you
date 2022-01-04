package com.tuwaiq.newsplanet.ui

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tuwaiq.newsplanet.R
import com.tuwaiq.newsplanet.db.ArticleDatabase
import com.tuwaiq.newsplanet.repo.NewsRepo
import com.tuwaiq.newsplanet.workmanager.NewsNotificationRepo
import kotlinx.android.synthetic.main.activity_news.*
import java.util.*


public lateinit var bottomNavView: BottomNavigationView

class NewsActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        setContentView(R.layout.activity_news)

        val sharedPreferences = getSharedPreferences("settings", Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString("LANGUAGE", "")!!
        val darkSharedPreferance = this.getSharedPreferences("darkMode", Context.MODE_PRIVATE)
        val isDarkMode = darkSharedPreferance.getBoolean("DARKMODE" , false)


        if(isDarkMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        setLocales(language)

        getSupportActionBar()?.show()
        getSupportActionBar()?.elevation = 0F
        supportActionBar?.setBackgroundDrawable(getDrawable(R.drawable.actionbar_bg))

        // implementing Worm Manager ..
        NewsNotificationRepo().myNotification(this)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        bottomNavView = findViewById(R.id.bottomNavigationView)
        bottomNavView.setupWithNavController(navController)
        bottomNavView.visibility = View.INVISIBLE

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.forgotPassFragment -> {
                    bottomNavigationView.visibility = View.GONE
                }
                R.id.signInFragment -> {
                    bottomNavigationView.visibility = View.GONE
                }
                R.id.signUpFragment -> {
                    bottomNavigationView.visibility = View.GONE
                }
                else -> {
                    bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }


        val newsRepository = NewsRepo(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application , newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)
    }

    private fun setLocales(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        this.resources?.updateConfiguration(config, this.resources?.displayMetrics)
        val editor = this.getSharedPreferences("settings", Context.MODE_PRIVATE).edit()
        editor.putString("LANGUAGE", language)
        editor.apply()
    }
}