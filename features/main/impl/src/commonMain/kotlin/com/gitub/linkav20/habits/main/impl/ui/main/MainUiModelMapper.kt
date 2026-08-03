package com.gitub.linkav20.habits.main.impl.ui.main

import com.github.linkav20.habits.core.uikit.components.charts.line.LineChartPoint
import com.github.linkav20.habits.core.utils.Time
import com.gitub.linkav20.habits.main.api.domain.model.main.CheckingItem
import com.gitub.linkav20.habits.main.api.domain.model.main.HabitItem
import com.gitub.linkav20.habits.main.api.domain.model.main.TodoItem
import com.gitub.linkav20.habits.main.api.domain.model.main.taskslist.TasksGroup
import com.gitub.linkav20.habits.main.api.domain.model.main.taskslist.TasksItem
import com.gitub.linkav20.habits.main.api.main.MainStore
import kotlinx.datetime.DateTimeUnit.Companion.DAY
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

internal data class MainUiModel(
    val pages: List<WeekUiModel>,
    val groupedTasks: Map<TasksGroup, List<Pair<TasksItem, Pair<LocalDate, Boolean?>>>>,
)

internal data class WeekUiModel(
    val days: List<LocalDate>,
    val moodRates: List<Int?>,
    val chartPoints: List<LineChartPoint>,
    val habits: List<HabitRowUiModel>,
)

internal data class HabitRowUiModel(
    val item: HabitItem,
    val completion: List<Pair<LocalDate, Boolean?>>,
)

/** The only place where canonical screen data is expanded into date intervals. */
internal fun MainStore.State.toUiModel(): MainUiModel {
    val data = data ?: return MainUiModel(emptyList(), emptyMap())
    val pages = Time.getDateRangeInclusive(data.startDate, data.endDate)
        .chunked(DAYS_IN_WEEK)
        .map { days ->
            val moodRates = days.map(data.moods::get)
            WeekUiModel(
                days = days,
                moodRates = moodRates,
                chartPoints = moodRates.toChartPoints(days, today),
                habits = data.items.mapNotNull { itemWithCompletion ->
                    val habit = itemWithCompletion.checkingItem as? HabitItem ?: return@mapNotNull null
                    HabitRowUiModel(
                        item = habit,
                        completion = days.map { date -> date to itemWithCompletion.completion[date] },
                    )
                },
            )
        }
    return MainUiModel(pages = pages, groupedTasks = buildTaskGroups())
}

private fun MainStore.State.buildTaskGroups():
    Map<TasksGroup, List<Pair<TasksItem, Pair<LocalDate, Boolean?>>>> {
    val data = data ?: return emptyMap()
    val tomorrow = today.plus(1, DAY)
    val dates = if (selectedDate == today) {
        List(TASK_PREVIEW_DAYS) { today.plus(it, DAY) }
    } else {
        listOf(selectedDate)
    }
    val tasks = dates.associateWith { date ->
        data.items.mapNotNull { entry ->
            val item = entry.checkingItem
            item.takeIf { it.isPlanned(date) }?.let { it to (entry.completion[date] ?: false) }
        }
    }
    val comparator = compareBy<Pair<TasksItem, Pair<LocalDate, Boolean?>>> {
        if (it.second.second == true) 1 else 0
    }.thenBy { it.second.first }

    fun itemsFor(filter: (LocalDate) -> Boolean) = tasks.filterKeys(filter)
        .flatMap { (date, items) ->
            items.map { (item, done) -> TasksItem.Item(item) to (date to done) }
        }
        .sortedWith(comparator)

    fun decorate(
        items: List<Pair<TasksItem, Pair<LocalDate, Boolean?>>>,
        rateDate: LocalDate?,
        emptyDate: LocalDate?,
    ): List<Pair<TasksItem, Pair<LocalDate, Boolean?>>> {
        val rate = rateDate?.takeIf { data.moods[it] == null }
            ?.let { listOf(TasksItem.RateDay to (it to null)) }.orEmpty()
        val empty = emptyDate?.takeIf { items.none { entry -> entry.first is TasksItem.Item } }
            ?.let { listOf(TasksItem.Empty to (it to null)) }.orEmpty()
        return empty + items.filter { it.second.second != true } + rate +
            items.filter { it.second.second == true }
    }

    val todayTasks = decorate(
        itemsFor { it == today },
        selectedDate.takeIf { it == today },
        today.takeIf { selectedDate == today },
    )
    val tomorrowTasks = decorate(
        itemsFor { it == tomorrow },
        null,
        tomorrow.takeIf { selectedDate == tomorrow },
    )
    val otherTasks = decorate(
        itemsFor { it != today && it != tomorrow },
        selectedDate.takeIf { it < today },
        selectedDate.takeIf { it != today && it != tomorrow },
    )

    return buildMap {
        if (todayTasks.isNotEmpty()) put(TasksGroup.Today, todayTasks)
        if (tomorrowTasks.isNotEmpty()) put(TasksGroup.Tomorrow, tomorrowTasks)
        if (otherTasks.isNotEmpty()) {
            put(if (selectedDate == today) TasksGroup.Later else TasksGroup.Date(selectedDate), otherTasks)
        }
    }
}

private fun CheckingItem.isPlanned(date: LocalDate): Boolean = when (this) {
    is HabitItem -> dateInHabitRange(date) && date.dayOfWeek in daysForHabit
    is TodoItem -> isPlanned(date)
    else -> false
}

private fun List<Int?>.toChartPoints(days: List<LocalDate>, today: LocalDate) =
    mapIndexedNotNull { index, rate ->
        val date = days[index]
        if (date < today || date == today && rate != null) {
            LineChartPoint(x = date.dayOfWeek.ordinal + 0.5f, y = rate?.toFloat() ?: 0f)
        } else {
            null
        }
    }

private const val DAYS_IN_WEEK = 7
private const val TASK_PREVIEW_DAYS = 5
