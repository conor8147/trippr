package com.con19.tripplanner.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "transactions")
class Transaction(
    @ColumnInfo(name = "name")
    var transactionName: String,
    @ColumnInfo(name = "creation_date")
    var creationDate: Date,
    var cost: Int,
    var image: String,
    var members: MutableList<Person>,
    @ColumnInfo(name = "split_status")
    var splitStatus: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0
}