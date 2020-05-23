package com.con19.tripplanner.view.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.con19.tripplanner.R
import com.con19.tripplanner.db.entities.TripWithPeople
import com.con19.tripplanner.view.adapters.TransactionsAdapter
import com.con19.tripplanner.viewmodel.TransactionViewModel
import com.con19.tripplanner.viewmodel.TripViewModel
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar


const val TRIP_ID = "trip_id"

class TripViewFragment : Fragment() {
     private val TAG = this::class.simpleName

    private var tripId: Long? = null
    private lateinit var viewManager: LinearLayoutManager
    private lateinit var tripViewModel: TripViewModel
    private lateinit var transactionViewModel: TransactionViewModel
    private var trip: TripWithPeople? = null

    private var listener: TripViewListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tripViewModel = ViewModelProvider(requireActivity())
            .get(TripViewModel::class.java)
        transactionViewModel = ViewModelProvider(requireActivity())
            .get(TransactionViewModel::class.java)

        arguments?.let {
            tripId = it.getLong(TRIP_ID)
        }
        trip = tripId?.let { tripViewModel.getTripById(it) }

        if (trip == null) {
            Log.e(TAG, "Error opening TripViewFragment, tripId was $tripId")
            listener?.onErrorOpeningTripViewFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_trip_view, container, false)
        layout.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayout).title =
            trip?.trip?.tripName ?: "Trip Not Found"
        initialiseRecyclerView(layout)

        layout.findViewById<MaterialToolbar>(R.id.topAppBar).setNavigationOnClickListener {
            listener?.onBackSelected()
        }

        return layout
    }

    private fun initialiseRecyclerView(layout: View) {
        viewManager = LinearLayoutManager(requireContext())
        val recyclerView: RecyclerView = layout.findViewById(R.id.recyclerView)
        val transactionAdapter = trip?.let { TransactionsAdapter(requireContext(), it) }

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = transactionAdapter
        }

        tripId?.let { tripId ->
            transactionViewModel.getTransactionsForTrip(tripId)
                .observe(viewLifecycleOwner, Observer { transactions ->
                    // Update the cached copy of the words in the adapter.
                    transactions?.let { transactionAdapter?.allTransactions = it }
                })
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TripViewListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement TripViewListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface TripViewListener {
        fun onBackSelected()
        fun onErrorOpeningTripViewFragment()
    }

    companion object {
        @JvmStatic
        fun newInstance(tripId: Long) =
            TripViewFragment().apply {
                arguments = Bundle().apply {
                    putLong(TRIP_ID, tripId)
                }
            }
    }
}
