package com.gitub.linkav20.habits.main.impl.data.mapper

import com.github.linkav20.habits.database.habits.model.todo.MonthDay
import com.github.linkav20.habits.database.habits.model.todo.TodoEntity
import com.github.linkav20.habits.database.habits.model.todo.TodoRepeatRule
import com.gitub.linkav20.habits.main.api.domain.model.main.TodoItem

fun TodoItem.toTodoDatabase(): TodoEntity {
    val repeatRule = when (val period = repeatPeriod) {
        is TodoItem.RepeatPeriod.Single -> TodoRepeatRule.Single(period.startDate)
        is TodoItem.RepeatPeriod.Weekly ->
            TodoRepeatRule.Weekly(period.days)

        is TodoItem.RepeatPeriod.Monthly -> {
            val days = period.days?.let {
                if (period.repeatOnFirstDay) it.plus(1) else it
            } ?: if (period.repeatOnFirstDay) setOf(1) else emptySet()
            TodoRepeatRule.Monthly(
                daysOfMonth = days,
                repeatLastDay = period.repeatOnLastDay,
            )
        }

        is TodoItem.RepeatPeriod.Yearly ->
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
        uid = id,
        title = title,
        deadline = endDate,
        createdDate = createdDate,
        repeatRule = repeatRule,
    )
}
