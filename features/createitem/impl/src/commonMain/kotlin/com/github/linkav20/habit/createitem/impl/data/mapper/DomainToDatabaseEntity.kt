package com.github.linkav20.habit.createitem.impl.data.mapper

import com.github.linkav20.habits.core.utils.randomUUID
import com.github.linkav20.habits.createitem.api.model.AddHabitForm
import com.github.linkav20.habits.createitem.api.model.AddTodoForm
import com.github.linkav20.habits.database.habits.model.habit.HabitEntity
import com.github.linkav20.habits.database.habits.model.todo.MonthDay
import com.github.linkav20.habits.database.habits.model.todo.TodoEntity
import com.github.linkav20.habits.database.habits.model.todo.TodoRepeatRule
import kotlinx.datetime.LocalDate

fun AddHabitForm.toTodoDatabase(creationDay: LocalDate) = HabitEntity(
    uid = randomUUID(),
    title = title,
    endDate = deadline,
    daysForHabit = repeatDays,
    emoji = emoji,
    isActive = true,
    startDate = creationDay,
)

fun AddTodoForm.toTodoDatabase(creationDay: LocalDate): TodoEntity {
    val repeatRule = when (val period = repeatPeriod) {
        is AddTodoForm.RepeatPeriod.Single -> TodoRepeatRule.Single(period.startDate)
        is AddTodoForm.RepeatPeriod.Weekly ->
            TodoRepeatRule.Weekly(period.days)

        is AddTodoForm.RepeatPeriod.Monthly -> {
            val days = period.days?.let {
                if (period.repeatOnFirstDay) it.plus(1) else it
            } ?: if (period.repeatOnFirstDay) setOf(1) else emptySet()
            TodoRepeatRule.Monthly(
                daysOfMonth = days,
                repeatLastDay = period.repeatOnLastDay,
            )
        }

        is AddTodoForm.RepeatPeriod.Yearly ->
            TodoRepeatRule.Yearly(
                period.dates.map {
                    MonthDay(
                        month = it.month,
                        day = it.day,
                    )
                },
            )
    }


    return TodoEntity(
        uid = randomUUID(),
        title = title,
        deadline = deadline,
        createdDate = creationDay,
        repeatRule = repeatRule,
    )
}
