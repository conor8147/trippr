package com.con19.tripplanner.view.fragments


import android.content.Context
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
import com.con19.tripplanner.view.adapters.TripsListAdapter
import com.con19.tripplanner.viewmodel.TripViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.RuntimeException

/**
 * Fragment for the Home Tab.
 */
class TripsHomeFragment : Fragment() {
    private lateinit var tripViewModel: TripViewModel
    private var listener : TripsHomeFragmentListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tripViewModel = ViewModelProvider(requireActivity())
            .get(TripViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_trips_home, container, false)

        view.findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {
            listener?.onFABSelected()
        }

        createRecyclerView(view)

        return view
    }

    interface TripsHomeFragmentListener{
        fun onFABSelected()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TripsHomeFragment.TripsHomeFragmentListener) {
            this.listener = context
        } else {
            throw RuntimeException("$context must implement TripsHomeFragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        this.listener = null
    }

    private fun createRecyclerView(view: View) {
        val viewManager = LinearLayoutManager(requireContext())
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        val tripsListAdapter = TripsListAdapter(requireContext()
        )
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = tripsListAdapter
        }

        tripViewModel.allTrips.observe(viewLifecycleOwner, Observer {trips ->
            // Update the cached copy of the words in the adapter.
            trips?.let { tripsListAdapter.tripList = it }
        })    }
}
