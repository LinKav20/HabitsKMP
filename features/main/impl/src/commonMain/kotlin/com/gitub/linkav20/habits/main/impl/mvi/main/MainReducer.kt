package com.gitub.linkav20.habits.main.impl.mvi.main

import com.arkivanov.mvikotlin.core.store.Reducer
import com.gitub.linkav20.habits.main.api.main.MainStore

internal class MainReducer : Reducer<MainStore.State, MainMessage> {
    override fun MainStore.State.reduce(msg: MainMessage): MainStore.State {
        return when (msg) {
            is MainMessage.ChangeSelectedDate -> copy(
                selectedDate = msg.selectedDate,
                tasks = msg.tasks,
            )

            is MainMessage.LoadInitialData -> copy(
                itemsWithMood = msg.itemsWithMood,
                today = msg.today,
                selectedDate = msg.selectedDate,
                currentPage = msg.currentPage,
                tasks = msg.tasks,
            )

            is MainMessage.DataUpdated -> copy(
                itemsWithMood = msg.itemsWithMood,
                tasks = msg.tasks,
            )

            is MainMessage.UpdateCompletion -> copy(
                itemsWithMood = msg.itemsWithMood,
                tasks = msg.tasks,
            )

            is MainMessage.UpdateMood -> copy(
                itemsWithMood = msg.itemsWithMood,
            )
        }
    }
}
