package com.con19.tripplanner.view.fragments

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.con19.tripplanner.R
import com.con19.tripplanner.db.entities.TripWithPeople
import com.con19.tripplanner.viewmodel.TransactionViewModel
import com.con19.tripplanner.viewmodel.TripViewModel
import com.google.android.material.chip.ChipGroup
import kotlin.properties.Delegates

class AddReceiptFragment private constructor(): Fragment() {

    private var tripId by Delegates.notNull<Long>()
    private var tripWithPeople: TripWithPeople? = null
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var tripViewModel: TripViewModel
    private lateinit var nameEditText: EditText
    private lateinit var costEditText: EditText
    private lateinit var addPersonEditText: EditText
    private lateinit var chipGroup: ChipGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tripViewModel = ViewModelProvider(requireActivity())
            .get(TripViewModel::class.java)
        transactionViewModel = ViewModelProvider(requireActivity())
            .get(TransactionViewModel::class.java)

        arguments?.let {
            tripId = it.getLong(TRIP_ID)
        }
        tripWithPeople = tripId.let { tripViewModel.getTripById(it) }

        if (tripWithPeople == null) {
            throw Resources.NotFoundException()
            // TODO: handle this gracefully
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout: View = inflater.inflate(R.layout.fragment_add_receipt, container, false)

        nameEditText = layout.findViewById(R.id.nameEditText)
        costEditText = layout.findViewById(R.id.costEditText)
        addPersonEditText = layout.findViewById(R.id.addPersonEditText)
        chipGroup = layout.findViewById(R.id.chipGroup)

        return layout
    }

    companion object {
        private const val TRIP_ID = "tripId"
        @JvmStatic
        fun newInstance(tripId: Long) =
            AddReceiptFragment().apply {
                arguments = Bundle().apply {
                    putLong(TRIP_ID, tripId)
                }
            }
    }
}