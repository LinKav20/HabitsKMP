package com.gitub.linkav20.habits.main.impl.domain.usecase

import com.gitub.linkav20.habits.main.impl.domain.repository.MoodRepository
import kotlinx.datetime.LocalDate

class SetMoodUseCase(
    private val moodRepository: MoodRepository,
) {

    suspend fun invoke(
        rate: Int,
        date: LocalDate,
    ) = runCatching {
        moodRepository.setMood(
            rate = rate,
            date = date,
        )
    }
}
