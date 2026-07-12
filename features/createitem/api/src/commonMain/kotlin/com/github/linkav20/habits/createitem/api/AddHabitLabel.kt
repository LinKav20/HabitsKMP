package com.github.linkav20.habits.createitem.api

sealed interface AddHabitLabel {
    data object ItemSuccessfullyCreated : AddHabitLabel
    data object ItemFailedToCreateCreated : AddHabitLabel
    data object CreateTodoStartDateShouldBeEarlierError : AddHabitLabel
    data object CreateTodoRepeatPeriodShouldBeNotEmptyError : AddHabitLabel
}
