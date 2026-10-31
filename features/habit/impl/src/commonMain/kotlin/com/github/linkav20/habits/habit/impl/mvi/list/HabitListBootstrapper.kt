package com.github.linkav20.habits.habit.impl.mvi.list

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper

internal class HabitListBootstrapper : CoroutineBootstrapper<HabitListAction>() {
    override fun invoke() {
        dispatch(HabitListAction.Init)
    }
}
