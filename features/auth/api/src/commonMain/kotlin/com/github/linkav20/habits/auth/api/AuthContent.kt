package com.github.linkav20.habits.auth.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface AuthContent {

    @Composable
    fun Content(
        component: AuthComponent,
        modifier: Modifier,
    )
}
