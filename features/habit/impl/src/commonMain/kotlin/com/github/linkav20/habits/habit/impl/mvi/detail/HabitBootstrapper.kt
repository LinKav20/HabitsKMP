package com.github.linkav20.habits.habit.impl.mvi.detail

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper

internal class HabitBootstrapper(
    private val itemId: String,
) : CoroutineBootstrapper<HabitAction>() {

    override fun invoke() {
    }
}
