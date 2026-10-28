package com.github.linkav20.habit.createitem.impl.mvi

internal sealed interface AddHabitAction {
    data object Init : AddHabitAction
}
