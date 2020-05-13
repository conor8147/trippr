package com.con19.tripplanner.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.con19.tripplanner.db.AppDatabase
import com.con19.tripplanner.db.entities.Trip
import com.con19.tripplanner.model.TripService
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class TripViewModel(application: Application) : AndroidViewModel(application) {
    private val service: TripService
    val allTrips: LiveData<List<Trip>>

    init {
        val TripDao = AppDatabase.getDatabase(application, viewModelScope).tripDao()
        service = TripService(TripDao)
        allTrips = service.allTrips
    }
    //async can return things, like the id after insertion
    fun insertAsync(trip : Trip) : Deferred<Long> = viewModelScope.async(Dispatchers.IO) {
        service.insert(trip)
    }
}