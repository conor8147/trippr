package com.con19.tripplanner.entities

import androidx.room.*

@Dao
interface PersonDao {
    @Query ("SELECT * FROM people")
    fun getAll(): List<Person>

    @Insert
    fun insert(person: Person): Long

    @Update
    fun update(person: Person)

    @Delete
    fun delete(person: Person)
}