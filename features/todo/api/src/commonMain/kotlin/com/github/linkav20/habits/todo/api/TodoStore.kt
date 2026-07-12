package com.github.linkav20.habits.todo.api

import com.arkivanov.mvikotlin.core.store.Store

interface TodoStore : Store<TodoIntent, TodoStore.State, TodoLabel> {
    data class State(
        val itemId: String,
    )

    interface Factory {
        fun create(itemId: String): TodoStore
    }
}
