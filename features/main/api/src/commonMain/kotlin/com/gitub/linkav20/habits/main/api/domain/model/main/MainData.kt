package com.gitub.linkav20.habits.main.api.domain.model.main

import kotlinx.datetime.LocalDate

/**
 * Canonical data loaded for the main screen.
 *
 * Missing completion or mood entries mean that no value was recorded. Date
 * windows, placeholders and chart points are presentation concerns and are
 * intentionally not stored here.
 */
data class MainData(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val items: List<CheckingItemWithCompletion>,
    val moods: Map<LocalDate, Int>,
)
