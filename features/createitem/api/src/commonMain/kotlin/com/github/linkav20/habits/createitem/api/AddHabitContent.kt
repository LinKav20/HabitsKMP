package com.github.linkav20.habits.createitem.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface AddHabitContent {

    @Composable
    fun Content(
        component: AddHabitComponent,
        modifier: Modifier,
    )
}
