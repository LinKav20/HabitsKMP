package com.github.linkav20.habits.profile.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface ProfileNavContent {
    @Composable
    fun Content(
        component: ProfileNavComponent,
        modifier: Modifier,
    )
}
