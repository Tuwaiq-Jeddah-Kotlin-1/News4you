package com.tuwaiq.newsplanet.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tuwaiq.newsplanet.fragments.TopHeadlineFragment

class TabsPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle, private var numberOfTabs: Int) : FragmentStateAdapter(fm, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                val topHeadlineFragment = TopHeadlineFragment()
                return topHeadlineFragment
            }
            1 -> {
                val topHeadlineFragment = TopHeadlineFragment()
                return topHeadlineFragment
            }
            2 -> {
                // # Books Fragment
                val topHeadlineFragment = TopHeadlineFragment()
                return topHeadlineFragment
            }
            3 -> {
                // # Books Fragment
                val topHeadlineFragment = TopHeadlineFragment()
                return topHeadlineFragment
            }
            4 -> {
                // # Books Fragment
                val topHeadlineFragment = TopHeadlineFragment()
                return topHeadlineFragment
            }
            5 -> {
                // # Books Fragment
                val topHeadlineFragment = TopHeadlineFragment()
                return topHeadlineFragment
            }
            6 -> {
                // # Books Fragment
                val topHeadlineFragment = TopHeadlineFragment()
                return topHeadlineFragment
            }
            else -> return TopHeadlineFragment()
        }
    }

    override fun getItemCount(): Int {
        return numberOfTabs
    }
}