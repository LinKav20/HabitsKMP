package com.github.linkav20.habits.habit.impl.domain.usecase

import com.github.linkav20.habits.habit.api.list.HabitListItem
import com.github.linkav20.habits.habit.impl.domain.model.HabitListUpdate

internal class CalculateHabitListUpdateUseCase {
    operator fun invoke(
        previous: List<HabitListItem>?,
        current: List<HabitListItem>,
    ): HabitListUpdate? {
        if (previous == null) return HabitListUpdate.Initial(current)
        if (previous == current) return null

        val previousById = previous.associateBy { it.id }
        val updatedItems = current.map { item ->
            previousById[item.id]?.takeIf { it == item } ?: item
        }
        return HabitListUpdate.Changed(updatedItems)
    }
}
