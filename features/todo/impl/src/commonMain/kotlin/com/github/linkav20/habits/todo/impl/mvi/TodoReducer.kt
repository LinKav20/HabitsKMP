package com.github.linkav20.habits.todo.impl.mvi

import com.arkivanov.mvikotlin.core.store.Reducer
import com.github.linkav20.habits.todo.api.TodoStore

internal class TodoReducer : Reducer<TodoStore.State, TodoMessage> {
    override fun TodoStore.State.reduce(msg: TodoMessage): TodoStore.State {
        return when (msg) {
            else -> this
        }
    }
}
