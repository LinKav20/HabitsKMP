package com.github.linkav20.habits.auth.impl.mvi

import com.arkivanov.mvikotlin.core.store.Reducer
import com.github.linkav20.habits.auth.api.AuthStore

internal class AuthReducer : Reducer<AuthStore.State, AuthMessage> {
    override fun AuthStore.State.reduce(msg: AuthMessage): AuthStore.State = copy("")
}
