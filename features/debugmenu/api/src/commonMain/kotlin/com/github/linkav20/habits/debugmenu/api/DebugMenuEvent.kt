package com.github.linkav20.habits.debugmenu.api

import com.github.linkav20.habits.core.featureflags.FeatureFlag

sealed interface DebugMenuEvent {
    data object OnBackClick : DebugMenuEvent
    data class ChangeIsStage(val isStage: Boolean) : DebugMenuEvent
    data object ResetStageToken : DebugMenuEvent
    data class ChangeFeatureFlag(val key: FeatureFlag, val enabled: Boolean) : DebugMenuEvent
    data object ResetProdToken : DebugMenuEvent
}
