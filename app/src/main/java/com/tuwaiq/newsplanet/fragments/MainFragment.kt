package com.tuwaiq.newsplanet.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tuwaiq.newsplanet.R
import com.tuwaiq.newsplanet.adapters.TabsPagerAdapter
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_top_headlines_news.*

class MainFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTabs()
    }

    fun setupTabs(){
        // Tabs Customization
        tab_layout.setSelectedTabIndicatorColor(Color.parseColor("#f9f9f9"))
        tab_layout.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        tab_layout.tabTextColors = ContextCompat.getColorStateList(requireContext(), R.color.colorPrimaryDark)

        // Set different Text Color for Tabs for when are selected or not
        //tab_layout.setTabTextColors(R.color.normalTabTextColor, R.color.selectedTabTextColor)

        // Number Of Tabs
        val numberOfTabs = 7

        // Set Tabs in the center
        //tab_layout.tabGravity = TabLayout.GRAVITY_CENTER

        // Show all Tabs in screen
        tab_layout.tabMode = TabLayout.MODE_SCROLLABLE

        // Scroll to see all Tabs
        //tab_layout.tabMode = TabLayout.MODE_SCROLLABLE

        // Set Tab icons next to the text, instead above the text
        tab_layout.isInlineLabel = true

        // Set the ViewPager Adapter
        val adapter = TabsPagerAdapter(getChildFragmentManager(), lifecycle, numberOfTabs)
        tabs_viewpager.adapter = adapter

        // Enable Swipe
        tabs_viewpager.isUserInputEnabled = true



        // Link the TabLayout and the ViewPager2 together and Set Text & Icons
        TabLayoutMediator(tab_layout, tabs_viewpager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.general)
                    tab.setIcon(R.drawable.newspaper)
                }
                1 -> {
                    tab.text = getString(R.string.technology)
                    tab.setIcon(R.drawable.tech)
                }
                2 -> {
                    tab.text = getString(R.string.sports)
                    tab.setIcon(R.drawable.sport)
                }
                3 -> {
                    tab.text = getString(R.string.science)
                    tab.setIcon(R.drawable.ic_science)
                }
                4 -> {
                    tab.text = getString(R.string.business)
                    tab.setIcon(R.drawable.buisness)
                }
                5 -> {
                    tab.text = getString(R.string.health)
                    tab.setIcon(R.drawable.health)
                }
                6 -> {
                    tab.text = getString(R.string.entertainment)
                    tab.setIcon(R.drawable.entertainment)
                }
            }
            // Change color of the icons
            tab.icon?.colorFilter =
                BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                    Color.parseColor("#22485C"),
                    BlendModeCompat.SRC_ATOP
                )
        }.attach()
    }
}