package com.gitub.linkav20.habits.main.impl.data.mapper

import com.github.linkav20.habits.database.habits.model.habit.HabitEntity
import com.github.linkav20.habits.database.habits.model.todo.TodoEntity
import com.github.linkav20.habits.database.habits.model.todo.TodoRepeatRule
import com.github.linkav20.habits.database.mood.model.MoodEntity
import com.gitub.linkav20.habits.main.api.domain.model.main.HabitItem
import com.gitub.linkav20.habits.main.api.domain.model.main.MoodItem
import com.gitub.linkav20.habits.main.api.domain.model.main.TodoItem

fun HabitEntity.toDomain() = HabitItem(
    id = uid,
    title = title,
    startDate = startDate,
    endDate = endDate,
    daysForHabit = daysForHabit,
    emoji = emoji,
    isActive = isActive,
)

fun TodoEntity.toDomain() = TodoItem(
    id = this.uid,
    title = this.title,
    endDate = this.deadline,
    createdDate = this.createdDate,
    repeatPeriod = when (val rule = this.repeatRule) {
        is TodoRepeatRule.Single -> TodoItem.RepeatPeriod.Single(
            startDate = rule.date,
        )

        is TodoRepeatRule.Weekly -> TodoItem.RepeatPeriod.Weekly(
            days = rule.daysOfWeek,
        )

        is TodoRepeatRule.Monthly -> TodoItem.RepeatPeriod.Monthly(
            repeatOnFirstDay = rule.daysOfMonth.contains(1),
            repeatOnLastDay = rule.repeatLastDay,
            days = if (rule.daysOfMonth.isNotEmpty() &&
                !rule.repeatLastDay
            ) {
                rule.daysOfMonth
            } else {
                emptySet()
            },
        )

        is TodoRepeatRule.Yearly -> TodoItem.RepeatPeriod.Yearly(
            dates = rule.dates.map { monthDay ->
                TodoItem.RepeatPeriod.Yearly.MonthDay(
                    month = monthDay.month,
                    day = monthDay.day,
                )
            }.toSet(),
        )
    },
)

fun MoodEntity.toDomain() = MoodItem(
    date = date,
    rate = rate,
)
