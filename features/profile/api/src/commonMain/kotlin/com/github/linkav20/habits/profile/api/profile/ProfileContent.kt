package com.github.linkav20.habits.profile.api.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface ProfileContent {
    @Composable
    fun Content(
        component: ProfileComponent,
        modifier: Modifier,
    )
}
