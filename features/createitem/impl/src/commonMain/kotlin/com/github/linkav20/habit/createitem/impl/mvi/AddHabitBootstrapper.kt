package com.github.linkav20.habit.createitem.impl.mvi

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper

internal class AddHabitBootstrapper : CoroutineBootstrapper<AddHabitAction>() {

    override fun invoke() {
        dispatch(AddHabitAction.Init)
    }
}
