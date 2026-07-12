package com.github.linkav20.habits.habit.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface HabitContent {

    @Composable
    fun Content(
        component: HabitComponent,
        modifier: Modifier,
    )
}
