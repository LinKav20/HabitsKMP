package com.github.linkav20.habits.todo.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface TodoContent {

    @Composable
    fun Content(
        component: TodoComponent,
        modifier: Modifier,
    )
}
