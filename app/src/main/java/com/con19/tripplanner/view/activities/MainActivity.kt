package com.con19.tripplanner.view.activities

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.con19.tripplanner.R
import com.con19.tripplanner.view.adapters.HomePagerAdapter
import com.con19.tripplanner.view.adapters.PeopleAdapter
import com.con19.tripplanner.view.adapters.TripsListAdapter
import com.con19.tripplanner.view.fragments.*
import com.con19.tripplanner.viewmodel.PersonViewModel
import com.con19.tripplanner.viewmodel.TransactionViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Entry point to the app.
 * Contains View Pager that swipes between trips, people and settings
 */
class MainActivity :
    AppCompatActivity(),
    PeopleAdapter.OnPersonClickedListener,
    TripsListAdapter.TripsListListener,
    TripViewFragment.TripViewListener,
    AddReceiptFragment.AddReceiptFragmentListener,
    AddPersonFragment.AddPersonFragmentListener,
    PeopleHomeFragment.PeopleHomeFragmentListener,
    EditPersonFragment.EditPersonFragmentListener,
    TripsHomeFragment.TripsHomeFragmentListener,
    AddTripFragment.AddTripFragmentListener {

    private lateinit var viewPager: ViewPager2

    private val tripsTabFragment = TripsTabFragment()
    private val peopleTabFragment = PeopleTabFragment()
    private val currentSettingsTabFragment = SettingsFragment()

    private var firstTimeOpening = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (firstTimeOpening) {
            loadApp()
            firstTimeOpening = false
        }
        initialisePager()
    }

    /**
     * Display the loading screen while the database info is collected and displayed on the home screen.
     */
    private fun loadApp() {
        val splashScreen = SplashScreenFragment()
        val mainContainer = findViewById<LinearLayout>(R.id.main_container)
        val fragmentManager = supportFragmentManager

        // Hide app and replace with splash screen
        fragmentManager.beginTransaction()
            .add(R.id.splash_screen_container, splashScreen)
            .commit()

        // Asynchronously wait to reveal the app once it is loaded.
        lifecycleScope.launch {
            delay(3000L)
            fragmentManager.beginTransaction()
                .remove(splashScreen)
                .commit()
        }
    }

    /**
     * Initialise the ViewPager containing the full-screen fragments
     */
    private fun initialisePager() {
        viewPager = findViewById(R.id.pager)
        viewPager.adapter = HomePagerAdapter(
            this,
            tripsTabFragment,
            peopleTabFragment,
            currentSettingsTabFragment
        )

        val tabLayout: TabLayout = findViewById(R.id.tab_layout)

        tabLayout.apply {
            tabIconTint = null
            addOnTabSelectedListener(object : OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        HomePagerAdapter.TRIPS_POSITION -> tripsTabFragment.openTripHomeFragment()
                        HomePagerAdapter.PEOPLE_POSITION -> peopleTabFragment.openPeopleHomeFragment()
                    }
                }

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
     * Yes the double nesting is gross, but it's kinda necessary for the nested camera-in-add-transaction-in-trips-tab thing.
     * Any more nesting than that, and there will need to be a better way of doing this.
     *
     * Jasmine I'm so sorry
     */
    override fun onBackPressed() {
        // if there is a fragment and the back stack of this fragment is not empty,
        // then emulate 'onBackPressed' behaviour, because in default, it is not working
        val fm = supportFragmentManager
        for (frag in fm.fragments) {
            if (frag.isVisible) {
                val childFm = frag.childFragmentManager
                if (childFm.fragments.size > 0) {
                    for (nestedFrag in childFm.fragments) {
                        if (nestedFrag.isVisible) {
                            val nestedChildFm = nestedFrag.childFragmentManager
                            if (nestedChildFm.backStackEntryCount > 0) {
                                nestedChildFm.popBackStack()
                                return
                            }
                        }
                    }
                    if (childFm.backStackEntryCount > 0) {
                        childFm.popBackStack()
                        return
                    }
                }
            }
            super.onBackPressed()
        }
    }

    override fun onPersonClicked(personId: Long) {
        peopleTabFragment.openEditPersonTab(personId)
    }

    override fun onTripClicked(tripId: Long) {
        tripsTabFragment.openTripViewFragment(tripId)
    }

    override fun onBackSelected() {
        tripsTabFragment.openTripHomeFragment()
    }

    override fun onErrorOpeningTripViewFragment() {
        tripsTabFragment.openTripHomeFragment()
    }

    override fun onAddReceiptButtonClicked(tripId: Long) {
        tripsTabFragment.openAddReceiptFragment(tripId)
    }

    override fun onTransactionSelected(tripId: Long, transactionId: Long) {
        tripsTabFragment.openEditReceiptFragment(tripId, transactionId)
    }

    override fun onEditClicked(tripId: Long) {
        tripsTabFragment.openEditTripFragment(tripId)
    }

    override fun onFABSelected() {
        tripsTabFragment.openAddTripFragment()
    }

    override fun onReceiptFragmentBackButtonPressed(tripId: Long) {
        tripsTabFragment.openTripViewFragment(tripId)
    }

    override fun onFinished() {
        peopleTabFragment.openPeopleHomeFragment()
    }

    override fun onAddPersonClicked() {
        peopleTabFragment.openAddPersonFragment()
    }

    override fun onAddTripFragmentFinished() {
        tripsTabFragment.openTripHomeFragment()
    }

    override fun onEditTripBackPressed(tripId: Long) {
        tripsTabFragment.openTripViewFragment(tripId)
    }
}
