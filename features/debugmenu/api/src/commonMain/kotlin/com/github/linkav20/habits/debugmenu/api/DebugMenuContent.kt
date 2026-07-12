package com.github.linkav20.habits.debugmenu.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface DebugMenuContent {

    @Composable
    fun Content(
        component: DebugMenuComponent,
        modifier: Modifier,
    )
}
