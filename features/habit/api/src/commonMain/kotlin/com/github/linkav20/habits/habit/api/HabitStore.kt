package com.github.linkav20.habits.habit.api

import com.arkivanov.mvikotlin.core.store.Store

interface HabitStore : Store<HabitIntent, HabitStore.State, HabitLabel> {
    data class State(
        val itemId: String,
    )

    interface Factory {
        fun create(itemId: String): HabitStore
    }
}
