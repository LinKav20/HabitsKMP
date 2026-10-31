package com.github.linkav20.habits.habit.api.detail

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

@Stable
interface HabitComponent {
    val model: StateFlow<HabitStore.State>

    fun handleEvent(event: HabitEvent)

    interface Factory {
        fun create(
            componentContext: ComponentContext,
            itemId: String,
            onBackClick: () -> Unit,
        ): HabitComponent
    }
}
