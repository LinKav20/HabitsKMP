package com.github.linkav20.habit.createitem.impl.data

import com.github.linkav20.habit.createitem.impl.data.mapper.toTodoDatabase
import com.github.linkav20.habit.createitem.impl.domain.repository.CreationItemRepository
import com.github.linkav20.habits.createitem.api.model.AddHabitForm
import com.github.linkav20.habits.createitem.api.model.AddTodoForm
import com.github.linkav20.habits.createitem.api.model.CreationForm
import com.github.linkav20.habits.database.habits.dao.HabitsDao
import com.github.linkav20.habits.database.habits.dao.TodoDao
import kotlinx.datetime.LocalDate

class CreationItemRepositoryImpl(
    val habitsDao: HabitsDao,
    val todoDao: TodoDao,
) : CreationItemRepository {


    override suspend fun createCheckingItem(
        creationForm: CreationForm,
        creationDay: LocalDate,
    ): Result<Unit> = runCatching {
        when (creationForm) {
            is AddHabitForm -> {
                habitsDao.insertHabit(creationForm.toTodoDatabase(creationDay))
            }

            is AddTodoForm -> {
                todoDao.insertTodo(creationForm.toTodoDatabase(creationDay))
            }
        }
    }
}
