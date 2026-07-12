package com.gitub.linkav20.habits.main.api.domain.model.main.taskslist

import com.gitub.linkav20.habits.main.api.domain.model.main.CheckingItem

sealed class TasksItem {
    data class Item(val item: CheckingItem) : TasksItem()

    data object RateDay : TasksItem()

    data object Empty : TasksItem()
}
