package com.github.linkav20.habits.debugmenu.impl.mvi

import com.github.linkav20.habits.core.featureflags.FeatureFlag

internal sealed interface DebugMenuMessage {
    data class FeatureFlagsLoaded(val flags: Map<FeatureFlag, Boolean>) : DebugMenuMessage

    data class FeatureFlagChanged(val key: FeatureFlag, val enabled: Boolean) : DebugMenuMessage

    data class Data(
        val prodToken: String?,
        val stageToken: String?,
        val isStage: Boolean,
    ) : DebugMenuMessage

    data object ClearStageToken : DebugMenuMessage
    data object ClearProdToken : DebugMenuMessage
    data class UpdateIsStage(val isStage: Boolean) : DebugMenuMessage
}
