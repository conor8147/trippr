package com.con19.tripplanner.view.fragments

import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.con19.tripplanner.R
import com.con19.tripplanner.db.entities.Trip
import com.con19.tripplanner.db.entities.TripWithPeople
import com.con19.tripplanner.viewmodel.TripViewModel
import com.google.android.material.chip.Chip
import kotlin.properties.Delegates

class EditTripFragment() : AddTripFragment() {
    private var tripId by Delegates.notNull<Long>()
    private lateinit var tripWithPeople: TripWithPeople
    private lateinit var tripViewModel: TripViewModel
    private lateinit var trip: Trip

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tripId = it.getLong(TRIP_ID)
        }
        tripViewModel = ViewModelProvider(requireActivity())
            .get(TripViewModel::class.java)

        val tempTrip = tripId.let { tripViewModel.getTripById(it) }
            ?: throw Resources.NotFoundException()
        // TODO: handle this gracefully

        tripWithPeople = tempTrip
        trip = tripWithPeople.trip
    }

    override fun initViews(layout: View) {
        super.initViews(layout)
        nameEditText.setText(trip.tripName)
        tripWithPeople.people.forEach {
            addedPeople.add(it)
            val chip = Chip(requireContext())
            chip.text = it.nickname
            chipGroup.addView(chip)
        }
        trip.coverPhoto?.let {
            tripCoverPhoto.setImageURI(Uri.parse(it))
        }

        startDate = trip.startDate
        endDate = trip.endDate
    }

    override fun setToolbarListeners() {
        super.setToolbarListeners()
        toolbar.apply {
            title = context.getString(R.string.edit_receipt)
        }
    }

    override fun submitTrip() {
        trip.tripName = nameEditText.text.toString()
        trip.startDate = startDate
        trip.endDate = endDate
        if (!tripPhotoUri.isNullOrEmpty()) {
            deletePhoto(trip.coverPhoto)    // delete the old cover pic to save memory
            trip.coverPhoto = tripPhotoUri
        }
        tripViewModel.update(trip, addedPeople)
        listener?.onAddTripFragmentFinished()
    }

    override fun navigateBack() {
        if (!tripPhotoSaved) {
            deletePhoto(tripPhotoUri)
        }
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