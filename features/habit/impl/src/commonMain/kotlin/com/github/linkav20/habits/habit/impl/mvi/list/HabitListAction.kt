package com.github.linkav20.habits.habit.impl.mvi.list

internal sealed interface HabitListAction {
    data object Init : HabitListAction
}
