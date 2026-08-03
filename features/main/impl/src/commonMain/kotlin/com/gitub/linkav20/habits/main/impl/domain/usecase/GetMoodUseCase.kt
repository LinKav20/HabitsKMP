package com.gitub.linkav20.habits.main.impl.domain.usecase

import com.gitub.linkav20.habits.main.api.domain.model.main.MoodItem
import com.gitub.linkav20.habits.main.impl.domain.repository.MoodRepository
import kotlinx.datetime.LocalDate

class GetMoodUseCase(
    private val moodRepository: MoodRepository,
) {

    suspend fun invoke(
        startDate: LocalDate,
        endDate: LocalDate,
    ): Result<List<MoodItem>> = moodRepository.getMood(startDate, endDate)
}
