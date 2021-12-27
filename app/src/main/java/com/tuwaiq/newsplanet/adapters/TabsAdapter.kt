package com.tuwaiq.newsplanet.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tuwaiq.newsplanet.fragments.SportsFragment
import com.tuwaiq.newsplanet.fragments.TechnologyFragment
import com.tuwaiq.newsplanet.fragments.TopHeadlineFragment

class TabsPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle, private var numberOfTabs: Int ) : FragmentStateAdapter(fm, lifecycle) {


    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                // general
// # Music Fragment
                val topHeadlineFragment = TopHeadlineFragment()
                return topHeadlineFragment
            }
            1 -> {
                // # technology
                // # Movies Fragment
                val technologyFragment = TechnologyFragment()
                return technologyFragment
            }
            2 -> {
                // # sports
                // # Books Fragment
                val sportFragment = SportsFragment()
                return sportFragment
            }
            3 -> {
//                //science
//                newsViewModel.newsCategory = "science"
//                Log.e("category",newsViewModel.newsCategory)
                return TopHeadlineFragment()
            }
            4 -> {
                // business
//                newsViewModel.newsCategory = "business"
//                Log.e("category",newsViewModel.newsCategory)
                return TopHeadlineFragment()
            }
            5 -> {
                // health
//                newsViewModel.newsCategory = "health"
//                Log.e("category",newsViewModel.newsCategory)
                return TopHeadlineFragment()
            }
            6 -> {
                // entertainment
//                newsViewModel.newsCategory = "entertainment"
//                Log.e("category",newsViewModel.newsCategory)
                return TopHeadlineFragment()
            }
            else -> return TopHeadlineFragment()
        }
    }

    override fun getItemCount(): Int {
        return numberOfTabs
    }
}