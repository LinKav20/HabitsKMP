package com.github.linkav20.habits.habit.impl.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.linkav20.habits.core.uikit.HabitTheme
import com.github.linkav20.habits.habit.api.HabitComponent
import com.github.linkav20.habits.habit.api.HabitContent

internal class HabitContentImpl : HabitContent {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content(
        component: HabitComponent,
        modifier: Modifier,
    ) {
        val state by component.model.collectAsState()
        Box(
            modifier = Modifier.fillMaxSize().background(HabitTheme.colors.background),
            contentAlignment = Alignment.Center
        ){
            Text("Hello habit")
        }
    }
}
