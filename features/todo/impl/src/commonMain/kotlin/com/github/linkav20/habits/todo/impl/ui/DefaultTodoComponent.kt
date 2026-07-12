package com.github.linkav20.habits.todo.impl.ui

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.github.linkav20.habits.core.uikit.components.snackbar.SnackBarNotifier
import com.github.linkav20.habits.todo.api.TodoComponent
import com.github.linkav20.habits.todo.api.TodoEvent
import com.github.linkav20.habits.todo.api.TodoStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

@OptIn(ExperimentalCoroutinesApi::class)
internal class DefaultTodoComponent private constructor(
    componentContext: ComponentContext,
    private val itemId: String,
    private val storeFactory: TodoStore.Factory,
    private val snackBarNotifier: SnackBarNotifier,
    private val onBackClick: () -> Unit,
) : TodoComponent,
    ComponentContext by componentContext {
    private val store = instanceKeeper.getStore { storeFactory.create(itemId) }
    override val model: StateFlow<TodoStore.State> = store.stateFlow
    private val scope = coroutineScope()

    override fun handleEvent(event: TodoEvent) {
        when (event) {
            else -> {}
        }
    }

    internal class Factory(
        private val storeFactory: TodoStore.Factory,
    ) : TodoComponent.Factory,
        KoinComponent {
        override fun create(
            componentContext: ComponentContext,
            itemId: String,
            onBackClick: () -> Unit,
        ): TodoComponent {
            return DefaultTodoComponent(
                componentContext = componentContext,
                storeFactory = storeFactory,
                itemId = itemId,
                onBackClick = onBackClick,
                snackBarNotifier = get(),
            )
        }
    }
}
