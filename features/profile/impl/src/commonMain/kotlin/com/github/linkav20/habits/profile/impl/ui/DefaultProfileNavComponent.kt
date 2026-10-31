package com.github.linkav20.habits.profile.impl.ui

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import com.github.linkav20.habits.debugmenu.api.DebugMenuComponent
import com.github.linkav20.habits.habit.api.HabitNavComponent
import com.github.linkav20.habits.profile.api.ProfileNavComponent
import com.github.linkav20.habits.profile.api.ProfileNavigationChild
import com.github.linkav20.habits.profile.api.ProfileNavigationConfig
import com.github.linkav20.habits.profile.api.profile.ProfileComponent

internal class DefaultProfileNavComponent private constructor(
    componentContext: ComponentContext,
    private val debugMenuComponentFactory: DebugMenuComponent.Factory,
    private val profileComponentFactory: ProfileComponent.Factory,
    private val habitNavComponentFactory: com.github.linkav20.habits.habit.api.HabitNavComponent.Factory,
    private val onBackClick: () -> Unit,
) : ProfileNavComponent,
    ComponentContext by componentContext {

    private val navigation = StackNavigation<ProfileNavigationConfig>()

    override val stack: Value<ChildStack<*, ProfileNavigationChild>> = childStack(
        source = navigation,
        serializer = ProfileNavigationConfig.serializer(),
        initialConfiguration = ProfileNavigationConfig.Profile,
        handleBackButton = true,
        childFactory = ::child,
    )

    override fun onBackClicked() {
        navigation.pop { popped ->
            if (!popped) onBackClick()
        }
    }

    private fun child(
        config: ProfileNavigationConfig,
        componentContext: ComponentContext,
    ): ProfileNavigationChild = when (config) {
        ProfileNavigationConfig.Habits -> ProfileNavigationChild.Habits(
            habitNavComponentFactory.create(componentContext, ::onBackClicked),
        )
        ProfileNavigationConfig.DebugMenu -> ProfileNavigationChild.DebugMenu(
            component = debugMenuComponentFactory.create(
                componentContext = componentContext,
                onBackClick = ::onBackClicked,
                onShowNotification = {},
            ),
        )
        ProfileNavigationConfig.Profile -> ProfileNavigationChild.Profile(
            component = profileComponentFactory.create(
                componentContext = componentContext,
                onBackClick = ::onBackClicked,
                onAllHabitsClick = { navigation.pushNew(ProfileNavigationConfig.Habits) },
                onDebugMenuClick = {
                   navigation.pushNew(ProfileNavigationConfig.DebugMenu)
                },
            ),
        )
    }

    internal class Factory(
        private val debugMenuComponentFactory: DebugMenuComponent.Factory,
        private val profileComponentFactory: ProfileComponent.Factory,
        private val habitNavComponentFactory: HabitNavComponent.Factory,
    ) : ProfileNavComponent.Factory {
        override fun create(
            componentContext: ComponentContext,
            onBackClick: () -> Unit,
        ): ProfileNavComponent = DefaultProfileNavComponent(
            componentContext = componentContext,
            profileComponentFactory = profileComponentFactory,
            habitNavComponentFactory = habitNavComponentFactory,
            debugMenuComponentFactory = debugMenuComponentFactory,
            onBackClick = onBackClick,
        )
    }
}
