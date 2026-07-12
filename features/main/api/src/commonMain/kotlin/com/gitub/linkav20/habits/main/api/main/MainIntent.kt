package com.gitub.linkav20.habits.main.api.main

import com.gitub.linkav20.habits.main.api.domain.model.main.CheckingItem
import kotlinx.datetime.LocalDate

sealed interface MainIntent {
    data class ChangeSelectedDate(val date: LocalDate) : MainIntent

    data class ChangeItemIsDone(
        val item: CheckingItem,
        val date: LocalDate,
    ) : MainIntent

    data class SetRate(
        val rate: Int,
        val date: LocalDate,
    ) : MainIntent

    data object UpdateData : MainIntent
}
