package com.tuwaiq.newsplanet.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tuwaiq.newsplanet.R
import com.tuwaiq.newsplanet.db.ArticleDatabase
import com.tuwaiq.newsplanet.repo.NewsRepo
import kotlinx.android.synthetic.main.activity_news.*


public lateinit var bottomNavView: BottomNavigationView

class NewsActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        getSupportActionBar()?.elevation = 0F

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        bottomNavView = findViewById(R.id.bottomNavigationView)
        bottomNavView.setupWithNavController(navController)
        bottomNavView.visibility = View.INVISIBLE

        val newsRepository = NewsRepo(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application , newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)
    }
}