package com.con19.tripplanner.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.con19.tripplanner.db.entities.Transaction
import com.con19.tripplanner.db.entities.TransactionPersonCrossRef
import com.con19.tripplanner.db.entities.TransactionWithPeople

@Dao
interface TransactionDao {

    // LiveData class: https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/#5
    @androidx.room.Transaction
    @Query ("SELECT * FROM transactions")
    fun getAll(): LiveData<List<TransactionWithPeople>>

    @androidx.room.Transaction
    @Query("SELECT * FROM transactions WHERE tripId = :tripId")
    fun getTransactionsForTripWithId(tripId: Long): LiveData<List<TransactionWithPeople>>

    @Query("UPDATE transactions SET paid = :newPaidStatus WHERE transactionId = :transactionId")
    fun updatePaidStatusForTransaction(transactionId: Long, newPaidStatus: Boolean)

    @Insert
    suspend fun insert(transaction : Transaction): Long

    /**
     * Inserts a row into the cross reference table only. Use to link a transaction with a person
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(transactionPersonCrossRef: TransactionPersonCrossRef): Long

    @Delete
    suspend fun delete(transaction: Transaction)

    /**
     * Removes row from reference table only, use to decouple trip and person
     */
    @Delete
    suspend fun delete(transactionPersonCrossRef: TransactionPersonCrossRef)
}