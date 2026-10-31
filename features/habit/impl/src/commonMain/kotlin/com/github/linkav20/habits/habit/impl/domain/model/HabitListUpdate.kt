package com.github.linkav20.habits.habit.impl.domain.model

import com.github.linkav20.habits.habit.api.list.HabitListItem

internal sealed interface HabitListUpdate {
    data class Initial(val items: List<HabitListItem>) : HabitListUpdate
    data class Changed(val items: List<HabitListItem>) : HabitListUpdate
}
