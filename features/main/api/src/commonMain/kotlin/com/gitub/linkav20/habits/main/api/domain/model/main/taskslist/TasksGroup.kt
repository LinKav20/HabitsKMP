package com.gitub.linkav20.habits.main.api.domain.model.main.taskslist

import kotlinx.datetime.LocalDate

sealed class TasksGroup {
    data object Today : TasksGroup()
    data object Tomorrow : TasksGroup()
    data object Later : TasksGroup()
    data class Date(val date: LocalDate) : TasksGroup()
}
