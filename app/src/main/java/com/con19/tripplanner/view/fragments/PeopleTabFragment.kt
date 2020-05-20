package com.con19.tripplanner.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.con19.tripplanner.R

/**
 * A container fragment for all the fragments located on the people tab.
 * These are inflated into this fragment as necessary.
 * All navigation between fragments should be handled here
 */
class PeopleTabFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_root, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val transaction = childFragmentManager.beginTransaction()
        // When this container fragment is created, we fill it with our first "real" fragment
        transaction.replace(R.id.frame, PeopleHomeFragment())
        transaction.commit()
    }

    fun openEditPersonTab(personId: Long) {
        val newFrag = EditPersonFragment.newInstance(personId)
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, newFrag)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun openPeopleHomeFragment() {
        val newFrag = PeopleHomeFragment()
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, newFrag)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}