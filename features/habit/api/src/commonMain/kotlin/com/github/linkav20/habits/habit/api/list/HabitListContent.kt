package com.github.linkav20.habits.habit.api.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface HabitListContent {
    @Composable
    fun Content(
        component: HabitListComponent,
        modifier: Modifier,
    )
}
