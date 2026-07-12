package com.github.linkav20.habits.database.habits.model.todo

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
sealed class TodoRepeatRule {
    @Serializable
    data class Single(
        val date: LocalDate,
    ) : TodoRepeatRule()

    @Serializable
    data class Weekly(
        val daysOfWeek: Set<DayOfWeek>
    ) : TodoRepeatRule()

    @Serializable
    data class Monthly(
        val daysOfMonth: Set<Int>,
        val repeatLastDay: Boolean = false,
    ) : TodoRepeatRule()

    @Serializable
    data class Yearly(
        val dates: List<MonthDay>,
    ) : TodoRepeatRule()
}
