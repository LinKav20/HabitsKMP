package com.github.linkav20.habits.debugmenu.api

import com.github.linkav20.habits.core.featureflags.FeatureFlag

sealed interface DebugMenuIntent {
    data class ChangeStage(val isStage: Boolean) : DebugMenuIntent
    data object ResetStageToken : DebugMenuIntent
    data class ChangeFeatureFlag(val key: FeatureFlag, val enabled: Boolean) : DebugMenuIntent
    data object ResetProdToken : DebugMenuIntent
}
