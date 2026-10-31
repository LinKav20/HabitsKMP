package com.github.linkav20.habits.habit.api

import com.github.linkav20.habits.habit.api.detail.HabitComponent
import com.github.linkav20.habits.core.mvi.navigation.BaseNavigationConfig
import com.github.linkav20.habits.habit.api.list.HabitListComponent
import kotlinx.serialization.Serializable

@Serializable
sealed interface HabitNavigationConfig : BaseNavigationConfig {
    @Serializable
    data object List : HabitNavigationConfig {
        override val screenName = "habit_list"
    }

    @Serializable
    data class Detail(val habitId: String) : HabitNavigationConfig {
        override val screenName = "habit_detail"
    }
}

sealed class HabitNavigationChild {
    data class List(val component: HabitListComponent) : HabitNavigationChild()
    data class Detail(val component: HabitComponent) : HabitNavigationChild()
}
