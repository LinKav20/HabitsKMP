package com.gitub.linkav20.habits.main.api.main

import com.github.linkav20.habits.core.mvi.navigation.api.NavStore
import com.github.linkav20.habits.core.utils.Time
import com.gitub.linkav20.habits.main.api.domain.model.main.CheckingItem
import com.gitub.linkav20.habits.main.api.domain.model.main.ItemsWithMood
import com.gitub.linkav20.habits.main.api.domain.model.main.taskslist.TasksGroup
import com.gitub.linkav20.habits.main.api.domain.model.main.taskslist.TasksItem
import kotlinx.datetime.DateTimeUnit.Companion.DAY
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

interface MainStore : NavStore<MainIntent, MainStore.State, Any, MainNav> {
    data class State(
        val today: LocalDate = Time.now(),
        val selectedDate: LocalDate = Time.now(),
        val itemsWithMood: ItemsWithMood = ItemsWithMood(
            days = emptyList(),
            checkingItemWithCompletion = emptyList(),
            mood = emptyList(),
            pagesCount = 0,
        ),
        val currentPage: Int = 0,
        val tasks: Map<LocalDate, List<Pair<CheckingItem, Boolean?>>> = emptyMap(),
    ) {
        val pagesCount = itemsWithMood.pagesCount
        val groupedTasks: Map<TasksGroup, List<Pair<TasksItem, Pair<LocalDate, Boolean?>>>>
            get() {
                val comparator = compareBy<Pair<TasksItem, Pair<LocalDate, Boolean?>>> { (_, dateAndDone) ->
                    when (dateAndDone.second) {
                        true -> 1
                        false, null -> 0
                    }
                }.thenBy { (_, dateAndDone) ->
                    dateAndDone.first
                }

                val tomorrow = today.plus(1, DAY)

                val todayTasks = tasks.filterKeys { it == today }
                    .flatMap { (date, items) ->
                        items.map { item -> TasksItem.Item(item.first) to (date to item.second) }
                    }.sortedWith(comparator)
                    .let { allItems ->
                        val completed = allItems.filter { it.second.second == true }.sortedWith(comparator)
                        val notCompleted = allItems.filter { it.second.second != true }.sortedWith(comparator)
                        val rateItem =
                            if (selectedDate == today && itemsWithMood.mood.find { it.date == selectedDate }?.rate == null) {
                                listOf(TasksItem.RateDay to (selectedDate to null))
                            } else emptyList()

                        notCompleted + rateItem + completed
                    }
                    .let { items ->
                        if (selectedDate == today && items.none { it.first is TasksItem.Item }) {
                            listOf(TasksItem.Empty to (today to null)) + items
                        } else items
                    }

                val tomorrowTasks = tasks.filterKeys { it == tomorrow }
                    .flatMap { (date, items) ->
                        items.map { item -> TasksItem.Item(item.first) to (date to item.second) }
                    }.sortedWith(comparator)
                    .let { items ->
                        if (selectedDate == tomorrow && items.isEmpty()) {
                            listOf(TasksItem.Empty to (today to null))
                        } else items
                    }

                val otherTasks = tasks.filterKeys { it != today && it != tomorrow }
                    .flatMap { (date, items) ->
                        items.map { item -> TasksItem.Item(item.first) to (date to item.second) }
                    }.sortedWith(comparator)
                    .let { allItems ->
                        val completed = allItems.filter { it.second.second == true }.sortedWith(comparator)
                        val notCompleted = allItems.filter { it.second.second != true }.sortedWith(comparator)
                        val rateItem =
                            if (selectedDate < today && itemsWithMood.mood.find { it.date == selectedDate }?.rate == null) {
                                listOf(TasksItem.RateDay to (selectedDate to null))
                            } else emptyList()

                        notCompleted + rateItem + completed
                    }
                    .let { items ->
                        if (selectedDate != today && selectedDate != tomorrow  && items.none { it.first is TasksItem.Item }) {
                            listOf(TasksItem.Empty to (selectedDate to null)) + items
                        } else items
                    }

                return buildMap {
                    if (todayTasks.isNotEmpty()) {
                        put(TasksGroup.Today, todayTasks)
                    }
                    if (tomorrowTasks.isNotEmpty()) {
                        put(TasksGroup.Tomorrow, tomorrowTasks)
                    }
                    if (otherTasks.isNotEmpty()) {
                        put(
                            if (selectedDate != today) TasksGroup.Date(selectedDate) else TasksGroup.Later,
                            otherTasks,
                        )
                    }
                }
            }

        fun getDays(page: Int) = itemsWithMood.getDays(page)

        fun getChartPoints(page: Int) = itemsWithMood.getChartPoints(page)
    }

    interface Factory {
        fun create(): MainStore
    }
}
