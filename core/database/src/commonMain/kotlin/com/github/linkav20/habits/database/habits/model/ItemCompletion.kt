package com.github.linkav20.habits.database.habits.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.github.linkav20.habits.database.habits.model.habit.HabitEntity
import kotlinx.datetime.LocalDate

@Entity(
    tableName = Tables.ITEM_COMPLETION,
    primaryKeys = ["itemId", "date"],
    indices = [Index(value = ["itemId"])],
)
data class ItemCompletion(
    val itemId: String,
    val date: LocalDate,
    val isCompleted: Boolean,
)
