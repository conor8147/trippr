package com.con19.tripplanner.view.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.con19.tripplanner.R
import com.con19.tripplanner.db.entities.Trip
import com.con19.tripplanner.view.activities.MainActivity
import com.con19.tripplanner.view.adapters.PeopleAdapter
import com.con19.tripplanner.view.adapters.TripsListAdapter
import com.con19.tripplanner.viewmodel.PersonViewModel
import com.con19.tripplanner.viewmodel.TripViewModel

/**
 * Fragment for the Settings Tab.
 */
class TripsTabFragment : Fragment() {
    private lateinit var tripViewModel: TripViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tripViewModel = ViewModelProvider(requireActivity())
            .get(TripViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_trips_tab, container, false)

        createRecyclerView(view)

        return view
    }

    private fun createRecyclerView(view: View) {
        val viewManager = LinearLayoutManager(requireContext())
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        val tripsListAdapter = TripsListAdapter(requireContext(),
            (requireActivity() as MainActivity).personService
        )
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = tripsListAdapter
        }

        tripViewModel.allTrips.observe(viewLifecycleOwner, Observer {trips ->
            // Update the cached copy of the words in the adapter.
            trips?.let { tripsListAdapter.tripList = it }
        })

    }


}
