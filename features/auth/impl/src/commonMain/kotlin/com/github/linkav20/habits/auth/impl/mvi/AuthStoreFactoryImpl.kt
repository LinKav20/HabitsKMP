package com.github.linkav20.habits.auth.impl.mvi

import org.koin.core.component.KoinComponent
import com.github.linkav20.habits.auth.api.AuthIntent
import com.github.linkav20.habits.auth.api.AuthNav
import com.github.linkav20.habits.auth.api.AuthStore
import com.github.linkav20.habits.core.mvi.navigation.api.NavStore
import com.github.linkav20.habits.core.mvi.navigation.api.NavStoreFactory

internal class AuthStoreFactoryImpl(
    private val storeFactory: NavStoreFactory,
) : AuthStore.Factory,
    KoinComponent {
    override fun create(): AuthStore {
        return object :
            AuthStore,
            NavStore<AuthIntent, AuthStore.State, Any, AuthNav> by storeFactory.create(
                name = "AuthStore",
                initialState = AuthStore.State(),
                bootstrapper = AuthBootstrapper(),
                executorFactory = {
                    AuthExecutor(it)
                },
                reducer = AuthReducer(),
            ) {}
    }
}
