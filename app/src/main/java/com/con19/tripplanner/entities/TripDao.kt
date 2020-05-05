package com.con19.tripplanner.entities

import androidx.room.*

@Dao
interface TripDao {

    @Query("SELECT * FROM trips")
    fun getAll(): List<Trip>

    /**
     * Returns id of inserted item
     */
    @Insert
    fun insert(trip: Trip): Long

    @Update
    fun update(trip: Trip)

    /**
     * Returns number of rows deleted
     */
    @Delete
    fun delete(trip: Trip): Int

}