package com.tuwaiq.newsplanet.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tuwaiq.newsplanet.fragments.CategoryFragments.BusinessFragment
import com.tuwaiq.newsplanet.fragments.CategoryFragments.EntertainmentFragment
import com.tuwaiq.newsplanet.fragments.CategoryFragments.HealthFragment
import com.tuwaiq.newsplanet.fragments.CategoryFragments.ScienceFragment
import com.tuwaiq.newsplanet.fragments.SportsFragment
import com.tuwaiq.newsplanet.fragments.TechnologyFragment
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
                // business ..
                return BusinessFragment()
            }
            5 -> {
                // health ..
                return HealthFragment()
            }
            6 -> {
                // entertainment ..
                return EntertainmentFragment()
            }
            else -> return TopHeadlineFragment("general")
        }
    }

    override fun getItemCount(): Int {
        return numberOfTabs
    }
}