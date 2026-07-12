package com.github.linkav20.habits.database.converters

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

class DateConverters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? {
        return dateString?.let { LocalDate.parse(it) }
    }

    @TypeConverter
    fun fromLocalDateList(dates: List<LocalDate>?): String? {
        return dates?.let {
            json.encodeToString(it.map { date -> date.toString() })
        }
    }

    @TypeConverter
    fun toLocalDateList(dateStrings: String?): List<LocalDate>? {
        return dateStrings?.let { date ->
            val stringDates: List<String> = json.decodeFromString(date)
            stringDates.map { LocalDate.parse(it) }
        }
    }
}
