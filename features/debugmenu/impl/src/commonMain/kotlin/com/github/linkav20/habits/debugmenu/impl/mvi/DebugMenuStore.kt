package com.github.linkav20.habits.debugmenu.impl.mvi

import com.arkivanov.mvikotlin.core.store.Store
import com.github.linkav20.habits.debugmenu.api.DebugMenuIntent
import com.github.linkav20.habits.debugmenu.api.DebugMenuLabel
import com.github.linkav20.habits.debugmenu.api.DebugMenuState

interface DebugMenuStore : Store<DebugMenuIntent, DebugMenuState, DebugMenuLabel> {

    interface Factory {
        fun create(onShowNotification: () -> Unit): DebugMenuStore
    }
}
