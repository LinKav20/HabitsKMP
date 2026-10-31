package com.github.linkav20.habits.habit.impl.mvi.detail

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.github.linkav20.habits.habit.api.detail.HabitIntent
import com.github.linkav20.habits.habit.api.detail.HabitLabel
import com.github.linkav20.habits.habit.api.detail.HabitStore
import org.koin.core.component.KoinComponent

internal class HabitStoreFactoryImpl(
    private val storeFactory: StoreFactory,
) : HabitStore.Factory,
    KoinComponent {
    override fun create(
        itemId: String,
    ): HabitStore {
        return object :
            HabitStore,
            Store<
                    HabitIntent,
                    HabitStore.State,
                    HabitLabel,
                    > by storeFactory.create(
                name = "ItemHabitStore",
                initialState = HabitStore.State(itemId),
                bootstrapper = HabitBootstrapper(
                    itemId = itemId,
                ),
                executorFactory = {
                    HabitExecutor()
                },
                reducer = HabitReducer(),
            ) {}
    }
}
