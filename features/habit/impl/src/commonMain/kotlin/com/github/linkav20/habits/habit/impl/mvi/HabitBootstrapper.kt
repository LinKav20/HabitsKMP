package com.github.linkav20.habits.habit.impl.mvi

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper

internal class HabitBootstrapper(
    private val itemId: String,
) : CoroutineBootstrapper<com.github.linkav20.habits.habit.impl.mvi.HabitAction>() {

    override fun invoke() {
    }
}
