package com.con19.tripplanner.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.con19.tripplanner.db.entities.Trip
import com.con19.tripplanner.db.entities.TripPersonCrossRef
import com.con19.tripplanner.db.entities.TripWithPeople

@Dao
interface TripDao {

    // LiveData class: https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/#5
    @Transaction
    @Query("SELECT * FROM trips")
    fun getAll(): LiveData<List<TripWithPeople>>

    /**
     * Returns id of inserted item
     */
    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tripPersonCrossRef: TripPersonCrossRef): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(trip: Trip): Long

    @Update
    suspend fun update(trip: Trip)

    /**
     * Returns number of rows deleted
     */
    @Delete
    suspend fun delete(trip: Trip): Int

    @Delete
    suspend fun delete(tripPersonCrossRef: TripPersonCrossRef)


    @Query("DELETE FROM trips")
    suspend fun deleteAll()
}

