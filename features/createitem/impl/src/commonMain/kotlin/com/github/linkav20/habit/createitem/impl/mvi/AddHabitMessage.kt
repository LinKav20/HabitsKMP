package com.github.linkav20.habit.createitem.impl.mvi

import com.github.linkav20.habits.createitem.api.model.CreationForm

internal sealed interface AddHabitMessage {
    data class OpenNewCreationForm(val creationForm: CreationForm) : AddHabitMessage
}
