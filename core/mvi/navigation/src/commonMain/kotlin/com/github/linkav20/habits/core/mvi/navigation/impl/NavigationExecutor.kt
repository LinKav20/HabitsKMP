package com.github.linkav20.habits.core.mvi.navigation.impl

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.channels.SendChannel

abstract class NavigationExecutor<
    in Intent : Any,
    Action : Any,
    State : Any,
    Message : Any,
    Label : Any,
    Navigation : Any,
>(
    protected val navigationChannel: SendChannel<Navigation>,
) : CoroutineExecutor<Intent, Action, State, Message, Label>()
