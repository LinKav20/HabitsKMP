package com.gitub.linkav20.habits.main.api.main

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

@Stable
interface MainComponent {
    val model: StateFlow<MainStore.State>

    fun handleEvent(event: MainEvent)

    interface Factory {
        fun create(
            componentContext: ComponentContext,
            onProfileClick: () -> Unit,
            onAddItemClick: () -> Unit,
            onViewTodoClick: (String) -> Unit,
            onViewHabitClick: (String) -> Unit,
        ): MainComponent
    }
}
