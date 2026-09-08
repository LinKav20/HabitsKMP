package com.gitub.linkav20.habits.main.impl.ui.main

import com.github.linkav20.habits.core.uikit.components.charts.line.LineChartPoint
import com.gitub.linkav20.habits.main.api.domain.model.main.CheckingItem
import com.gitub.linkav20.habits.main.api.domain.model.main.HabitItem
import com.gitub.linkav20.habits.main.api.domain.model.main.TodoItem
import com.gitub.linkav20.habits.main.api.domain.model.main.taskslist.TasksGroup
import com.gitub.linkav20.habits.main.api.domain.model.main.taskslist.TasksItem
import com.gitub.linkav20.habits.main.api.main.MainStore
import com.gitub.linkav20.habits.main.impl.mvi.main.calendarWeekDates
import com.gitub.linkav20.habits.main.impl.mvi.main.calendarWeekStart
import kotlinx.datetime.DateTimeUnit.Companion.DAY
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

internal data class MainUiModel(
    val groupedTasks: Map<TasksGroup, List<Pair<TasksItem, Pair<LocalDate, Boolean?>>>>,
)

/**
 * Whether the data behind a calendar week is actually available.
 *
 * The pager can show any week, so "no habits" and "no mood" must never be rendered for a week
 * that simply has not been read from the database yet.
 */
internal enum class WeekLoadState {
    LOADED,
    LOADING,
    FAILED,
}

internal data class WeekUiModel(
    val weekStart: LocalDate,
    val days: List<LocalDate>,
    val moodRates: List<Int?>,
    val chartPoints: List<LineChartPoint>,
    val habits: List<HabitRowUiModel>,
    val loadState: WeekLoadState,
)

internal data class HabitRowUiModel(
    val item: HabitItem,
    val completion: List<Pair<LocalDate, Boolean?>>,
)

internal fun MainStore.State.toUiModel(): MainUiModel {
    return MainUiModel(groupedTasks = buildTaskGroups())
}

internal fun MainStore.State.toWeekUiModel(weekStart: LocalDate): WeekUiModel {
    val normalizedWeekStart = calendarWeekStart(weekStart)
    val days = calendarWeekDates(normalizedWeekStart)
    val loadState = when {
        normalizedWeekStart in cachedWeekStarts -> WeekLoadState.LOADED
        normalizedWeekStart in failedWeekStarts -> WeekLoadState.FAILED
        else -> WeekLoadState.LOADING
    }
    val loadedData = data.takeIf { loadState == WeekLoadState.LOADED }
    val moodRates = days.map { date -> loadedData?.moods?.get(date) }
    return WeekUiModel(
        weekStart = normalizedWeekStart,
        days = days,
        moodRates = moodRates,
        // A missing rate only means "not rated" once the week is actually loaded; otherwise the
        // chart would draw a full week of zeroes, which reads as the lowest possible mood.
        chartPoints = if (loadState == WeekLoadState.LOADED) {
            moodRates.toChartPoints(days, today)
        } else {
            emptyList()
        },
        habits = loadedData?.items.orEmpty().mapNotNull { itemWithCompletion ->
            val habit = itemWithCompletion.checkingItem as? HabitItem ?: return@mapNotNull null
            HabitRowUiModel(
                item = habit,
                completion = days.map { date -> date to itemWithCompletion.completion[date] },
            )
        },
        loadState = loadState,
    )
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

private const val TASK_PREVIEW_DAYS = 5
