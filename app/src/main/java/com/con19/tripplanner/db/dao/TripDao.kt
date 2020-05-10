package com.con19.tripplanner.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.con19.tripplanner.db.entities.Trip

@Dao
interface TripDao {

    // LiveData class: https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/#5
    @Query("SELECT * FROM trips")
    fun getAll(): LiveData<List<Trip>>

    @Query("SELECT * FROM trips WHERE id = :id")
    fun getTripById(id: Int): Trip

    /**
     * Returns id of inserted item
     */
    @Insert
    suspend fun insert(trip: Trip): Long

    @Update
    suspend fun update(trip: Trip)

    /**
     * Returns number of rows deleted
     */
    @Delete
    suspend fun delete(trip: Trip): Int

}