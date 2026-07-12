package com.gitub.linkav20.habits.main.impl.domain.usecase

import com.github.linkav20.habits.core.utils.Time
import com.github.linkav20.habits.main.impl.resources.Res
import com.gitub.linkav20.habits.main.api.domain.model.main.CheckingItemWithCompletion
import com.gitub.linkav20.habits.main.impl.domain.repository.ItemsRepository
import kotlinx.datetime.LocalDate

class GetItemsWithCompletionUseCase(
    private val itemsRepository: ItemsRepository,
) {

    suspend fun invoke(
        startDate: LocalDate,
        endDate: LocalDate,
    ): Result<List<CheckingItemWithCompletion>> {
        val items = itemsRepository.getItems(startDate, endDate)
        return items.fold(
            onSuccess = { checkingItems ->
                val a = checkingItems.map {
                    val completionResult = itemsRepository.getCompletionForItem(
                        item = it,
                        startDate = startDate,
                        endDate = endDate,
                    )
                    val completion = if (completionResult.isSuccess) {
                        completionResult.getOrNull() ?: getEmptyCompletionForInterval(startDate, endDate)
                    } else {
                        getEmptyCompletionForInterval(startDate, endDate)
                    }
                    CheckingItemWithCompletion(
                        checkingItem = it,
                        completion = completion,
                    )
                }
                Result.success(a)
            },
            onFailure = {
                Result.failure(Throwable("Error to get items")) // todo
            },
        )
    }

    private fun getEmptyCompletionForInterval(
        startDate: LocalDate,
        endDate: LocalDate,
    ): List<Pair<LocalDate, Boolean?>> {
        val interval = Time.getDateRangeInclusive(startDate, endDate)
        return interval.map { date -> date to null }
    }
}
