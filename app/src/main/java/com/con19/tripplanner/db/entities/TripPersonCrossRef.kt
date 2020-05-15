package com.con19.tripplanner.db.entities

import androidx.room.Entity

@Entity(primaryKeys=["tripId", "personId"])
data class TripPersonCrossRef(
    val tripId: Long,
    val personId: Long
)