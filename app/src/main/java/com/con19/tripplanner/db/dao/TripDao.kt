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
     * Inserts hands_split row into the cross reference table only. Use to link hands_split trip with hands_split person
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tripPersonCrossRef: TripPersonCrossRef): Long

    /**
     * Returns id of inserted item
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(trip: Trip): Long

    @Update
    suspend fun update(trip: Trip)

    /**
     * Returns number of rows deleted
     */
    @Delete
    suspend fun delete(trip: Trip): Int

    /**
     * Removes row from reference table only, use to decouple trip and person
     */
    @Delete
    suspend fun delete(tripPersonCrossRef: TripPersonCrossRef)


    @Query("DELETE FROM trips")
    suspend fun deleteAll()
}

