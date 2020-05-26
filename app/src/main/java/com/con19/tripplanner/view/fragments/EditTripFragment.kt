package com.con19.tripplanner.view.fragments

import android.content.res.Resources
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.con19.tripplanner.R
import com.con19.tripplanner.db.entities.Trip
import com.con19.tripplanner.db.entities.TripWithPeople
import com.con19.tripplanner.viewmodel.TripViewModel
import kotlin.properties.Delegates

class EditTripFragment() : AddTripFragment() {
    private var tripId by Delegates.notNull<Long>()
    private lateinit var tripWithPeople: TripWithPeople
    private lateinit var tripViewModel: TripViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tripId = it.getLong(TRIP_ID)
        }
        tripViewModel = ViewModelProvider(requireActivity())
            .get(TripViewModel::class.java)

        val trip = tripId.let { tripViewModel.getTripById(it) }

        if (trip == null) {
            throw Resources.NotFoundException()
            // TODO: handle this gracefully
        }
        this.tripWithPeople = trip
    }

    override fun setToolbarListeners() {
        super.setToolbarListeners()
        toolbar.apply {
            title = context.getString(R.string.edit_receipt)
        }
    }

    override fun navigateBack() {
        listener?.onEditTripBackPressed(tripId)
    }

    companion object {
        private const val TRIP_ID = "trip_id"
        fun newInstance(tripId: Long) =
            EditTripFragment().apply {
                arguments = Bundle().apply {
                    putLong(TRIP_ID, tripId)
                }
            }

    }
}