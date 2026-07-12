package com.github.linkav20.habits.debugmenu.impl.mvi

internal sealed interface DebugMenuAction {
    data object Init : DebugMenuAction
}
