package com.github.linkav20.habits.debugmenu.api

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

@Stable
interface DebugMenuComponent {
    val model: StateFlow<DebugMenuState>

    fun handleAction(event: DebugMenuEvent)

    interface Factory {
        fun create(
            componentContext: ComponentContext,
            onShowNotification: () -> Unit,
            onBackClick: () -> Unit,
        ): DebugMenuComponent
    }
}
