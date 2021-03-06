package com.con19.tripplanner.view.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.con19.tripplanner.R
import com.con19.tripplanner.db.entities.Person
import com.con19.tripplanner.db.entities.TripWithPeople
import com.con19.tripplanner.view.adapters.TransactionsAdapter
import com.con19.tripplanner.viewmodel.TransactionViewModel
import com.con19.tripplanner.viewmodel.TripViewModel
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.launch
import java.text.DecimalFormat


class TripViewFragment : Fragment(), TransactionsAdapter.TransactionsAdapterListener {
    private val TAG = this::class.simpleName

    private var tripId: Long? = null
    private lateinit var viewManager: LinearLayoutManager
    private lateinit var tripViewModel: TripViewModel
    private lateinit var transactionViewModel: TransactionViewModel
    private var trip: TripWithPeople? = null
    private lateinit var toolbar: MaterialToolbar

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
            listener?.onErrorOpeningTripViewFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_trip_view, container, false)
        toolbar = layout.findViewById<MaterialToolbar>(R.id.topAppBar)
        layout.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayout).title =
            trip?.trip?.tripName ?: "Trip Not Found"
        initialiseRecyclerView(layout)

        layout.findViewById<MaterialToolbar>(R.id.topAppBar).setNavigationOnClickListener {
            listener?.onBackSelected()
        }

        layout.findViewById<MaterialToolbar>(R.id.topAppBar)?.setOnMenuItemClickListener { _ ->
            tripId?.let { listener?.onEditClicked(it) }
            true
        }

        return layout
    }

    private fun initialiseRecyclerView(layout: View) {
        viewManager = LinearLayoutManager(requireContext())
        val recyclerView: RecyclerView = layout.findViewById(R.id.recyclerView)
        val transactionAdapter = trip?.let {
            TransactionsAdapter(
                requireContext(),
                this,
                it
            )
        }

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

    override fun onAddReceiptButtonClicked() {
        tripId?.let { listener?.onAddReceiptButtonClicked(it) }
    }

    override fun onSplitCostsClicked() {
        val df = DecimalFormat("#.00")
        val accountNumber = PreferenceManager
            .getDefaultSharedPreferences(requireContext())
            .getString("bank_details", "(None Provided)")
        val message = StringBuilder()
            .append("Time to settle up for your trip ${trip?.trip?.tripName}. Here's how much everyone owes:\n\n")

        val phoneNumbers = StringBuilder()

        lifecycleScope.launch {
            // running this inside an async launch thing as it may take quite some time to finish.
            val peopleCostsMap: List<Pair<Person, Float>>? =
                tripId?.let { transactionViewModel.settleUpTrip(it) }
            peopleCostsMap?.forEach {
                message.append("${it.first.nickname} owes \$${df.format(it.second)}\n")
                phoneNumbers.append("${it.first.phoneNumber},")
            }

            message.append("\nPayable to: $accountNumber")
            val sendIntent = Intent(Intent.ACTION_SENDTO)
            sendIntent.data = Uri.parse("smsto:" + phoneNumbers.toString())
            sendIntent.putExtra("sms_body", message.toString())
            ContextCompat.startActivity(requireContext(), sendIntent, null)

        }
    }

    override fun onTransactionSelected(transactionId: Long) {
        tripId?.let { listener?.onTransactionSelected(it, transactionId) }
    }

    interface TripViewListener {
        fun onBackSelected()
        fun onErrorOpeningTripViewFragment()
        fun onAddReceiptButtonClicked(tripId: Long)
        fun onTransactionSelected(tripId: Long, transactionId: Long)
        fun onEditClicked(tripId: Long)
    }

    companion object {
        private const val TRIP_ID = "trip_id"
        @JvmStatic
        fun newInstance(tripId: Long) =
            TripViewFragment().apply {
                arguments = Bundle().apply {
                    putLong(TRIP_ID, tripId)
                }
            }
    }
}
