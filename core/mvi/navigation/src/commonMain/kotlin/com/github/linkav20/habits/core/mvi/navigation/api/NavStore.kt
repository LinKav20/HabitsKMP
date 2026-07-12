package com.github.linkav20.habits.core.mvi.navigation.api

import com.arkivanov.mvikotlin.core.store.Store
import kotlinx.coroutines.flow.Flow

interface NavStore<in Intent : Any, out State : Any, out Label : Any, Navigation : Any> : Store<Intent, State, Label> {
    val navigation: Flow<Navigation>
}
