package com.con19.tripplanner.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.con19.tripplanner.R
import com.con19.tripplanner.db.dao.TripDao
import com.con19.tripplanner.model.PersonService
import com.con19.tripplanner.model.TripService
import com.con19.tripplanner.view.adapters.HomePagerAdapter
import com.con19.tripplanner.view.fragments.PeopleTabFragment
import com.con19.tripplanner.view.fragments.SettingsTabFragment
import com.con19.tripplanner.view.fragments.TripsTabFragment
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

    private lateinit var viewPager: ViewPager2

    private val tripsTabFragment  = TripsTabFragment()
    private val basePeopleFragment: Fragment = PeopleTabFragment()
    private val currentSettingsTabFragment: Fragment = SettingsTabFragment()

    lateinit var personService: PersonService

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

        personService = personViewModel.service
    }

    /**
     * Initialise the ViewPager containing the full-screen fragments
     */
    private fun initialisePager() {
        viewPager = findViewById(R.id.pager)
        viewPager.adapter = HomePagerAdapter(
            this,
            tripsTabFragment,
            basePeopleFragment,
            currentSettingsTabFragment
        )

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

    /**
     * A bit of a hack, but necessary for back in nested fragments to work properly
     */
    override fun onBackPressed() {
        // if there is a fragment and the back stack of this fragment is not empty,
        // then emulate 'onBackPressed' behaviour, because in default, it is not working
        val fm = supportFragmentManager
        for (frag in fm.fragments) {
            if (frag.isVisible) {
                val childFm = frag.childFragmentManager
                if (childFm.backStackEntryCount > 0) {
                    childFm.popBackStack()
                    return
                }
            }
        }
        super.onBackPressed()
    }
}
