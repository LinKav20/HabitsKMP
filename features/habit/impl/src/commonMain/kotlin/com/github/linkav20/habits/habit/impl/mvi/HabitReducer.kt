package com.github.linkav20.habits.habit.impl.mvi

import com.arkivanov.mvikotlin.core.store.Reducer
import com.github.linkav20.habits.habit.api.HabitStore

internal class HabitReducer : Reducer<HabitStore.State, com.github.linkav20.habits.habit.impl.mvi.HabitMessage> {
    override fun HabitStore.State.reduce(msg: com.github.linkav20.habits.habit.impl.mvi.HabitMessage): HabitStore.State {
        return when (msg) {
            else -> this
        }
    }
}
