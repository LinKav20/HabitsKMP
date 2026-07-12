package com.gitub.linkav20.habits.main.impl.mvi.main

import org.koin.core.component.get
import org.koin.core.component.KoinComponent
import com.github.linkav20.habits.core.mvi.navigation.api.NavStore
import com.github.linkav20.habits.core.mvi.navigation.api.NavStoreFactory
import com.gitub.linkav20.habits.main.api.main.MainIntent
import com.gitub.linkav20.habits.main.api.main.MainNav
import com.gitub.linkav20.habits.main.api.main.MainStore

internal class MainStoreFactoryImpl(
    private val storeFactory: NavStoreFactory,
) : MainStore.Factory,
    KoinComponent {
    override fun create(): MainStore {
        return object :
            MainStore,
            NavStore<MainIntent, MainStore.State, Any, MainNav> by storeFactory.create(
                name = "MainStore",
                initialState = MainStore.State(),
                bootstrapper = MainBootstrapper(),
                executorFactory = {
                    MainExecutor(
                        it,
                        get(),
                        get(),
                        get(),
                        get(),
                        get(),
                    )
                },
                reducer = MainReducer(),
            ) {}
    }
}
