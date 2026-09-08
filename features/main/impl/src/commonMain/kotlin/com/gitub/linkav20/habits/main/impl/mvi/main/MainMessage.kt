package com.gitub.linkav20.habits.main.impl.mvi.main

import com.gitub.linkav20.habits.main.api.domain.model.main.MainData
import kotlinx.datetime.LocalDate

internal sealed interface MainMessage {

    data class ChangeSelectedDate(
        val selectedDate: LocalDate,
    ) : MainMessage

    data class LoadInitialData(
        val data: MainData,
        val cachedWeekStarts: Set<LocalDate>,
        val today: LocalDate,
        val selectedDate: LocalDate,
        val currentPage: Int,
    ) : MainMessage

    data class DataUpdated(
        val data: MainData,
        val cachedWeekStarts: Set<LocalDate>,
        val failedWeekStarts: Set<LocalDate>,
    ) : MainMessage

    data object InitialLoadStarted : MainMessage

    data object InitialLoadFailed : MainMessage

    data class UpdateCompletion(
        val data: MainData,
    ) : MainMessage

    data class UpdateMood(
        val data: MainData,
    ) : MainMessage
}
