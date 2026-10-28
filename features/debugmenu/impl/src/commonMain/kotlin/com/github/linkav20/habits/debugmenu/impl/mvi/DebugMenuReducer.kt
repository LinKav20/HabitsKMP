package com.github.linkav20.habits.debugmenu.impl.mvi

import com.arkivanov.mvikotlin.core.store.Reducer
import com.github.linkav20.habits.debugmenu.api.DebugMenuState

internal class DebugMenuReducer : Reducer<DebugMenuState, DebugMenuMessage> {
    override fun DebugMenuState.reduce(msg: DebugMenuMessage): DebugMenuState =
        when (msg) {
            is DebugMenuMessage.FeatureFlagsLoaded -> copy(featureFlags = msg.flags)
            is DebugMenuMessage.FeatureFlagChanged -> copy(featureFlags = featureFlags + (msg.key to msg.enabled))
            is DebugMenuMessage.Data -> copy(
                isStage = msg.isStage,
                prodToken = msg.prodToken,
                stageToken = msg.stageToken,
            )

            is DebugMenuMessage.ClearStageToken -> copy(
                stageToken = null,
            )

            is DebugMenuMessage.ClearProdToken -> copy(
                prodToken = null,
            )

            is DebugMenuMessage.UpdateIsStage -> copy(
                isStage = msg.isStage,
            )
        }
}
