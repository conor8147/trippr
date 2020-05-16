package com.con19.tripplanner.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.con19.tripplanner.R
import com.con19.tripplanner.view.adapters.PeopleAdapter
import com.con19.tripplanner.viewmodel.PersonViewModel

/**
 * A simple [Fragment] subclass.
 */
class TripViewFragment : Fragment(), PeopleAdapter.OnPersonClickedListener {

    private lateinit var viewManager: LinearLayoutManager
    //TODO change this to be a transactionViewModel
    private lateinit var personViewModel: PersonViewModel

    // TODO change this to transactions. part of people list test
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        personViewModel = ViewModelProvider(requireActivity())
            .get(PersonViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_trip_view, container, false)
        initialiseRecyclerView(layout)
        return layout
    }

    //TODO change this to contain transactions rather than people
    private fun initialiseRecyclerView(layout: View) {
        viewManager = LinearLayoutManager(requireContext())
        val recyclerView: RecyclerView = layout.findViewById(R.id.recyclerView)
        val peopleAdapter = PeopleAdapter(requireContext(), this)

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = peopleAdapter
        }

        personViewModel.allPeople.observe(viewLifecycleOwner, Observer { people ->
            // Update the cached copy of the words in the adapter.
            people?.let { peopleAdapter.peopleList = it }
        })
    }

    // TODO remove this. part of people list test
    override fun onPersonClicked(personId: Long) {

    }

}
