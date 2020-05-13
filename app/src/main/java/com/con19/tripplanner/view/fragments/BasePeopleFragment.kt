package com.con19.tripplanner.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.con19.tripplanner.R

class BasePeopleFragment : Fragment(), PeopleTabFragment.OnPersonSelectedListener {

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
        transaction.replace(R.id.frame, PeopleTabFragment())
        transaction.commit()
    }

    override fun onPersonSelected(personId: Long) {
        openEditPersonTab(personId)
    }

    private fun openEditPersonTab(personId: Long) {
        val newFrag = EditPersonFragment.newInstance(personId)
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, newFrag)
        transaction.commit()
    }
}