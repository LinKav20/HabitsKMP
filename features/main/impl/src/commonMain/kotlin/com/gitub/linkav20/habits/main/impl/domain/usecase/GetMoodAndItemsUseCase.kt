package com.gitub.linkav20.habits.main.impl.domain.usecase

import com.github.linkav20.habits.core.utils.runCatchingCancellable
import com.gitub.linkav20.habits.main.api.domain.model.main.MainData
import kotlinx.datetime.LocalDate

class GetMoodAndItemsUseCase(
    private val getItemsWithCompletionUseCase: GetItemsWithCompletionUseCase,
    private val getMoodUseCase: GetMoodUseCase,
) {

    suspend fun invoke(
        startDate: LocalDate,
        endDate: LocalDate,
    ): Result<MainData> = runCatchingCancellable {
        val items = getItemsWithCompletionUseCase.invoke(startDate, endDate).getOrThrow()
        val moods = getMoodUseCase.invoke(startDate, endDate).getOrThrow()

        MainData(
            startDate = startDate,
            endDate = endDate,
            items = items,
            moods = moods.mapNotNull { mood ->
                mood.rate?.let { mood.date to it }
            }.toMap(),
        )
    }
}
