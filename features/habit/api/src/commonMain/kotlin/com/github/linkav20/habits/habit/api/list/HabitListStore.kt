package com.github.linkav20.habits.habit.api.list

import com.arkivanov.mvikotlin.core.store.Store

interface HabitListStore : Store<HabitListIntent, HabitListStore.State, Nothing> {
    sealed class State {
        data class Content(val items: List<HabitListItem> = emptyList()) : State()

        data object Loading : State()

        data object Error : State()
    }

    interface Factory {
        fun create(): HabitListStore
    }
}
