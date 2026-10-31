package com.github.linkav20.habits.profile.impl.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.github.linkav20.habits.core.uikit.components.animator.templateBackAnimation
import com.github.linkav20.habits.debugmenu.api.DebugMenuContent
import com.github.linkav20.habits.habit.api.HabitNavContent
import com.github.linkav20.habits.profile.api.ProfileNavComponent
import com.github.linkav20.habits.profile.api.ProfileNavContent
import com.github.linkav20.habits.profile.api.ProfileNavigationChild
import com.github.linkav20.habits.profile.api.profile.ProfileContent
import org.koin.compose.koinInject

internal class ProfileNavContentImpl : ProfileNavContent {
    @Composable
    override fun Content(
        component: ProfileNavComponent,
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
                is ProfileNavigationChild.Habits -> koinInject<HabitNavContent>().Content(
                    component = instance.component,
                    modifier = Modifier,
                )
                is ProfileNavigationChild.DebugMenu -> koinInject<DebugMenuContent>().Content(
                    component = instance.component,
                    modifier = Modifier,
                )
                is ProfileNavigationChild.Profile -> koinInject<ProfileContent>().Content(
                    component = instance.component,
                    modifier = Modifier,
                )
            }
        }
    }
}
