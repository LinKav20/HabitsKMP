package com.github.linkav20.habits.core.mvi.navigation.impl

import com.arkivanov.mvikotlin.core.store.Bootstrapper
import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import com.github.linkav20.habits.core.mvi.navigation.api.NavStore
import com.github.linkav20.habits.core.mvi.navigation.api.NavStoreFactory
import kotlin.js.JsName

class NavStoreFactoryImpl(
    private val original: StoreFactory,
) : NavStoreFactory {

    @JsName("create")
    override fun <Intent : Any, Action : Any, Message : Any, State : Any, Label : Any, Navigation : Any> create(
        name: String?,
        autoInit: Boolean,
        initialState: State,
        bootstrapper: Bootstrapper<Action>?,
        executorFactory: (SendChannel<Navigation>) -> Executor<Intent, Action, State, Message, Label>,
        reducer: Reducer<State, Message>,
    ): NavStore<Intent, State, Label, Navigation> {
        val channel = Channel<Navigation> {}

        return NavStoreImpl(
            receiveChannel = channel,
            originalStore = original.create(
                name = name,
                autoInit = autoInit,
                initialState = initialState,
                bootstrapper = bootstrapper,
                executorFactory = {
                    executorFactory(channel)
                },
                reducer = reducer,
            ),
        )
    }
}
