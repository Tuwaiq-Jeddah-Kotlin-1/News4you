package com.tuwaiq.newsplanet.adapters

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tuwaiq.newsplanet.db.ArticleDatabase
import com.tuwaiq.newsplanet.fragments.TopHeadlineFragment
import com.tuwaiq.newsplanet.repo.NewsRepo
import com.tuwaiq.newsplanet.ui.NewsViewModel

class TabsPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle, private var numberOfTabs: Int) : FragmentStateAdapter(fm, lifecycle) {

    val topHeadlineFragmentObject = TopHeadlineFragment()

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                // general
                //NewsViewModel.newsCategory = "general"
                topHeadlineFragmentObject.setupRecyclerView()
                return TopHeadlineFragment()
            }
            1 -> {
                // technology
                //NewsViewModel.newsCategory = "technology"
                topHeadlineFragmentObject.setupRecyclerView()
                return TopHeadlineFragment()
            }
            2 -> {
                // sports
                //NewsViewModel.newsCategory = "sports"
                return TopHeadlineFragment()
            }
            3 -> {
                // science
                //NewsViewModel.newsCategory = "science"
                return TopHeadlineFragment()
            }
            4 -> {
                // business
                //NewsViewModel.newsCategory = "business"
                return TopHeadlineFragment()
            }
            5 -> {
                // health
                //NewsViewModel.newsCategory = "health"
                return TopHeadlineFragment()
            }
            6 -> {
                // entertainment
                //NewsViewModel.newsCategory = "entertainment"
                return TopHeadlineFragment()
            }
            else -> return TopHeadlineFragment()
        }
    }

    override fun getItemCount(): Int {
        return numberOfTabs
    }
}