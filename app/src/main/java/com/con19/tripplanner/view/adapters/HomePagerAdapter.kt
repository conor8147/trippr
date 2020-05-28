package com.con19.tripplanner.view.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomePagerAdapter(
    parent: AppCompatActivity,
    private val tripsTabFragment: Fragment,
    private val peopleTabFragment: Fragment,
    private val settingsFragment: Fragment
) : FragmentStateAdapter(parent) {

    override fun getItemCount(): Int = 3

    /**
     * Create the tab fragment for the given position
     * @param position Int
     * @returns the tab fragment inflated inside hands_split rootFragment
     */
    override fun createFragment(position: Int): Fragment =
        when (position) {
            TRIPS_POSITION -> tripsTabFragment
            PEOPLE_POSITION -> peopleTabFragment
            else -> settingsFragment
        }


    companion object {
        const val TRIPS_POSITION = 0
        const val PEOPLE_POSITION = 1
        const val SETTINGS_POSITION = 2
    }
}
