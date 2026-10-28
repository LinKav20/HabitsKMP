package com.github.linkav20.habits.debugmenu.impl.mvi

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.github.linkav20.habits.debugmenu.api.DebugMenuIntent
import com.github.linkav20.habits.debugmenu.api.DebugMenuLabel
import com.github.linkav20.habits.debugmenu.api.DebugMenuState
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal class DebugMenuStoreFactoryImpl(
    private val storeFactory: StoreFactory,
) : DebugMenuStore.Factory,
    KoinComponent {
    override fun create(onShowNotification: () -> Unit): DebugMenuStore {
        return object :
            DebugMenuStore,
            Store<
                    DebugMenuIntent,
                    DebugMenuState,
                    DebugMenuLabel,
                    > by storeFactory.create(
                name = "DebugMenuStore",
                initialState = DebugMenuState(),
                bootstrapper = DebugMenuBootstrapper(),
                executorFactory = {
                    DebugMenuExecutor(
                        onShowNotification = onShowNotification,
                        settingsStorage = get(),
                        setFeatureFlagUseCase = get(),
                        getFeatureFlagsUseCase = get(),
                    )
                },
                reducer = DebugMenuReducer(),
            ) {}
    }
}
