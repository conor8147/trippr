package com.con19.tripplanner.db.entities

import androidx.room.Entity

/**
 * Used to link the Trip and Person entities together in the database.
 */
@Entity(primaryKeys=["tripId", "personId"])
data class TripPersonCrossRef(
    val tripId: Long,
    val personId: Long
)