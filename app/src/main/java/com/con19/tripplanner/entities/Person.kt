package com.con19.tripplanner.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "people")
class Person(
    var nickname: String,
    @ColumnInfo(name = "phone_number")
    var phoneNumber: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0
}