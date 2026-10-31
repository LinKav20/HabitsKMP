package com.github.linkav20.habits.habit.impl.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.github.linkav20.habits.core.uikit.components.animator.templateBackAnimation
import com.github.linkav20.habits.habit.api.detail.HabitContent
import com.github.linkav20.habits.habit.api.HabitNavComponent
import com.github.linkav20.habits.habit.api.HabitNavContent
import com.github.linkav20.habits.habit.api.HabitNavigationChild
import com.github.linkav20.habits.habit.api.list.HabitListContent
import org.koin.compose.koinInject

internal class HabitNavContentImpl : HabitNavContent {
    @Composable
    override fun Content(
        component: HabitNavComponent,
        modifier: Modifier,
    ) {
        Children(
            stack = component.stack,
            modifier = modifier.fillMaxSize(),
            animation = templateBackAnimation(
                backHandler = component.backHandler,
                onBack = component::onBackClicked,
            ),
        ) {
            when (val instance = it.instance) {
                is HabitNavigationChild.Detail -> koinInject<HabitContent>().Content(
                    component = instance.component,
                    modifier = Modifier,
                )
                is HabitNavigationChild.List -> koinInject<HabitListContent>().Content(
                    component = instance.component,
                    modifier = Modifier,
                )
            }
        }
    }
}
