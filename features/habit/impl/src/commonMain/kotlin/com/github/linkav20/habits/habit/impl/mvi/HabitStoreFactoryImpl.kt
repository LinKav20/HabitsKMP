package com.github.linkav20.habits.habit.impl.mvi

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.github.linkav20.habits.habit.api.HabitIntent
import com.github.linkav20.habits.habit.api.HabitLabel
import com.github.linkav20.habits.habit.api.HabitStore
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
                initialState = HabitStore.State(""),
                bootstrapper = _root_ide_package_.com.github.linkav20.habits.habit.impl.mvi.HabitBootstrapper(
                    itemId = itemId,
                ),
                executorFactory = {
                    _root_ide_package_.com.github.linkav20.habits.habit.impl.mvi.HabitExecutor()
                },
                reducer = _root_ide_package_.com.github.linkav20.habits.habit.impl.mvi.HabitReducer(),
            ) {}
    }
}
