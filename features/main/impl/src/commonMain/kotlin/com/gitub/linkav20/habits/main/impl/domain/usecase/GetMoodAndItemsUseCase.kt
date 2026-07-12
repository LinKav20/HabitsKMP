package com.gitub.linkav20.habits.main.impl.domain.usecase

import com.github.linkav20.habits.core.utils.Time
import com.gitub.linkav20.habits.main.api.domain.model.main.ItemsWithMood
import kotlinx.datetime.LocalDate

class GetMoodAndItemsUseCase(
    private val getItemsWithCompletionUseCase: GetItemsWithCompletionUseCase,
    private val getMoodUseCase: GetMoodUseCase,
) {

    suspend fun invoke(
        startDate: LocalDate,
        endDate: LocalDate,
        pagesCount: Int,
    ): Result<ItemsWithMood> = runCatching {
        val days = Time.getDateRangeInclusive(startDate, endDate)
        val items = getItemsWithCompletionUseCase.invoke(startDate, endDate).getOrThrow()
        val moods = getMoodUseCase.invoke(startDate, endDate).getOrThrow()

        ItemsWithMood(
            days = days,
            checkingItemWithCompletion = items,
            mood = moods,
            pagesCount = pagesCount,
        )
    }
}
