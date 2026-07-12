package com.gitub.linkav20.habits.main.impl.data

import com.github.linkav20.habits.core.utils.Time
import com.github.linkav20.habits.database.habits.dao.CompletionDao
import com.github.linkav20.habits.database.habits.dao.HabitsDao
import com.github.linkav20.habits.database.habits.dao.TodoDao
import com.github.linkav20.habits.database.habits.model.ItemCompletion
import com.gitub.linkav20.habits.main.api.domain.model.main.CheckingItem
import com.gitub.linkav20.habits.main.api.domain.model.main.TodoItem
import com.gitub.linkav20.habits.main.impl.data.mapper.toDomain
import com.gitub.linkav20.habits.main.impl.data.mapper.toTodoDatabase
import com.gitub.linkav20.habits.main.impl.domain.repository.ItemsRepository
import kotlinx.datetime.LocalDate

class ItemsRepositoryImpl(
    val habitsDao: HabitsDao,
    val todoDao: TodoDao,
    val completionDao: CompletionDao,
) : ItemsRepository {

    override suspend fun getItems(
        startDate: LocalDate,
        endDate: LocalDate,
    ): Result<List<CheckingItem>> =
        runCatching {
            val habits = habitsDao.getActiveHabitsOnDate(startDate.toString()).map {
                it.toDomain()
            }
            val todos = todoDao.getActiveTodosOnDate().map {
                it.toDomain()
            }
            habits + todos
        }

    override suspend fun getCompletionForItem(
        item: CheckingItem,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Result<List<Pair<LocalDate, Boolean?>>> {
        return runCatching {
            val interval = Time.getDateRangeInclusive(startDate, endDate)
            val completion = completionDao.getCompletionsForHabit(
                itemId = item.id,
                startDate = startDate.toString(),
                endDate = endDate.toString(),
            ).associate { it.date to it.isCompleted }
            interval.map { date ->
                date to completion[date]
            }
        }
    }

    override suspend fun setItemCompletion(
        item: CheckingItem,
        date: LocalDate,
        isDone: Boolean?
    ) {
        isDone?.let {
            completionDao.setItemCompletion(
                ItemCompletion(
                    itemId = item.id,
                    date = date,
                    isCompleted = it,
                ),
            )
        } ?: completionDao.deleteCompletionByIdIdAndDate(
            id = item.id,
            date = date.toString(),
        )
    }

    override suspend fun updateTodo(item: TodoItem) {
        todoDao.insertTodo(item.toTodoDatabase())
    }
}
