package com.github.linkav20.habits.habit.api.list

sealed interface HabitListIntent {
    data object Retry : HabitListIntent
}
