package com.github.linkav20.habits.profile.impl.ui

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.Value
import com.github.linkav20.habits.profile.api.ProfileNavComponent
import com.github.linkav20.habits.profile.api.ProfileNavigationChild
import com.github.linkav20.habits.profile.api.ProfileNavigationConfig
import com.github.linkav20.habits.profile.api.profile.ProfileComponent

internal class DefaultProfileNavComponent private constructor(
    componentContext: ComponentContext,
    private val profileComponentFactory: ProfileComponent.Factory,
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
        ProfileNavigationConfig.Profile -> ProfileNavigationChild.Profile(
            component = profileComponentFactory.create(
                componentContext = componentContext,
                onBackClick = ::onBackClicked,
            ),
        )
    }

    internal class Factory(
        private val profileComponentFactory: ProfileComponent.Factory,
    ) : ProfileNavComponent.Factory {
        override fun create(
            componentContext: ComponentContext,
            onBackClick: () -> Unit,
        ): ProfileNavComponent = DefaultProfileNavComponent(
            componentContext = componentContext,
            profileComponentFactory = profileComponentFactory,
            onBackClick = onBackClick,
        )
    }
}
