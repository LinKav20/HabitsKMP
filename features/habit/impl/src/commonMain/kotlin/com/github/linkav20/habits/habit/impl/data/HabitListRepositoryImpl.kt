package com.github.linkav20.habits.habit.impl.data

import com.github.linkav20.habits.database.habits.dao.HabitsDao
import com.github.linkav20.habits.habit.api.list.HabitListItem
import com.github.linkav20.habits.habit.impl.domain.repository.HabitListRepository
import kotlinx.coroutines.flow.map

internal class HabitListRepositoryImpl(private val dao: HabitsDao) : HabitListRepository {
    override fun observeAll() = dao.observeAllHabits().map { habits ->
        habits.map { HabitListItem(id = it.uid, title = it.title, emoji = it.emoji) }
    }
}
