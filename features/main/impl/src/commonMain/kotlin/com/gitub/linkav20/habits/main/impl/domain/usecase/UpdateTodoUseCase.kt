package com.gitub.linkav20.habits.main.impl.domain.usecase

import com.gitub.linkav20.habits.main.api.domain.model.main.TodoItem
import com.gitub.linkav20.habits.main.impl.domain.repository.ItemsRepository

class UpdateTodoUseCase(
    private val repository: ItemsRepository,
) {

    suspend fun invoke(item: TodoItem) {
        repository.updateTodo(item)
    }
}
