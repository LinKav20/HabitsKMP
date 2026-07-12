package com.github.linkav20.habits.database.mood.model

import androidx.room.Entity
import kotlinx.datetime.LocalDate

@Entity(
    tableName = Tables.MOOD,
    primaryKeys = ["date"],
)
data class MoodEntity(
    val date: LocalDate,
    val rate: Int,
)
