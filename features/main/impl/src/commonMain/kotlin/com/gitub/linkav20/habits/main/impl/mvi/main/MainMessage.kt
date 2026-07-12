package com.gitub.linkav20.habits.main.impl.mvi.main

import com.gitub.linkav20.habits.main.api.domain.model.main.CheckingItem
import com.gitub.linkav20.habits.main.api.domain.model.main.ItemsWithMood
import kotlinx.datetime.LocalDate

internal sealed interface MainMessage {

    data class ChangeSelectedDate(
        val selectedDate: LocalDate,
        val tasks: Map<LocalDate, List<Pair<CheckingItem, Boolean?>>>,
    ) : MainMessage

    data class LoadInitialData(
        val itemsWithMood: ItemsWithMood,
        val today: LocalDate,
        val selectedDate: LocalDate,
        val currentPage: Int,
        val tasks: Map<LocalDate, List<Pair<CheckingItem, Boolean?>>>,
    ) : MainMessage

    data class DataUpdated(
        val itemsWithMood: ItemsWithMood,
        val tasks: Map<LocalDate, List<Pair<CheckingItem, Boolean?>>>,
    ) : MainMessage

    data class UpdateCompletion(
        val itemsWithMood: ItemsWithMood,
        val tasks: Map<LocalDate, List<Pair<CheckingItem, Boolean?>>>,
    ) : MainMessage

    data class UpdateMood(
        val itemsWithMood: ItemsWithMood,
    ) : MainMessage
}
