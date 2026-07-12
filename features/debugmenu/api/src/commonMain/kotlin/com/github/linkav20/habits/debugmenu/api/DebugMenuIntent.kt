package com.github.linkav20.habits.debugmenu.api

sealed interface DebugMenuIntent {
    data class ChangeStage(val isStage: Boolean) : DebugMenuIntent
    data object ResetStageToken : DebugMenuIntent
    data object ResetProdToken : DebugMenuIntent
}
