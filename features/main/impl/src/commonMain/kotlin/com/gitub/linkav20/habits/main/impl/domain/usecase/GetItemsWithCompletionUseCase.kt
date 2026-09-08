package com.gitub.linkav20.habits.main.impl.domain.usecase

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
                    val completion = completionResult.getOrDefault(emptyMap())
                    CheckingItemWithCompletion(
                        checkingItem = it,
                        completion = completion,
                    )
                }
                Result.success(a)
            },
            onFailure = { Result.failure(it) },
        )
    }

}
