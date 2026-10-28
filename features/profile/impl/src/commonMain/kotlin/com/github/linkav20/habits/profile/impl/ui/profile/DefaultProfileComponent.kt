package com.github.linkav20.habits.profile.impl.ui.profile

import com.arkivanov.decompose.ComponentContext
import com.github.linkav20.habits.profile.api.profile.ProfileComponent

internal class DefaultProfileComponent private constructor(
    componentContext: ComponentContext,
    private val onBackClick: () -> Unit,
) : ProfileComponent,
    ComponentContext by componentContext {

    override fun onBackClicked() = onBackClick()

    internal class Factory : ProfileComponent.Factory {
        override fun create(
            componentContext: ComponentContext,
            onBackClick: () -> Unit,
        ): ProfileComponent = DefaultProfileComponent(
            componentContext = componentContext,
            onBackClick = onBackClick,
        )
    }
}
