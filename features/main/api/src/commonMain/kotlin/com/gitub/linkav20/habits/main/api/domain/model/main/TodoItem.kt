package com.gitub.linkav20.habits.main.api.domain.model.main

import com.github.linkav20.habits.core.utils.Time
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number

data class TodoItem(
    override val id: String,
    override val title: String,
    override val endDate: LocalDate?,
    val createdDate: LocalDate,
    val repeatPeriod: RepeatPeriod,
) : CheckingItem {

    fun isPlanned(date: LocalDate): Boolean {
        return when (val period = repeatPeriod) {
            is RepeatPeriod.Single -> date == period.startDate

            is RepeatPeriod.Weekly ->
                date.dayOfWeek in period.days

            is RepeatPeriod.Monthly ->
                date.day in period.days

            is RepeatPeriod.Yearly ->
                RepeatPeriod.Yearly.toDayMonth(date) in period.dates
        }
    }

    sealed interface RepeatPeriod {
        data class Single(
            val startDate: LocalDate = Time.now(),
        ) : RepeatPeriod

        data class Weekly(
            val days: Set<DayOfWeek> = setOf(
                DayOfWeek.MONDAY,
            ),
        ) : RepeatPeriod

        data class Monthly(
            val repeatOnFirstDay: Boolean = false,
            val repeatOnLastDay: Boolean = false,
            val days: Set<Int> = emptySet(),
        ) : RepeatPeriod

        data class Yearly(
            val dates: Set<MonthDay> = emptySet(),
        ) : RepeatPeriod {

            companion object {
                fun toDayMonth(date: LocalDate) = MonthDay(
                    month = date.month.number,
                    day = date.day,
                )
            }

            data class MonthDay(
                val month: Int,
                val day: Int,
            )
        }
    }

    fun daysUntilDeadline(): Int {
        return if (repeatPeriod is RepeatPeriod.Single && endDate != null) {
            Time.daysBetween(repeatPeriod.startDate, endDate)
        } else {
            Int.MAX_VALUE
        }
    }

    fun getAccent(): String {
        return if (daysUntilDeadline() < CRITICAL_DAYS_UNTIL_DEADLINE) {
            "\u2757"
        } else {
            ""
        }
    }

    fun shouldReschedule(): Boolean? {
        return if (repeatPeriod is RepeatPeriod.Single && endDate != null) {
            Time.now() < endDate && repeatPeriod.startDate < Time.now()
        } else {
            null
        }
    }

    fun shouldFail(): Boolean? {
        return if (repeatPeriod is RepeatPeriod.Single && endDate != null) {
            Time.now() >= endDate
        } else {
            null
        }
    }

    companion object {
        const val CRITICAL_DAYS_UNTIL_DEADLINE = 3
    }
}

