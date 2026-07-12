package com.gitub.linkav20.habits.main.impl.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.github.linkav20.habits.core.uikit.components.animator.templateBackAnimation
import com.github.linkav20.habits.createitem.api.AddHabitContent
import com.github.linkav20.habits.habit.api.HabitContent
import com.github.linkav20.habits.todo.api.TodoContent
import com.gitub.linkav20.habits.main.api.main.MainContent
import com.gitub.linkav20.habits.main.api.MainNavComponent
import com.gitub.linkav20.habits.main.api.MainNavContent
import com.gitub.linkav20.habits.main.api.MainNavigationChild
import org.koin.compose.koinInject

internal class MainNavContentImpl : MainNavContent {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content(
        component: MainNavComponent,
        modifier: Modifier,
    ) {
        Children(
            stack = component.stack,
            modifier = Modifier.fillMaxSize(),
            animation = templateBackAnimation(
                backHandler = component.backHandler,
                onBack = component::onBackClicked,
            ),
        ) {
            when (val instance: MainNavigationChild = it.instance) {
                is MainNavigationChild.Main -> {
                    koinInject<MainContent>()
                        .Content(
                            component = instance.component,
                            modifier = Modifier,
                        )
                }

                is MainNavigationChild.AddItem -> {
                    koinInject<AddHabitContent>()
                        .Content(
                            component = instance.component,
                            modifier = Modifier,
                        )
                }

                is MainNavigationChild.ViewHabit -> {
                    koinInject<HabitContent>()
                        .Content(
                            component = instance.component,
                            modifier = Modifier,
                        )
                }

                is MainNavigationChild.ViewTodo -> {
                    koinInject<TodoContent>()
                        .Content(
                            component = instance.component,
                            modifier = Modifier,
                        )
                }

                is MainNavigationChild.Profile -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "No implementation yet",
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }
                }
            }
        }
    }
}
