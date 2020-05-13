package com.con19.tripplanner.model

import androidx.lifecycle.LiveData
import com.con19.tripplanner.db.dao.TripDao
import com.con19.tripplanner.db.entities.Trip

class TripService(private val tripDao: TripDao) {
    // by lazy just gets doesn't make it until it is used
    val allTrips: LiveData<List<Trip>> by lazy { tripDao.getAll() }

    fun getTripById(id: Int) = tripDao.getTripById(id)

    suspend fun insert(trip: Trip): Long {
        return tripDao.insert(trip)
    }

    companion object {
        private var INSTANCE: TripService?= null

        fun getInstance(tripDao: TripDao): TripService {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this){
                val instance = TripService(tripDao)
                INSTANCE = instance
                return instance
            }
        }
    }
}