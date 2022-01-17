package com.tuwaiq.newsplanet.fragments

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.webkit.WebViewClient
import android.window.SplashScreen
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.tuwaiq.newsplanet.R
import com.tuwaiq.newsplanet.ui.NewsActivity
import com.tuwaiq.newsplanet.ui.NewsViewModel
import com.tuwaiq.newsplanet.ui.SplashScreenActivity
import kotlinx.android.synthetic.main.fragment_article.*
import kotlinx.android.synthetic.main.fragment_article.fab
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment(R.layout.fragment_details) {
    lateinit var viewModel: NewsViewModel
    val args: ArticleFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // to access the activity's ViewModel
        viewModel = (activity as NewsActivity).viewModel
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Article"
        val article = args.article

        setHasOptionsMenu(true)

        ivArticleImage.load(article.urlToImage)
        tvTitle.text = article.title
        tvDescription.text = article.description
        tvSource.text = article.source.name
        tvPublishedAt.text = article.publishedAt.removeRange(9 , 18).replace('Z',' ')
        tvContent.text = article.content.toString()


        fab.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(view, "Article saved successfully", Snackbar.LENGTH_SHORT).show()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)  {
        inflater.inflate(R.menu.main_menu , menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val article = args.article
        if(item?.itemId == R.id.share_menu) {
            val shareIntent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT, article.url)
                this.type = "text/plain"
            }
            startActivity(shareIntent)
        }else {
            return super.onOptionsItemSelected(item)
        }
        return true
    }
}