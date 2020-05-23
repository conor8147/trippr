package com.con19.tripplanner.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "people")
class Person(
    var nickname: String,
    var phoneNumber: String
) {
    @PrimaryKey(autoGenerate = true)
    var personId: Long = 0

    override fun equals(other: Any?): Boolean {
        return other != null && other is Person && other.personId == this.personId
    }
}