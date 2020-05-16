package com.con19.tripplanner.db.entities

import androidx.room.Entity

/**
 * Used to link the Transaction and Person entities together in the database.
 */
@Entity(primaryKeys=["transactionId", "personId"])
data class TransactionPersonCrossRef(
    val transactionId: Long,
    val personId: Long
)