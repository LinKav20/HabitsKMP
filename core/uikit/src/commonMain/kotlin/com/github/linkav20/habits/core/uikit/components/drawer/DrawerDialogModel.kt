package com.github.linkav20.habits.core.uikit.components.drawer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable

@Stable
data class DrawerDialogModel(
    val titleContent: @Composable () -> Unit,
    val mainAreaContent: @Composable () -> Unit,
    val bottomAreaContent: @Composable () -> Unit,
)
