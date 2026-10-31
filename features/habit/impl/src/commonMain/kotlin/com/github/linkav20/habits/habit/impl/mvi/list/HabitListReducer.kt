package com.github.linkav20.habits.habit.impl.mvi.list

import com.arkivanov.mvikotlin.core.store.Reducer
import com.github.linkav20.habits.habit.api.list.HabitListStore

internal object HabitListReducer : Reducer<HabitListStore.State, HabitListMessage> {
    override fun HabitListStore.State.reduce(msg: HabitListMessage): HabitListStore.State = when (msg) {
        HabitListMessage.Loading -> HabitListStore.State.Loading
        HabitListMessage.Failed -> HabitListStore.State.Error
        is HabitListMessage.Loaded -> HabitListStore.State.Content(items = msg.items)
        is HabitListMessage.ItemsChanged -> if (this is HabitListStore.State.Content) {
            copy(items = msg.items)
        } else {
            this
        }
    }
}
