package com.con19.tripplanner.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.con19.tripplanner.db.entities.Person

@Dao
interface PersonDao {

    // LiveData class: https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/#5
    @Query ("SELECT * FROM people")
    fun getAll(): LiveData<List<Person>>

    @Insert
    suspend fun insert(person: Person): Long

    @Update
    suspend fun update(person: Person)

    @Delete
    suspend fun delete(person: Person)

    @Query("DELETE FROM people")
    suspend fun deleteAll()
}