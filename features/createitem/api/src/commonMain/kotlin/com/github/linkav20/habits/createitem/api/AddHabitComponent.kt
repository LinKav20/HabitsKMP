package com.github.linkav20.habits.createitem.api

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

@Stable
interface AddHabitComponent {
    val model: StateFlow<AddHabitStore.State>

    fun handleEvent(event: AddHabitEvent)

    interface Factory {
        fun create(
            componentContext: ComponentContext,
            onBackClick: () -> Unit,
        ): AddHabitComponent
    }
}
