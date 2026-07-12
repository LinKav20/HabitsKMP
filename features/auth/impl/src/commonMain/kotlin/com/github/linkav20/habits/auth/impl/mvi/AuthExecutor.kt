package com.github.linkav20.habits.auth.impl.mvi

import kotlinx.coroutines.channels.SendChannel
import com.github.linkav20.habits.auth.api.AuthIntent
import com.github.linkav20.habits.auth.api.AuthNav
import com.github.linkav20.habits.auth.api.AuthStore
import com.github.linkav20.habits.core.mvi.navigation.impl.NavigationExecutor

internal class AuthExecutor(channel: SendChannel<AuthNav>) :
    NavigationExecutor<AuthIntent, AuthAction, AuthStore.State, AuthMessage, Any, AuthNav>(
        channel,
    ) {

    override fun executeAction(action: AuthAction) {
        super.executeAction(action)
    }
}
