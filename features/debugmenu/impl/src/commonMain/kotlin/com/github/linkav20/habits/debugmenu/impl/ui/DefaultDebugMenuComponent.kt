package com.github.linkav20.habits.debugmenu.impl.ui

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent
import com.github.linkav20.habits.debugmenu.api.DebugMenuComponent
import com.github.linkav20.habits.debugmenu.api.DebugMenuEvent
import com.github.linkav20.habits.debugmenu.api.DebugMenuIntent
import com.github.linkav20.habits.debugmenu.api.DebugMenuState
import com.github.linkav20.habits.debugmenu.impl.mvi.DebugMenuStore

@OptIn(ExperimentalCoroutinesApi::class)
internal class DefaultDebugMenuComponent private constructor(
    componentContext: ComponentContext,
    private val debugMenuStoreFactory: DebugMenuStore.Factory,
    private val onShowNotification: () -> Unit,
    private val onBackClick: () -> Unit,
) : DebugMenuComponent,
    ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        debugMenuStoreFactory.create(
            onShowNotification = onShowNotification,
        )
    }
    override val model: StateFlow<DebugMenuState> = store.stateFlow

    override fun handleAction(event: DebugMenuEvent) {
        when (event) {
            DebugMenuEvent.OnBackClick -> onBackClick()
            is DebugMenuEvent.ChangeIsStage -> store.accept(DebugMenuIntent.ChangeStage(event.isStage))
            DebugMenuEvent.ResetStageToken -> store.accept(DebugMenuIntent.ResetStageToken)
            DebugMenuEvent.ResetProdToken -> store.accept(DebugMenuIntent.ResetProdToken)
        }
    }

    internal class Factory(
        private val debugMenuStoreFactory: DebugMenuStore.Factory,
    ) : DebugMenuComponent.Factory,
        KoinComponent {

        override fun create(
            componentContext: ComponentContext,
            onShowNotification: () -> Unit,
            onBackClick: () -> Unit,
        ): DebugMenuComponent {
            return DefaultDebugMenuComponent(
                componentContext = componentContext,
                debugMenuStoreFactory = debugMenuStoreFactory,
                onShowNotification = onShowNotification,
                onBackClick = onBackClick,
            )
        }
    }
}
