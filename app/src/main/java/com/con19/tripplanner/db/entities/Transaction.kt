package com.con19.tripplanner.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "transactions"
)
class Transaction(
    @ColumnInfo(name = "name")
    var transactionName: String,

    @ColumnInfo(name = "creation_date")
    var creationDate: Date,

    @ColumnInfo(name = "id_trip")
    var tripId: Long,

    var memberIds: List<Long>,

    @ColumnInfo(name = "paid")
    var paid: Boolean,

    var cost: Int,
    var image: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0
}