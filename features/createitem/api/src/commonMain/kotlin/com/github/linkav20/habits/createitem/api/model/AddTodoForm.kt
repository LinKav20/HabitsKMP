package com.github.linkav20.habits.createitem.api.model

import com.github.linkav20.habits.core.utils.Time
import kotlinx.datetime.DateTimeUnit.Companion.DAY
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

data class AddTodoForm(
    val title: String = "",
    val deadline: LocalDate = Time.now().plus(7, DAY),
    val repeatPeriod: RepeatPeriod = RepeatPeriod.Single()
) : CreationForm {

    val isRegular = repeatPeriod !is RepeatPeriod.Single

    sealed interface RepeatPeriod {
        fun isEmpty(): Boolean

        data class Single(
            val startDate: LocalDate = Time.now(),
        ) : RepeatPeriod {
            override fun isEmpty() = false
        }

        data class Weekly(
            val days: Set<DayOfWeek> = setOf(
                DayOfWeek.MONDAY,
            ),
        ) : RepeatPeriod {
            override fun isEmpty(): Boolean {
                return days.isEmpty()
            }
        }

        data class Monthly(
            val repeatOnFirstDay: Boolean = false,
            val repeatOnLastDay: Boolean = false,
            val days: Set<Int>? = null,
        ) : RepeatPeriod {
            fun daysAsLocalDate(): Set<LocalDate> {
                val year = 2020
                val monthNumber = 12
                return days?.map { LocalDate(year, monthNumber, it) }?.toSet() ?: emptySet()
            }

            override fun isEmpty(): Boolean {
                return repeatOnFirstDay.not() && repeatOnLastDay.not() && days.isNullOrEmpty()
            }
        }

        data class Yearly(
            val dates: Set<MonthDay> = emptySet(),
        ) : RepeatPeriod {
            fun datesAsLocalDate(): Set<LocalDate> {
                val year = 2020
                return dates.map { LocalDate(year, it.month, it.day) }.toSet()
            }

            override fun isEmpty(): Boolean {
                return dates.isEmpty()
            }

            data class MonthDay(
                val month: Int,
                val day: Int,
            )
        }
    }
}
