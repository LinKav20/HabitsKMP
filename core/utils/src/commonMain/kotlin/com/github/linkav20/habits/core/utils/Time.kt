package com.github.linkav20.habits.core.utils

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DateTimeUnit.Companion.DAY
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

public object Time {
    public fun now(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDate =
        Clock.System.now()
            .toLocalDateTime(timeZone)
            .date

    public fun tomorrow(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDate =
        now(timeZone).plus(1, DAY)

    public fun getWeekWithDay(day: LocalDate = now()): List<LocalDate> {
        val monday = when (day.dayOfWeek) {
            DayOfWeek.MONDAY -> day
            DayOfWeek.TUESDAY -> day.minus(1, DAY)
            DayOfWeek.WEDNESDAY -> day.minus(2, DAY)
            DayOfWeek.THURSDAY -> day.minus(3, DAY)
            DayOfWeek.FRIDAY -> day.minus(4, DAY)
            DayOfWeek.SATURDAY -> day.minus(5, DAY)
            DayOfWeek.SUNDAY -> day.minus(6, DAY)
        }
        return List(7) { monday.plus(it, DAY) }
    }

    public fun getDateRangeInclusive(startDate: LocalDate, endDate: LocalDate): List<LocalDate> {
        val daysBetween = startDate.daysUntil(endDate)
        return List(daysBetween + 1) { index ->
            startDate.plus(DatePeriod(days = index))
        }
    }

    public fun getWeekRange(
        day: LocalDate = now(),
        weeksBefore: Int,
        weeksAfter: Int,
    ): Pair<LocalDate, LocalDate> {
        val daysSinceMonday = (day.dayOfWeek.ordinal - DayOfWeek.MONDAY.ordinal + 7) % 7
        val currentMonday = day.minus(daysSinceMonday.toLong(), DAY)
        val firstMonday = currentMonday.minus(weeksBefore * 7L, DAY)
        val lastSunday = currentMonday.plus(weeksAfter * 7L - 1, DAY)
        return firstMonday to lastSunday
    }

    public fun getIndexOfTheFirstDayOfWeek(
        day: LocalDate,
        interval: List<LocalDate>,
    ): Int {
        val daysSinceMonday = (day.dayOfWeek.ordinal - DayOfWeek.MONDAY.ordinal + 7) % 7
        val currentMonday = day.minus(daysSinceMonday.toLong(), DAY)
        return interval.indexOfFirst { it == currentMonday }
    }

    public fun getWeeksWithSurrounding(
        centerDay: LocalDate = now(),
        weeksBefore: Int = 2,
        weeksAfter: Int = 2,
    ): List<List<LocalDate>> {
        val centerWeek = getWeekWithDay(centerDay)
        val centerMonday = centerWeek.first()

        return buildList {
            for (i in weeksBefore downTo 1) {
                val monday = centerMonday.minus(i * 7, DAY)
                add(List(7) { monday.plus(it, DAY) })
            }
            add(centerWeek)
            for (i in 1..weeksAfter) {
                val monday = centerMonday.plus(i * 7, DAY)
                add(List(7) { monday.plus(it, DAY) })
            }
        }
    }

    public fun daysBetween(startDate: LocalDate, endDate: LocalDate): Int =
        startDate.daysUntil(endDate)
}
