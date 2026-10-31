package com.github.linkav20.habits.habit.api.list

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

@Stable
interface HabitListComponent {
    val model: StateFlow<HabitListStore.State>
    fun handleEvent(event: HabitListEvent)

    interface Factory {
        fun create(
            componentContext: ComponentContext,
            onBackClick: () -> Unit,
            onHabitClick: (String) -> Unit,
        ): HabitListComponent
    }
}
