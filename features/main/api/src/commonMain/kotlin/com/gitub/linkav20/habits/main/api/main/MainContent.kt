package com.gitub.linkav20.habits.main.api.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface MainContent {

    @Composable
    fun Content(
        component: MainComponent,
        modifier: Modifier,
    )
}
