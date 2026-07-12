package com.github.linkav20.habits.debugmenu.impl.mvi

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import kotlinx.coroutines.launch

internal class DebugMenuBootstrapper : CoroutineBootstrapper<DebugMenuAction>() {

    override fun invoke() {
        scope.launch {
            dispatch(DebugMenuAction.Init)
        }
    }
}
