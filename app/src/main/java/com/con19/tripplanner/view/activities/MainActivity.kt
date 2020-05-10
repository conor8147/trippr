package com.con19.tripplanner.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.con19.tripplanner.R
import com.con19.tripplanner.view.adapters.HomePagerAdapter
import com.con19.tripplanner.viewmodel.PersonViewModel
import com.con19.tripplanner.viewmodel.TransactionViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator

/**
 * Entry point to the app.
 * Contains View Pager that swipes between trips, people and settings
 */
class MainActivity : AppCompatActivity() {
    
    private lateinit var personViewModel: PersonViewModel
    private lateinit var transactionViewModel: TransactionViewModel
    // TODO: Jasmine to initialise TripViewModel here and set below

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialisePager()

        personViewModel = ViewModelProvider(this)
            .get(PersonViewModel::class.java)
        transactionViewModel = ViewModelProvider(this)
            .get(TransactionViewModel::class.java)
    }

    /**
     * Initialise the ViewPager containing the full-screen fragments
     */
    private fun initialisePager() {
        val viewPager: ViewPager2 = findViewById(R.id.pager)
        viewPager.adapter =
            HomePagerAdapter(this)

        val tabLayout: TabLayout = findViewById(R.id.tab_layout)

        tabLayout.apply {
            tabIconTint = null
            addOnTabSelectedListener(object : OnTabSelectedListener {
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
