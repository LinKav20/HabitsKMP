package com.github.linkav20.habits.core.mvi.navigation.impl

import com.arkivanov.mvikotlin.core.store.Store
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import com.github.linkav20.habits.core.mvi.navigation.api.NavStore

internal class NavStoreImpl<in Intent : Any, out State : Any, out Label : Any, Navigation : Any>(
    receiveChannel: ReceiveChannel<Navigation>,
    originalStore: Store<Intent, State, Label>,
) : NavStore<Intent, State, Label, Navigation>,
    Store<Intent, State, Label> by originalStore {

    override val navigation: Flow<Navigation> = receiveChannel.receiveAsFlow()
}
