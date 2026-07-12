package com.github.linkav20.habits.createitem.api.model

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

data class AddHabitForm(
    val title: String = "",
    val emoji: String = "\uD83E\uDD70",
    val repeatDays: Set<DayOfWeek> = setOf(
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
    ),
    val deadline: LocalDate? = null,
) : CreationForm
