package com.github.linkav20.habits.debugmenu.api

sealed interface DebugMenuEvent {
    data object OnBackClick : DebugMenuEvent
    data class ChangeIsStage(val isStage: Boolean) : DebugMenuEvent
    data object ResetStageToken : DebugMenuEvent
    data object ResetProdToken : DebugMenuEvent
}
