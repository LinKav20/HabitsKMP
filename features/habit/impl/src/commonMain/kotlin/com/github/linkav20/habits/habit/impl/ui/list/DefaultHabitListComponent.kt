package com.github.linkav20.habits.habit.impl.ui.list

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.github.linkav20.habits.habit.api.list.HabitListEvent
import com.github.linkav20.habits.habit.api.list.HabitListComponent
import com.github.linkav20.habits.habit.api.list.HabitListStore
import com.github.linkav20.habits.habit.api.list.HabitListIntent
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
internal class DefaultHabitListComponent private constructor(
    componentContext: ComponentContext,
    storeFactory: HabitListStore.Factory,
    private val onBackClick: () -> Unit,
    private val onHabitClick: (String) -> Unit,
) : HabitListComponent, ComponentContext by componentContext {
    private val store = instanceKeeper.getStore { storeFactory.create() }
    override val model = store.stateFlow

    override fun handleEvent(event: HabitListEvent) {
        when (event) {
            HabitListEvent.OnBack -> onBackClick()
            HabitListEvent.OnReload -> store.accept(HabitListIntent.Retry)
            is HabitListEvent.OnHabitChanged -> onHabitClick(event.id)
        }
    }

    internal class Factory(private val storeFactory: HabitListStore.Factory) : HabitListComponent.Factory {
        override fun create(
            componentContext: ComponentContext,
            onBackClick: () -> Unit,
            onHabitClick: (String) -> Unit,
        ): HabitListComponent = DefaultHabitListComponent(componentContext, storeFactory, onBackClick, onHabitClick)
    }
}
