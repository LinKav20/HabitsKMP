package com.gitub.linkav20.habits.main.impl.domain.repository

import com.gitub.linkav20.habits.main.api.domain.model.main.MoodItem
import kotlinx.datetime.LocalDate

interface MoodRepository {

    suspend fun getMood(
        startDate: LocalDate,
        endDate: LocalDate,
    ): Result<List<MoodItem>>

    suspend fun setMood(
        rate: Int,
        date: LocalDate,
    ): Result<Unit>
}
