package com.github.linkav20.habits.auth.impl.ui

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent
import com.github.linkav20.habits.auth.api.AuthComponent
import com.github.linkav20.habits.auth.api.AuthStore

@OptIn(ExperimentalCoroutinesApi::class)
internal class DefaultAuthComponent private constructor(
    componentContext: ComponentContext,
    private val authStoreFactory: AuthStore.Factory,
    private val onAuthorized: () -> Unit,
) : AuthComponent,
    ComponentContext by componentContext {
    private val store = instanceKeeper.getStore { authStoreFactory.create() }
    override val model: StateFlow<AuthStore.State> = store.stateFlow

    override fun onAuthorizedClicked() {
        onAuthorized()
    }

    internal class Factory(
        private val authStoreFactory: AuthStore.Factory,
    ) : AuthComponent.Factory,
        KoinComponent {
        override fun create(
            componentContext: ComponentContext,
            onAuthorizedClicked: () -> Unit,
            onBackPressed: () -> Unit,
        ): AuthComponent {
            return DefaultAuthComponent(
                componentContext = componentContext,
                authStoreFactory = authStoreFactory,
                onAuthorized = onAuthorizedClicked,
            )
        }
    }
}
