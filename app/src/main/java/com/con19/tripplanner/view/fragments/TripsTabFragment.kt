package com.con19.tripplanner.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.con19.tripplanner.R

/**
 * A container fragment for all the fragments located on the trips tab.
 * These are inflated into this fragment as necessary.
 * All navigation between fragments should be handled here
 */
class TripsTabFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_root, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        openTripHomeFragment()
    }

    fun openTripViewFragment(tripId: Long) {
        val newFrag = TripViewFragment.newInstance(tripId)
        replaceCurrentFragmentWith(newFrag)
    }

    fun openTripHomeFragment() {
        replaceCurrentFragmentWith(TripsHomeFragment())
    }

    fun openAddReceiptFragment(tripId: Long) {
        val newFrag = AddReceiptFragment.newInstance(tripId)
        replaceCurrentFragmentWith(newFrag)
    }

    fun openAddTripFragment() {
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, AddTripFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun openEditReceiptFragment(tripId: Long, transactionId: Long) {
        replaceCurrentFragmentWith(
            EditReceiptFragment.newInstance(tripId, transactionId)
        )
    }

    fun openEditTripFragment(tripId: Long) {
        replaceCurrentFragmentWith(
            EditTripFragment.newInstance(tripId)
        )
    }

    private fun replaceCurrentFragmentWith(newFrag: Fragment) {
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, newFrag)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}