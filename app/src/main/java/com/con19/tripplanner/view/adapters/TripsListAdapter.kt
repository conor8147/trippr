package com.con19.tripplanner.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.con19.tripplanner.R
import com.con19.tripplanner.db.entities.TripWithPeople
import com.con19.tripplanner.model.PersonService
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.view_trip_card.view.*

class TripsListAdapter internal constructor(
    private val context: Context,
    private val personService: PersonService
) : RecyclerView.Adapter<TripsListAdapter.TripViewHolder>(){

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    var tripList = emptyList<TripWithPeople>()
        set(trip) {
            field = trip
            notifyDataSetChanged()
        }

    class TripViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tripTitle: TextView = view.trip_title
        val tripDates: TextView = view.trip_dates
        val tripPhoto: ImageView = view.trip_photo
        val tripPeople: ChipGroup = view.trip_people
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val tripCard = inflater.inflate(R.layout.view_trip_card, parent, false)
        return TripViewHolder(tripCard)
    }

    override fun getItemCount(): Int =  tripList.size

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val currentTripWithMembers = tripList[position]
        holder.tripTitle.text = currentTripWithMembers.trip.tripName
        holder.tripDates.text = "${currentTripWithMembers.trip.startDate} - ${currentTripWithMembers.trip.endDate}"
        holder.tripPeople.removeAllViews()
        // holder.tripPhoto.setImageResource(currentTrip.coverPhoto)
        currentTripWithMembers.people.forEach {
            val name = it.nickname
            val chip = Chip(context)
            chip.text = name
            holder.tripPeople.addView(chip)
        }
    }
}