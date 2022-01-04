package com.tuwaiq.newsplanet.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tuwaiq.newsplanet.fragments.TopHeadlineFragment

class TabsPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle, private var numberOfTabs: Int ) : FragmentStateAdapter(fm, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                // general ..
                return TopHeadlineFragment("general")
            }
            1 -> {
                // technology ..
                return TopHeadlineFragment("technology")
            }
            2 -> {
                // sports ..
                return TopHeadlineFragment("sports")
            }
            3 -> {
                // science ..
                return TopHeadlineFragment("science")
            }
            4 -> {
                // business ..
                return TopHeadlineFragment("business")
            }
            5 -> {
                // health ..
                return TopHeadlineFragment("health")
            }
            6 -> {
                // entertainment ..
                return TopHeadlineFragment("entertainment")
            }
            else -> return TopHeadlineFragment("general")
        }
    }

    override fun getItemCount(): Int {
        return numberOfTabs
    }
}