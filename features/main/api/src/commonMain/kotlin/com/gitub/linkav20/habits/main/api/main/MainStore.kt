package com.gitub.linkav20.habits.main.api.main

import com.github.linkav20.habits.core.mvi.navigation.api.NavStore
import com.github.linkav20.habits.core.utils.Time
import com.gitub.linkav20.habits.main.api.domain.model.main.MainData
import kotlinx.datetime.LocalDate

interface MainStore : NavStore<MainIntent, MainStore.State, Any, MainNav> {
    data class State(
        val today: LocalDate = Time.now(),
        val selectedDate: LocalDate = today,
        val data: MainData? = null,
        val cachedWeekStarts: Set<LocalDate> = emptySet(),
        val failedWeekStarts: Set<LocalDate> = emptySet(),
        val initialLoadFailed: Boolean = false,
        val calendarAnchorWeekStart: LocalDate = Time.getWeekWithDay(today).first(),
        val currentPage: Int = Int.MAX_VALUE / 2,
    )

    interface Factory {
        fun create(): MainStore
    }
}
