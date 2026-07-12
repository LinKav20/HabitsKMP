package com.github.linkav20.habits.database.converters

import androidx.room.TypeConverter
import kotlinx.datetime.DayOfWeek

class DayOfWeekConverters {

    @TypeConverter
    fun fromDayOfWeekSet(days: Set<DayOfWeek>?): String {
        if (days.isNullOrEmpty()) return ""
        return days.joinToString(",") { it.ordinal.toString() }
    }

    @TypeConverter
    fun toDayOfWeekSet(data: String?): Set<DayOfWeek> {
        if (data.isNullOrEmpty()) return emptySet()
        return data.split(",")
            .mapNotNull { it.toIntOrNull() }
            .mapNotNull { ordinal ->
                DayOfWeek.entries.getOrNull(ordinal)
            }
            .toSet()
    }
}
