package com.con19.tripplanner.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "trips")
class Trip(
    @ColumnInfo(name = "trip_name")
    var tripName: String,
    @ColumnInfo(name = "start_date")
    var startDate: Date,
    @ColumnInfo(name = "end_date")
    var endDate: Date,
    var members: MutableList<Person>,
    var transactions: MutableList<Transaction>,
    @ColumnInfo(name = "cover_photo")
    var coverPhoto: String
    ) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    var id: Long = 0

}