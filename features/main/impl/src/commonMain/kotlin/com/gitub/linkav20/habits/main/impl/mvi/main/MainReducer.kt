package com.gitub.linkav20.habits.main.impl.mvi.main

import com.arkivanov.mvikotlin.core.store.Reducer
import com.gitub.linkav20.habits.main.api.main.MainStore

internal class MainReducer : Reducer<MainStore.State, MainMessage> {
    override fun MainStore.State.reduce(msg: MainMessage): MainStore.State {
        return when (msg) {
            is MainMessage.ChangeSelectedDate -> copy(
                selectedDate = msg.selectedDate,
            )

            is MainMessage.LoadInitialData -> copy(
                data = msg.data,
                today = msg.today,
                selectedDate = msg.selectedDate,
                currentPage = msg.currentPage,
            )

            is MainMessage.DataUpdated -> copy(
                data = msg.data,
            )

            is MainMessage.UpdateCompletion -> copy(
                data = msg.data,
            )

            is MainMessage.UpdateMood -> copy(
                data = msg.data,
            )
        }
    }
}
