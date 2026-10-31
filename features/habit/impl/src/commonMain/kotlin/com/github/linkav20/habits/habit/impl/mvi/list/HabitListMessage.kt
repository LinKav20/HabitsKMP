package com.github.linkav20.habits.habit.impl.mvi.list

import com.github.linkav20.habits.habit.api.list.HabitListItem

internal sealed interface HabitListMessage {
    data object Loading : HabitListMessage
    data class Loaded(val items: List<HabitListItem>) : HabitListMessage
    data class ItemsChanged(val items: List<HabitListItem>) : HabitListMessage

    data object Failed : HabitListMessage
}
