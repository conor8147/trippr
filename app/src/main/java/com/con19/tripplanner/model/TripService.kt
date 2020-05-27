package com.con19.tripplanner.model

import androidx.lifecycle.LiveData
import com.con19.tripplanner.db.dao.TripDao
import com.con19.tripplanner.db.entities.*

class TripService private constructor(private val tripDao: TripDao) {
    // by lazy just gets doesn't make it until it is used
    val allTrips: LiveData<List<TripWithPeople>> = tripDao.getAll()

    //fun getTripById(id: Int) = tripDao.getTripById(id)

    suspend fun insert(trip: Trip, peopleOnTrip: List<Person>): Long {
        val tripId = tripDao.insert(trip)
        peopleOnTrip.forEach {
            tripDao.insert(
                TripPersonCrossRef(tripId, it.personId)
            )
        }
        return tripId
    }

    suspend fun addPersonToTrip(tripId: Long, personId: Long) {
        tripDao.insert(
            TripPersonCrossRef(
                tripId,
                personId
            )
        )
    }

    suspend fun removePersonFromTrip(tripId: Long, personId: Long) {
        tripDao.delete(
            TripPersonCrossRef(
                tripId,
                personId
            )
        )
    }

    suspend fun update(trip: Trip, people: MutableList<Person>) {
        tripDao.update(trip)
        tripDao.deletePeopleFromCrossRefForTripWithId(trip.tripId)
        people.forEach {
            tripDao.insert(
                TripPersonCrossRef(
                    trip.tripId,
                    it.personId
                )
            )
        }
    }

    companion object {
        private var INSTANCE: TripService? = null

        fun getInstance(tripDao: TripDao): TripService {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = TripService(tripDao)
                INSTANCE = instance
                return instance
            }
        }
    }
}