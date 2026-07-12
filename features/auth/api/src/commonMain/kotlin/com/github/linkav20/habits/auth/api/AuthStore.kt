package com.github.linkav20.habits.auth.api

import com.github.linkav20.habits.core.mvi.navigation.api.NavStore

interface AuthStore : NavStore<AuthIntent, AuthStore.State, Any, AuthNav> {
    data class State(
        val value: String = "",
    )

    interface Factory {
        fun create(): AuthStore
    }
}
