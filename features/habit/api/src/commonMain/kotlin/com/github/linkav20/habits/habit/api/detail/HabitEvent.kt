package com.github.linkav20.habits.habit.api.detail

sealed interface HabitEvent {
    data object BackClicked : HabitEvent
}
