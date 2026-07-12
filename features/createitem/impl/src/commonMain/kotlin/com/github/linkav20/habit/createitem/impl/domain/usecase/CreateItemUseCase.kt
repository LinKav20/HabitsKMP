package com.github.linkav20.habit.createitem.impl.domain.usecase

import com.github.linkav20.habit.createitem.impl.domain.repository.CreationItemRepository
import com.github.linkav20.habits.createitem.api.model.CreationForm
import kotlinx.datetime.LocalDate

class CreateItemUseCase(
    private val repository: CreationItemRepository,
) {

    suspend fun invoke(
        creationForm: CreationForm,
        creationDay: LocalDate,
    ): Result<Unit> {
        return repository.createCheckingItem(
            creationForm = creationForm,
            creationDay = creationDay,
        )
    }
}
