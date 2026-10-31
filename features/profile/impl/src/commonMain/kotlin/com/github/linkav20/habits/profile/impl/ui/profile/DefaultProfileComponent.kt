package com.github.linkav20.habits.profile.impl.ui.profile

import com.arkivanov.decompose.ComponentContext
import com.github.linkav20.habits.profile.api.profile.ProfileComponent

internal class DefaultProfileComponent private constructor(
    componentContext: ComponentContext,
    private val onDebugMenuClick: () -> Unit,
    private val onAllHabitsClick: () -> Unit,
    private val onBackClick: () -> Unit,
) : ProfileComponent,
    ComponentContext by componentContext {

    override fun onDebugMenuClicked() = onDebugMenuClick()

    override fun onAllHabitsClicked() = onAllHabitsClick()

    override fun onBackClicked() = onBackClick()

    internal class Factory : ProfileComponent.Factory {
        override fun create(
            componentContext: ComponentContext,
            onBackClick: () -> Unit,
            onDebugMenuClick: () -> Unit,
            onAllHabitsClick: () -> Unit,
        ): ProfileComponent = DefaultProfileComponent(
            componentContext = componentContext,
            onBackClick = onBackClick,
            onDebugMenuClick = onDebugMenuClick,
            onAllHabitsClick = onAllHabitsClick,
        )
    }
}
