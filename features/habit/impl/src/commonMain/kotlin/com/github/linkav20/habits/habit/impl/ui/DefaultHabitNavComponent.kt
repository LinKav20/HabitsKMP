package com.github.linkav20.habits.habit.impl.ui

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import com.github.linkav20.habits.habit.api.detail.HabitComponent
import com.github.linkav20.habits.habit.api.HabitNavComponent
import com.github.linkav20.habits.habit.api.HabitNavigationChild
import com.github.linkav20.habits.habit.api.HabitNavigationConfig
import com.github.linkav20.habits.habit.api.list.HabitListComponent

internal class DefaultHabitNavComponent private constructor(
    componentContext: ComponentContext,
    private val habitComponentFactory: HabitComponent.Factory,
    private val habitListComponentFactory: HabitListComponent.Factory,
    private val onBackClick: () -> Unit,
) : HabitNavComponent,
    ComponentContext by componentContext {

    private val navigation = StackNavigation<HabitNavigationConfig>()

    override val stack: Value<ChildStack<*, HabitNavigationChild>> = childStack(
        source = navigation,
        serializer = HabitNavigationConfig.serializer(),
        initialConfiguration = HabitNavigationConfig.List,
        handleBackButton = true,
        childFactory = ::child,
    )

    override fun onBackClicked() {
        navigation.pop { popped ->
            if (!popped) onBackClick()
        }
    }

    private fun child(
        config: HabitNavigationConfig,
        componentContext: ComponentContext,
    ): HabitNavigationChild = when (config) {
        is HabitNavigationConfig.Detail -> HabitNavigationChild.Detail(
            component = habitComponentFactory.create(
                componentContext = componentContext,
                onBackClick = ::onBackClicked,
                itemId = config.habitId,
            ),
        )
        HabitNavigationConfig.List -> HabitNavigationChild.List(
            component = habitListComponentFactory.create(
                componentContext = componentContext,
                onBackClick = ::onBackClicked,
                onHabitClick = { id ->
                    navigation.pushNew(HabitNavigationConfig.Detail(id))
                },
            ),
        )
    }

    internal class Factory(
        private val habitComponentFactory: HabitComponent.Factory,
        private val habitListComponentFactory: HabitListComponent.Factory,
    ) : HabitNavComponent.Factory {
        override fun create(
            componentContext: ComponentContext,
            onBackClick: () -> Unit,
        ): HabitNavComponent = DefaultHabitNavComponent(
            componentContext = componentContext,
            habitListComponentFactory = habitListComponentFactory,
            habitComponentFactory = habitComponentFactory,
            onBackClick = onBackClick,
        )
    }
}
