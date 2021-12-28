package com.tuwaiq.newsplanet.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tuwaiq.newsplanet.fragments.CategoryFragments.BusinessFragment
import com.tuwaiq.newsplanet.fragments.CategoryFragments.ScienceFragment
import com.tuwaiq.newsplanet.fragments.SportsFragment
import com.tuwaiq.newsplanet.fragments.TechnologyFragment
import com.tuwaiq.newsplanet.fragments.TopHeadlineFragment

class TabsPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle, private var numberOfTabs: Int ) : FragmentStateAdapter(fm, lifecycle) {


    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                // general ..
                return TopHeadlineFragment()
            }
            1 -> {
                // technology ..
                return TechnologyFragment()
            }
            2 -> {
                // sports ..
                return SportsFragment()
            }
            3 -> {
                // science ..
                return ScienceFragment()
            }
            4 -> {
                // business
                return BusinessFragment()
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