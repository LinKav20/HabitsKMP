package com.github.linkav20.habits.debugmenu.api

import com.github.linkav20.habits.core.featureflags.FeatureFlag

data class DebugMenuState(
    val featureFlags: Map<FeatureFlag, Boolean> = emptyMap(),
    val stageToken: String? = null,
    val prodToken: String? = null,
    val isStage: Boolean = false,
)
