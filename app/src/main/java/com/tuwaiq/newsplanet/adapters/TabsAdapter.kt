package com.tuwaiq.newsplanet.adapters

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tuwaiq.newsplanet.fragments.CategoryFragments.BooksFragment
import com.tuwaiq.newsplanet.fragments.CategoryFragments.MoviesFragment
import com.tuwaiq.newsplanet.fragments.CategoryFragments.MusicFragment
import com.tuwaiq.newsplanet.fragments.SavedNewsfragment
import com.tuwaiq.newsplanet.fragments.TopHeadlineFragment
import com.tuwaiq.newsplanet.ui.NewsViewModel

class TabsPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle, private var numberOfTabs: Int ,val newsViewModel: NewsViewModel ) : FragmentStateAdapter(fm, lifecycle) {


    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                // general
                val topHeadlineFragment = TopHeadlineFragment()
                newsViewModel.newsCategory = "general"
                Log.e("category",newsViewModel.newsCategory)
                return topHeadlineFragment
            }
            1 -> {
                // # technology
                newsViewModel.newsCategory = "technology"
                Log.e("category",newsViewModel.newsCategory)
                Log.e("TAG", "moviesFragment" )
                return TopHeadlineFragment()
            }
            2 -> {
                // # sports
                newsViewModel.newsCategory = "sports"
                Log.e("category",newsViewModel.newsCategory)
                Log.e("TAG", "booksFragment:" )
                return TopHeadlineFragment()
            }
            3 -> {
                //science
                newsViewModel.newsCategory = "science"
                Log.e("category",newsViewModel.newsCategory)
                return TopHeadlineFragment()
            }
            4 -> {
                // business
                newsViewModel.newsCategory = "business"
                Log.e("category",newsViewModel.newsCategory)
                return TopHeadlineFragment()
            }
            5 -> {
                // health
                newsViewModel.newsCategory = "health"
                Log.e("category",newsViewModel.newsCategory)
                return TopHeadlineFragment()
            }
            6 -> {
                // entertainment
                newsViewModel.newsCategory = "entertainment"
                Log.e("category",newsViewModel.newsCategory)
                return TopHeadlineFragment()
            }
            else -> return TopHeadlineFragment()
        }
    }

    override fun getItemCount(): Int {
        return numberOfTabs
    }
}