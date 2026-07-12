package com.github.linkav20.habits.todo.impl.mvi

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.github.linkav20.habits.todo.api.TodoIntent
import com.github.linkav20.habits.todo.api.TodoLabel
import com.github.linkav20.habits.todo.api.TodoStore
import org.koin.core.component.KoinComponent

internal class TodoStoreFactoryImpl(
    private val storeFactory: StoreFactory,
) : TodoStore.Factory,
    KoinComponent {
    override fun create(
        itemId: String,
    ): TodoStore {
        return object :
            TodoStore,
            Store<
                    TodoIntent,
                    TodoStore.State,
                    TodoLabel,
                    > by storeFactory.create(
                name = "TodoStore",
                initialState = TodoStore.State(""),
                bootstrapper = TodoBootstrapper(
                    itemId = itemId,
                ),
                executorFactory = {
                    TodoExecutor()
                },
                reducer = TodoReducer(),
            ) {}
    }
}
