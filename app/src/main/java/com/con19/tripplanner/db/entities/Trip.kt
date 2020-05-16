package com.con19.tripplanner.db.entities

import androidx.room.*
import java.util.*

@Entity(tableName = "trips")
class Trip(
    var tripName: String,
    var startDate: Date,
    var endDate: Date,
    var coverPhoto: String?
    ) {
    @PrimaryKey(autoGenerate = true)
    var tripId: Long = 0
}

data class TripWithPeople(
    @Embedded val trip: Trip,
    @Relation(
        parentColumn = "tripId",
        entityColumn = "personId",
        associateBy = Junction(TripPersonCrossRef::class)
    )
    val people: List<Person>
)