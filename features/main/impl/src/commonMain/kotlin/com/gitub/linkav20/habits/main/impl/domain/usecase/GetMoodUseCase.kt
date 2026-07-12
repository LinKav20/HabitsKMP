package com.gitub.linkav20.habits.main.impl.domain.usecase

import com.github.linkav20.habits.core.utils.Time
import com.gitub.linkav20.habits.main.api.domain.model.main.MoodItem
import com.gitub.linkav20.habits.main.impl.domain.repository.MoodRepository
import kotlinx.datetime.LocalDate

class GetMoodUseCase(
    private val moodRepository: MoodRepository,
) {

    suspend fun invoke(
        startDate: LocalDate,
        endDate: LocalDate,
    ): Result<List<MoodItem>> = runCatching {
        val moodMap = moodRepository.getMood(startDate, endDate)
            .getOrThrow()
            .associate { it.date to it.rate }

        Time.getDateRangeInclusive(startDate, endDate).map { date ->
            MoodItem(date = date, rate = moodMap[date])
        }
    }
}
