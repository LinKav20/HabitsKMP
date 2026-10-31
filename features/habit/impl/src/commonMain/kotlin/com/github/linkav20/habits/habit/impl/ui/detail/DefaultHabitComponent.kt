package com.github.linkav20.habits.habit.impl.ui.detail

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.github.linkav20.habits.core.uikit.components.snackbar.SnackBarNotifier
import com.github.linkav20.habits.habit.api.detail.HabitComponent
import com.github.linkav20.habits.habit.api.detail.HabitEvent
import com.github.linkav20.habits.habit.api.detail.HabitStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

@OptIn(ExperimentalCoroutinesApi::class)
internal class DefaultHabitComponent private constructor(
    componentContext: ComponentContext,
    private val itemId: String,
    private val storeFactory: HabitStore.Factory,
    private val snackBarNotifier: SnackBarNotifier,
    private val onBackClick: () -> Unit,
) : HabitComponent,
    ComponentContext by componentContext {
    private val store = instanceKeeper.getStore { storeFactory.create(itemId) }
    override val model: StateFlow<HabitStore.State> = store.stateFlow
    private val scope = coroutineScope()

    override fun handleEvent(event: HabitEvent) {
        when (event) {
            HabitEvent.BackClicked -> onBackClick()
        }
    }

    internal class Factory(
        private val storeFactory: HabitStore.Factory,
    ) : HabitComponent.Factory,
        KoinComponent {
        override fun create(
            componentContext: ComponentContext,
            itemId: String,
            onBackClick: () -> Unit,
        ): HabitComponent {
            return DefaultHabitComponent(
                componentContext = componentContext,
                storeFactory = storeFactory,
                itemId = itemId,
                onBackClick = onBackClick,
                snackBarNotifier = get(),
            )
        }
    }
}
