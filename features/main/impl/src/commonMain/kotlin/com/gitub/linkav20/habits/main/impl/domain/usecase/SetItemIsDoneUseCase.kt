package com.gitub.linkav20.habits.main.impl.domain.usecase

import com.gitub.linkav20.habits.main.api.domain.model.main.CheckingItem
import com.gitub.linkav20.habits.main.impl.domain.repository.ItemsRepository
import kotlinx.datetime.LocalDate

class SetItemIsDoneUseCase(
    private val itemsRepository: ItemsRepository,
) {

    suspend fun invoke(
        item: CheckingItem,
        date: LocalDate,
        isDone: Boolean?,
    ) {
        itemsRepository.setItemCompletion(
            item = item,
            date = date,
            isDone = isDone,
        )
    }
}
