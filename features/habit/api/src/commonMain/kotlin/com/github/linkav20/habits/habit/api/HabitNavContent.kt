package com.github.linkav20.habits.habit.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface HabitNavContent {
    @Composable
    fun Content(
        component: HabitNavComponent,
        modifier: Modifier,
    )
}
