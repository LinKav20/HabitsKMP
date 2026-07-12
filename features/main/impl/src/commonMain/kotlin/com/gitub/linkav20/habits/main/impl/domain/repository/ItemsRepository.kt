package com.gitub.linkav20.habits.main.impl.domain.repository

import com.gitub.linkav20.habits.main.api.domain.model.main.CheckingItem
import com.gitub.linkav20.habits.main.api.domain.model.main.TodoItem
import kotlinx.datetime.LocalDate

interface ItemsRepository {

    suspend fun getItems(
        startDate: LocalDate,
        endDate: LocalDate,
    ): Result<List<CheckingItem>>

    suspend fun getCompletionForItem(
        item: CheckingItem,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Result<List<Pair<LocalDate, Boolean?>>>

    suspend fun setItemCompletion(
        item: CheckingItem,
        date: LocalDate,
        isDone: Boolean?,
    )

    suspend fun updateTodo(
        item: TodoItem,
    )
}
