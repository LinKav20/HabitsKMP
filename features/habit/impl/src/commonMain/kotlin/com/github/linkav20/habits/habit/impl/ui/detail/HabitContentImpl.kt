package com.github.linkav20.habits.habit.impl.ui.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.padding
import com.github.linkav20.habits.core.uikit.components.topbar.HabitCenterTopBar
import com.github.linkav20.habits.habit.api.detail.HabitEvent
import com.github.linkav20.habits.habit.impl.resources.Res
import com.github.linkav20.habits.habit.impl.resources.habit_title
import org.jetbrains.compose.resources.stringResource
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.linkav20.habits.core.uikit.HabitTheme
import com.github.linkav20.habits.habit.api.detail.HabitComponent
import com.github.linkav20.habits.habit.api.detail.HabitContent

internal class HabitContentImpl : HabitContent {
    @Composable
    override fun Content(
        component: HabitComponent,
        modifier: Modifier,
    ) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            containerColor = HabitTheme.colors.background,
            topBar = {
                HabitCenterTopBar(
                    title = stringResource(Res.string.habit_title),
                    containerColor = HabitTheme.colors.background,
                    onBackClick = { component.handleEvent(HabitEvent.BackClicked) },
                )
            },
        ) { padding ->
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Hello habit")
            }
        }
    }
}
