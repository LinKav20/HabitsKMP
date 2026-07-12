package com.github.linkav20.habits.todo.api

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

@Stable
interface TodoComponent {
    val model: StateFlow<TodoStore.State>

    fun handleEvent(event: TodoEvent)

    interface Factory {
        fun create(
            componentContext: ComponentContext,
            itemId: String,
            onBackClick: () -> Unit,
        ): TodoComponent
    }
}
