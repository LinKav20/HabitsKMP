package com.github.linkav20.habit.createitem.impl.mvi

import com.arkivanov.mvikotlin.core.store.Reducer
import com.github.linkav20.habits.createitem.api.AddHabitStore

internal class AddHabitReducer : Reducer<AddHabitStore.State, AddHabitMessage> {
    override fun AddHabitStore.State.reduce(msg: AddHabitMessage): AddHabitStore.State {
        return when (msg) {
            is AddHabitMessage.OpenNewCreationForm -> copy(creationForm = msg.creationForm)
            else -> this
        }
    }
}
