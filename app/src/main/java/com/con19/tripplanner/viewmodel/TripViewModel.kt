package com.con19.tripplanner.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.con19.tripplanner.db.AppDatabase
import com.con19.tripplanner.db.entities.Person
import com.con19.tripplanner.db.entities.Trip
import com.con19.tripplanner.db.entities.TripPersonCrossRef
import com.con19.tripplanner.db.entities.TripWithPeople
import com.con19.tripplanner.model.TripService
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TripViewModel(application: Application) : AndroidViewModel(application) {
    private val service: TripService
    val allTrips: LiveData<List<TripWithPeople>>

    init {
        val tripDao = AppDatabase.getDatabase(application, viewModelScope).tripDao()
        service = TripService.getInstance(tripDao)
        allTrips = service.allTrips
    }

    //    async can return things, like the id after insertion
    fun insertAsync(trip: Trip, peopleOnTrip: List<Person>): Deferred<Long> {
        return viewModelScope.async(Dispatchers.IO) {
            service.insert(trip, peopleOnTrip)
        }
    }

    fun addPersonToTrip(tripId: Long, personId: Long) = viewModelScope.launch(Dispatchers.IO) {
        service.addPersonToTrip(tripId, personId)
    }

    fun removePersonFromTrip(tripId: Long, personId: Long) = viewModelScope.launch(Dispatchers.IO) {
        service.removePersonFromTrip(tripId, personId)
    }

    fun getTripById(tripId: Long): TripWithPeople? {
        return allTrips.value?.find {
            it.trip.tripId == tripId
        }
    }

}