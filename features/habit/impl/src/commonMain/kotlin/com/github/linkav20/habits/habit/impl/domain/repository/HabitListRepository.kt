package com.github.linkav20.habits.habit.impl.domain.repository

import com.github.linkav20.habits.habit.api.list.HabitListItem
import kotlinx.coroutines.flow.Flow

internal interface HabitListRepository {
    fun observeAll(): Flow<List<HabitListItem>>
}
