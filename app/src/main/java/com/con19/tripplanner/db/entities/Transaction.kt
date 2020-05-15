package com.con19.tripplanner.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "transactions"
)
class Transaction(
    var name: String,
    var creationDate: Date,
    var tripId: Long,
    var memberIds: List<Long>,
    var paid: Boolean,
    var cost: Int,
    var image: String
) {
    @PrimaryKey(autoGenerate = true)
    var transactionId: Long = 0
}