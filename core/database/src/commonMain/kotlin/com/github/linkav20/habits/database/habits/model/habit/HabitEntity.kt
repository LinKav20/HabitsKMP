package com.github.linkav20.habits.database.habits.model.habit

import androidx.room.Entity
import com.github.linkav20.habits.database.habits.model.CompletableItem
import com.github.linkav20.habits.database.habits.model.Tables
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

@Entity(
	tableName = Tables.HABITS,
	primaryKeys = ["uid"],
)
data class HabitEntity(
	override val uid: String,
	val title: String,
	val startDate: LocalDate,
	val endDate: LocalDate?,
	val emoji: String,
	val daysForHabit: Set<DayOfWeek>,
	val isActive: Boolean = true,
) : CompletableItem
