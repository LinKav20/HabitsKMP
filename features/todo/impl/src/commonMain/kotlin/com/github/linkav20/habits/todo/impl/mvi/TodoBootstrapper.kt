package com.github.linkav20.habits.todo.impl.mvi

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper

internal class TodoBootstrapper(
    private val itemId: String,
) : CoroutineBootstrapper<TodoAction>() {

    override fun invoke() {
    }
}
