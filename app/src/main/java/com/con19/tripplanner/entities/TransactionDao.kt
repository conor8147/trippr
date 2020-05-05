package com.con19.tripplanner.entities

import androidx.room.*

@Dao
interface TransactionDao {
    @Query ("SELECT * FROM transactions")
    fun getAll(): List<Transaction>

    @Insert
    fun insert(transaction : Transaction): Long

    @Update
    fun update(transaction: Transaction)

    @Delete
    fun delete(transaction: Transaction)
}