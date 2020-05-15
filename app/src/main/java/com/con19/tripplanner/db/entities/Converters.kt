package com.con19.tripplanner.db.entities

import androidx.room.TypeConverter
import java.util.*

/**
 * Class for converting complex data types to and from the allowed SQLite data types
 */
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun idListToString(idList: List<Long>): String {
        return idList.joinToString(",")
    }

    @TypeConverter
    fun stringToIdList(ids: String): List<Long> {
        if (ids == "") {
            return listOf()
        }
        return ids.split(",").map { it.toLong() }
    }
}