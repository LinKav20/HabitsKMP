package com.github.linkav20.habit.createitem.impl.mvi

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.github.linkav20.habits.createitem.api.AddHabitIntent
import com.github.linkav20.habits.createitem.api.AddHabitLabel
import com.github.linkav20.habits.createitem.api.AddHabitStore
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal class AddHabitStoreFactoryImpl(
    private val storeFactory: StoreFactory,
) : AddHabitStore.Factory,
    KoinComponent {
    override fun create(): AddHabitStore {
        return object :
            AddHabitStore,
            Store<
                    AddHabitIntent,
                    AddHabitStore.State,
                    AddHabitLabel,
                    > by storeFactory.create(
                name = "AddHabitStore",
                initialState = AddHabitStore.State(),
                bootstrapper = AddHabitBootstrapper(),
                executorFactory = {
                    AddHabitExecutor(
                        get(),
                       // get(),
                    )
                },
                reducer = AddHabitReducer(),
            ) {}
    }
}
