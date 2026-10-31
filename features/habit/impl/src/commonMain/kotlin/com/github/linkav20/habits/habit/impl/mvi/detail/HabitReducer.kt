package com.github.linkav20.habits.habit.impl.mvi.detail

import com.arkivanov.mvikotlin.core.store.Reducer
import com.github.linkav20.habits.habit.api.detail.HabitStore

internal class HabitReducer : Reducer<HabitStore.State, HabitMessage> {
    override fun HabitStore.State.reduce(msg: HabitMessage): HabitStore.State {
        return when (msg) {
            else -> this
        }
    }
}
