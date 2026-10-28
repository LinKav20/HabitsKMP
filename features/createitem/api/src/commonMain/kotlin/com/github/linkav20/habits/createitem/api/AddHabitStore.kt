package com.github.linkav20.habits.createitem.api

import com.arkivanov.mvikotlin.core.store.Store
import com.github.linkav20.habits.createitem.api.model.AddHabitForm
import com.github.linkav20.habits.createitem.api.model.AddTodoForm
import com.github.linkav20.habits.createitem.api.model.CreationForm

interface AddHabitStore : Store<AddHabitIntent, AddHabitStore.State, AddHabitLabel> {
    data class State(
        val creationForm: CreationForm = AddHabitForm(),
    ) {
        fun canCreate(): Boolean {
            return when (creationForm) {
                is AddHabitForm -> creationForm.title.isNotEmpty()
                is AddTodoForm -> creationForm.title.isNotEmpty()
            }
        }
    }

    interface Factory {
        fun create(): AddHabitStore
    }
}
