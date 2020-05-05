package com.con19.tripplanner.view.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.con19.tripplanner.R
import com.con19.tripplanner.entities.Person

/**
 * Fragment for the Settings Tab.
 */
class PeopleTabFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout: View = inflater.inflate(R.layout.fragment_people_tab, container, false)

        val recyclerView: RecyclerView = layout.findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(requireContext())
        val

        return layout
    }
}
