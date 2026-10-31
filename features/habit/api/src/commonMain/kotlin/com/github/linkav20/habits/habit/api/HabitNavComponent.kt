package com.github.linkav20.habits.habit.api

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner

@Stable
interface HabitNavComponent : BackHandlerOwner {
    val stack: Value<ChildStack<*, HabitNavigationChild>>

    fun onBackClicked()

    interface Factory {
        fun create(
            componentContext: ComponentContext,
            onBackClick: () -> Unit,
        ): HabitNavComponent
    }
}
