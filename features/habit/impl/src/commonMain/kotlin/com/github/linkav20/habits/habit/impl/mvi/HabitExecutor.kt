package com.github.linkav20.habits.habit.impl.mvi

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.github.linkav20.habits.habit.api.HabitIntent
import com.github.linkav20.habits.habit.api.HabitLabel
import com.github.linkav20.habits.habit.api.HabitStore

internal class HabitExecutor() :
    CoroutineExecutor<HabitIntent, com.github.linkav20.habits.habit.impl.mvi.HabitAction, HabitStore.State, com.github.linkav20.habits.habit.impl.mvi.HabitMessage, HabitLabel>(
    ) {

    override fun executeAction(action: com.github.linkav20.habits.habit.impl.mvi.HabitAction) {
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
