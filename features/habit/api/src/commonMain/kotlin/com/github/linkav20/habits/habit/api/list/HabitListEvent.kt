package com.github.linkav20.habits.habit.api.list

sealed interface HabitListEvent {
    data object OnBack : HabitListEvent

    data object OnReload : HabitListEvent

    data class OnHabitChanged(val id: String) : HabitListEvent
}
