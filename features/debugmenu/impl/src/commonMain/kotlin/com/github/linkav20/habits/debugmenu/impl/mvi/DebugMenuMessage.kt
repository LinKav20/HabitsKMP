package com.github.linkav20.habits.debugmenu.impl.mvi

internal sealed interface DebugMenuMessage {
    data class Data(
        val prodToken: String?,
        val stageToken: String?,
        val isStage: Boolean,
    ) : DebugMenuMessage

    data object ClearStageToken : DebugMenuMessage
    data object ClearProdToken : DebugMenuMessage
    data class UpdateIsStage(val isStage: Boolean) : DebugMenuMessage
}
