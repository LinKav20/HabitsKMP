package com.gitub.linkav20.habits.main.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface MainNavContent {

    @Composable
    fun Content(
        component: MainNavComponent,
        modifier: Modifier,
    )
}
