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

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.signInFragment -> {
                    bottomNavView.visibility = View.GONE
                }
                R.id.signUpFragment -> {
                    bottomNavView.visibility = View.GONE
                }
                else -> {
                    bottomNavView.visibility = View.VISIBLE
                }
            }
        }

        val newsRepository = NewsRepo(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application , newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)
        bottomNavigationView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu , menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.share_menu) {
            val shareIntent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT, "News Planet is the best app to track top news ..")
                this.type = "text/plain"
            }
            startActivity(shareIntent)
        }else{
            return super.onOptionsItemSelected(item)

        }
        return true
    }
}