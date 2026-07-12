package com.gitub.linkav20.habits.main.api

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner

@Stable
interface MainNavComponent : BackHandlerOwner {
    val stack: Value<ChildStack<*, MainNavigationChild>>

    fun onBackClicked()

    interface Factory {
        fun create(
            componentContext: ComponentContext,
        ): MainNavComponent
    }
}
