package com.github.linkav20.habits.core.mvi.navigation.api

import com.arkivanov.mvikotlin.core.store.Bootstrapper
import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Reducer
import kotlinx.coroutines.channels.SendChannel
import kotlin.js.JsName

interface NavStoreFactory {

    @JsName("create")
    fun <Intent : Any, Action : Any, Message : Any, State : Any, Label : Any, Navigation : Any> create(
        name: String? = null,
        autoInit: Boolean = true,
        initialState: State,
        bootstrapper: Bootstrapper<Action>? = null,
        executorFactory: (SendChannel<Navigation>) -> Executor<Intent, Action, State, Message, Label>,
        @Suppress("UNCHECKED_CAST")
        reducer: Reducer<State, Message> = bypassReducer as Reducer<State, Any>,
    ): NavStore<Intent, State, Label, Navigation>

    companion object {
        val bypassReducer: Reducer<Any, Any> = Reducer { this }
    }
}
