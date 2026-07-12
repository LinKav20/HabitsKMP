package com.gitub.linkav20.habits.main.api.domain.model.main

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

data class HabitItem(
    override val id: String,
    override val title: String,
    override val endDate: LocalDate?,
    val emoji: String = "",
    val startDate: LocalDate,
    val daysForHabit: Set<DayOfWeek> = DayOfWeek.entries.toSet(),
    val isActive: Boolean,
) : CheckingItem {

    fun dateInHabitRange(date: LocalDate): Boolean {
        if (!isActive) return false
        return date >= startDate && (endDate == null || date <= endDate)
    }
}
