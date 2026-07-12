package com.github.linkav20.habit.createitem.impl.domain.repository

import com.github.linkav20.habits.createitem.api.model.CreationForm
import kotlinx.datetime.LocalDate

interface CreationItemRepository {
    suspend fun createCheckingItem(
        creationForm: CreationForm,
        creationDay: LocalDate,
    ): Result<Unit>
}
