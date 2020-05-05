package com.con19.tripplanner.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.con19.tripplanner.view.HomePagerAdapter
import com.con19.tripplanner.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator

/**
 * Entry point to the app.
 * Contains View Pager that swipes between trips, profile and
 */
class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPager: ViewPager2 = findViewById(R.id.pager)
        viewPager.adapter = HomePagerAdapter(this)

        val tabLayout: TabLayout = findViewById(R.id.tab_layout)

        tabLayout.apply {
            tabIconTint = null
            addOnTabSelectedListener(object: OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) {}

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    tab?.tabLabelVisibility = TabLayout.TAB_LABEL_VISIBILITY_UNLABELED
                }

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.tabLabelVisibility = TabLayout.TAB_LABEL_VISIBILITY_LABELED
                }
            })
        }


        TabLayoutMediator(tabLayout, viewPager) { tab, position ->

            tab.text = when (position) {
                HomePagerAdapter.TRIPS_POSITION -> getString(R.string.trips)
                HomePagerAdapter.PEOPLE_POSITION -> getString(R.string.people)
                else -> getString(R.string.settings)
            }

            tab.icon = when (position) {
                HomePagerAdapter.TRIPS_POSITION -> R.drawable.tab_trips_selector
                HomePagerAdapter.PEOPLE_POSITION -> R.drawable.tab_people_selector
                else -> R.drawable.tab_settings_selector
            }.let { getDrawable(it) }

            tab.tabLabelVisibility = TabLayout.TAB_LABEL_VISIBILITY_UNLABELED

        }.attach()


    }
}
