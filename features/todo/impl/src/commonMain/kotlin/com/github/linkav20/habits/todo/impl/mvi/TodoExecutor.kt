package com.github.linkav20.habits.todo.impl.mvi

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.github.linkav20.habits.todo.api.TodoIntent
import com.github.linkav20.habits.todo.api.TodoLabel
import com.github.linkav20.habits.todo.api.TodoStore

internal class TodoExecutor() :
    CoroutineExecutor<TodoIntent, TodoAction, TodoStore.State, TodoMessage, TodoLabel>(
    ) {

    override fun executeAction(action: TodoAction) {
        super.executeAction(action)
        when (action) {
            else -> {}
        }
    }

    override fun executeIntent(intent: TodoIntent) {
        super.executeIntent(intent)
        when (intent) {
            else -> {}
        }
    }
}
