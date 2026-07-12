package com.gitub.linkav20.habits.main.impl.ui.main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.gitub.linkav20.habits.main.api.communication.GlobalCommunicationEvent
import com.gitub.linkav20.habits.main.api.domain.model.main.HabitItem
import com.gitub.linkav20.habits.main.api.domain.model.main.TodoItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent
import com.gitub.linkav20.habits.main.api.main.MainComponent
import com.gitub.linkav20.habits.main.api.main.MainEvent
import com.gitub.linkav20.habits.main.api.main.MainIntent
import com.gitub.linkav20.habits.main.api.main.MainStore
import com.gitub.linkav20.habits.main.impl.communication.GlobalCommunication
import kotlinx.coroutines.launch
import org.koin.core.component.get

@OptIn(ExperimentalCoroutinesApi::class)
internal class DefaultMainComponent private constructor(
    componentContext: ComponentContext,
    private val storeFactory: MainStore.Factory,
    private val globalCourseCommunication: GlobalCommunication,
    private val onProfileClick: () -> Unit,
    private val onAddItemClick: () -> Unit,
    private val onViewTodoClick: (String) -> Unit,
    private val onViewHabitClick: (String) -> Unit,
) : MainComponent,
    ComponentContext by componentContext {
    private val store = instanceKeeper.getStore { storeFactory.create() }
    override val model: StateFlow<MainStore.State> = store.stateFlow

    init {
        listenGlobalChannelEvents()
    }

    override fun handleEvent(event: MainEvent) {
        when (event) {
            is MainEvent.OnOpenItemClick -> {
                when (event.item) {
                    is TodoItem -> onViewTodoClick(event.item.id)
                    is HabitItem -> onViewHabitClick(event.item.id)
                }
            }

            is MainEvent.ChangeSelectedDate -> {
                store.accept(MainIntent.ChangeSelectedDate(event.date))
            }

            is MainEvent.OnCheckingItemClick -> {
                store.accept(
                    MainIntent.ChangeItemIsDone(
                        item = event.checkingItem,
                        date = event.date,
                    ),
                )
            }

            is MainEvent.SetRate -> {
                store.accept(
                    MainIntent.SetRate(
                        rate = event.rate,
                        date = event.date,
                    ),
                )
            }

            MainEvent.OnProfileClick -> {
                onProfileClick()
            }

            MainEvent.OnAddItemClick -> {
                onAddItemClick()
            }
        }
    }

    private fun listenGlobalChannelEvents() {
        coroutineScope().launch {
            globalCourseCommunication.events.collect {
                when (it) {
                    is GlobalCommunicationEvent.DataUpdated -> {
                        store.accept(MainIntent.UpdateData)
                    }
                }
            }
        }
    }

    internal class Factory(
        private val storeFactory: MainStore.Factory,
    ) : MainComponent.Factory,
        KoinComponent {
        override fun create(
            componentContext: ComponentContext,
            onProfileClick: () -> Unit,
            onAddItemClick: () -> Unit,
            onViewTodoClick: (String) -> Unit,
            onViewHabitClick: (String) -> Unit,
        ): MainComponent {
            return DefaultMainComponent(
                componentContext = componentContext,
                storeFactory = storeFactory,
                globalCourseCommunication = get(),
                onProfileClick = onProfileClick,
                onAddItemClick = onAddItemClick,
                onViewHabitClick = onViewHabitClick,
                onViewTodoClick = onViewTodoClick,
            )
        }
    }
}
