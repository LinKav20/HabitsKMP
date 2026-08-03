package com.gitub.linkav20.habits.main.api.main

import com.github.linkav20.habits.core.mvi.navigation.api.NavStore
import com.github.linkav20.habits.core.utils.Time
import com.gitub.linkav20.habits.main.api.domain.model.main.MainData
import kotlinx.datetime.LocalDate

interface MainStore : NavStore<MainIntent, MainStore.State, Any, MainNav> {
    data class State(
        val today: LocalDate = Time.now(),
        val selectedDate: LocalDate = Time.now(),
        val data: MainData? = null,
        val currentPage: Int = 0,
    )

    interface Factory {
        fun create(): MainStore
    }
}
