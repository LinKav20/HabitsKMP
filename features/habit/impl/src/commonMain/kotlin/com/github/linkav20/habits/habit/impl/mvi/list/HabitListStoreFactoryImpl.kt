package com.github.linkav20.habits.habit.impl.mvi.list

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.github.linkav20.habits.habit.api.list.HabitListIntent
import com.github.linkav20.habits.habit.api.list.HabitListStore
import com.github.linkav20.habits.habit.impl.domain.usecase.CalculateHabitListUpdateUseCase
import com.github.linkav20.habits.habit.impl.domain.usecase.ObserveHabitsUseCase

internal class HabitListStoreFactoryImpl(
    private val storeFactory: StoreFactory,
    private val observeHabits: ObserveHabitsUseCase,
    private val calculateHabitListUpdate: CalculateHabitListUpdateUseCase,
) : HabitListStore.Factory {
    override fun create(): HabitListStore = object : HabitListStore,
        Store<HabitListIntent, HabitListStore.State, Nothing> by storeFactory.create(
            name = "HabitListStore",
            initialState = HabitListStore.State.Loading,
            bootstrapper = HabitListBootstrapper(),
            executorFactory = { HabitListExecutor(observeHabits, calculateHabitListUpdate) },
            reducer = HabitListReducer,
        ) {}
}
