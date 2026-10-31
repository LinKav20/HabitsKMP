package com.github.linkav20.habits.habit.impl.mvi.detail

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.github.linkav20.habits.habit.api.detail.HabitIntent
import com.github.linkav20.habits.habit.api.detail.HabitLabel
import com.github.linkav20.habits.habit.api.detail.HabitStore

internal class HabitExecutor() :
    CoroutineExecutor<HabitIntent, HabitAction, HabitStore.State, HabitMessage, HabitLabel>(
    ) {

    override fun executeAction(action: HabitAction) {
        super.executeAction(action)
        when (action) {
            else -> {}
        }
    }

    override fun executeIntent(intent: HabitIntent) {
        super.executeIntent(intent)
        when (intent) {
            else -> {}
        }
    }
}
