package com.github.linkav20.habits.habit.impl.domain.usecase

import com.github.linkav20.habits.habit.impl.domain.repository.HabitListRepository

internal class ObserveHabitsUseCase(
    private val repository: HabitListRepository,
) {
    operator fun invoke() = repository.observeAll()
}
