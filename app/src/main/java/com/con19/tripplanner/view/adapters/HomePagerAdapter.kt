package com.con19.tripplanner.view.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.con19.tripplanner.view.fragments.PeopleTabFragment
import com.con19.tripplanner.view.fragments.SettingsTabFragment
import com.con19.tripplanner.view.fragments.TripsTabFragment

class HomePagerAdapter(parent: AppCompatActivity): FragmentStateAdapter(parent) {

    override fun getItemCount(): Int = 3

    /**
     * Create the tab fragment for the given position
     * @param position Int
     * @returns the tab fragment
     */
    override fun createFragment(position: Int): Fragment =
        when (position) {
            TRIPS_POSITION -> TripsTabFragment()
            PEOPLE_POSITION -> PeopleTabFragment()
            else -> SettingsTabFragment()
        }

    companion object {
        const val TRIPS_POSITION = 0
        const val PEOPLE_POSITION = 1
        const val SETTINGS_POSITION = 2
    }
}
