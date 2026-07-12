package com.gitub.linkav20.habits.main.api.main

import com.gitub.linkav20.habits.main.api.domain.model.main.CheckingItem
import kotlinx.datetime.LocalDate

sealed interface MainEvent {
    data class OnOpenItemClick(val item: CheckingItem) : MainEvent

    data object OnProfileClick : MainEvent

    data object OnAddItemClick : MainEvent

    data class ChangeSelectedDate(val date: LocalDate) : MainEvent

    data class OnCheckingItemClick(
        val checkingItem: CheckingItem,
        val date: LocalDate,
    ) : MainEvent

    data class SetRate(
        val rate: Int,
        val date: LocalDate,
    ) : MainEvent
}
