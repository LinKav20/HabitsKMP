package com.github.linkav20.habits.compose.presentation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import com.github.linkav20.habits.core.uikit.components.snackbar.HabitSnackBarData
import com.github.linkav20.habits.datastore.datasource.ThemePreference

interface RootComponent : BackHandlerOwner {
    val stack: Value<ChildStack<*, RootNavigationChild>>
    val theme: StateFlow<ThemePreference>
    val snackBarFlow: Flow<HabitSnackBarData>

    fun onBackClicked()
    fun navigateToDebugMenu()

    interface Factory {
        fun create(componentContext: ComponentContext): RootComponent
    }
}
