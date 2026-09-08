package com.gitub.linkav20.habits.main.impl.mvi.main

import com.github.linkav20.habits.core.utils.Time
import com.gitub.linkav20.habits.main.api.domain.model.main.CheckingItem
import com.gitub.linkav20.habits.main.api.domain.model.main.CheckingItemWithCompletion
import com.gitub.linkav20.habits.main.api.domain.model.main.HabitItem
import com.gitub.linkav20.habits.main.api.domain.model.main.MainData
import com.gitub.linkav20.habits.main.api.domain.model.main.TodoItem
import kotlinx.datetime.DateTimeUnit.Companion.DAY
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlin.math.abs

internal data class CalendarDateRange(
    val startDate: LocalDate,
    val endDate: LocalDate,
)

/**
 * Weeks the screen has to load, split by urgency.
 *
 * [requiredWeekStarts] are the weeks the user can already see or is about to see: the visible
 * week, its composed neighbours and the pinned weeks. They must be loaded on every settle,
 * otherwise a composed page stays empty with nothing scheduled to fill it.
 * [prefetchWeekStarts] are the buffer weeks loaded ahead of the scroll and may be skipped.
 */
internal data class CalendarLoadPlan(
    val requiredWeekStarts: Set<LocalDate>,
    val prefetchWeekStarts: Set<LocalDate>,
)

internal fun calendarWeekStart(date: LocalDate): LocalDate = Time.getWeekWithDay(date).first()

internal fun calendarWeekStartForPage(
    page: Int,
    anchorWeekStart: LocalDate,
): LocalDate = anchorWeekStart.plus(
    (page.toLong() - CALENDAR_ANCHOR_PAGE) * CALENDAR_DAYS_IN_WEEK,
    DAY,
)

internal fun calendarPageForWeekStart(
    weekStart: LocalDate,
    anchorWeekStart: LocalDate,
): Int {
    val normalizedWeekStart = calendarWeekStart(weekStart)
    val weekOffset = anchorWeekStart.daysUntil(normalizedWeekStart) / CALENDAR_DAYS_IN_WEEK
    return (CALENDAR_ANCHOR_PAGE.toLong() + weekOffset)
        .coerceIn(0L, CALENDAR_PAGE_COUNT.toLong() - 1)
        .toInt()
}

internal fun calendarWeekDates(weekStart: LocalDate): List<LocalDate> =
    List(CALENDAR_DAYS_IN_WEEK) { day -> weekStart.plus(day, DAY) }

internal fun calendarWeekStarts(
    startDate: LocalDate,
    endDate: LocalDate,
): Set<LocalDate> = buildSet {
    var weekStart = calendarWeekStart(startDate)
    while (weekStart <= endDate) {
        add(weekStart)
        weekStart = weekStart.plus(CALENDAR_DAYS_IN_WEEK, DAY)
    }
}

internal fun calendarLoadPlan(
    visibleWeekStart: LocalDate,
    cachedWeekStarts: Set<LocalDate>,
    today: LocalDate,
    selectedDate: LocalDate,
): CalendarLoadPlan {
    val normalizedVisibleWeekStart = calendarWeekStart(visibleWeekStart)
    val requiredWeekStarts = calendarPinnedWeekStarts(today, selectedDate)
        .plus(weeksAround(normalizedVisibleWeekStart, CALENDAR_COMPOSED_NEIGHBOUR_WEEKS))
        .filterNotTo(mutableSetOf()) { it in cachedWeekStarts }

    val prefetchWeekStarts = mutableSetOf<LocalDate>()
    val cachedRange = cachedContiguousRangeContaining(
        weekStart = normalizedVisibleWeekStart,
        cachedWeekStarts = cachedWeekStarts,
    )
    if (cachedRange == null) {
        prefetchWeekStarts += weeksAround(normalizedVisibleWeekStart, CALENDAR_BUFFER_WEEKS)
    } else {
        val (cachedStart, cachedEnd) = cachedRange
        if (weeksBetween(cachedStart, normalizedVisibleWeekStart) <= CALENDAR_PREFETCH_DISTANCE_WEEKS) {
            (1..CALENDAR_BUFFER_WEEKS).mapTo(prefetchWeekStarts) { weekOffset ->
                normalizedVisibleWeekStart.minus(weekOffset * CALENDAR_DAYS_IN_WEEK, DAY)
            }
        }
        if (weeksBetween(normalizedVisibleWeekStart, cachedEnd) <= CALENDAR_PREFETCH_DISTANCE_WEEKS) {
            (1..CALENDAR_BUFFER_WEEKS).mapTo(prefetchWeekStarts) { weekOffset ->
                normalizedVisibleWeekStart.plus(weekOffset * CALENDAR_DAYS_IN_WEEK, DAY)
            }
        }
    }
    prefetchWeekStarts.removeAll(cachedWeekStarts)
    prefetchWeekStarts.removeAll(requiredWeekStarts)

    return CalendarLoadPlan(
        requiredWeekStarts = requiredWeekStarts,
        prefetchWeekStarts = prefetchWeekStarts,
    )
}

/**
 * Weeks that must survive cache eviction: the buffer window around the visible week plus the
 * pinned weeks. Also bounds the set of weeks worth remembering as failed.
 */
internal fun calendarRequiredWeekStarts(
    visibleWeekStart: LocalDate,
    today: LocalDate,
    selectedDate: LocalDate,
): Set<LocalDate> = weeksAround(calendarWeekStart(visibleWeekStart), CALENDAR_BUFFER_WEEKS) +
    calendarPinnedWeekStarts(today, selectedDate)

internal fun calendarWeeksToRetain(
    loadedWeekStarts: Set<LocalDate>,
    visibleWeekStart: LocalDate,
    today: LocalDate,
    selectedDate: LocalDate,
): Set<LocalDate> {
    val normalizedVisibleWeekStart = calendarWeekStart(visibleWeekStart)
    val requiredWeeks = calendarRequiredWeekStarts(
        visibleWeekStart = normalizedVisibleWeekStart,
        today = today,
        selectedDate = selectedDate,
    )
    val requiredLoadedWeeks = loadedWeekStarts.filterTo(mutableSetOf()) { it in requiredWeeks }
    val remainingCapacity = (CALENDAR_MAX_CACHED_WEEKS - requiredLoadedWeeks.size)
        .coerceAtLeast(0)
    val nearestExtraWeeks = (loadedWeekStarts - requiredLoadedWeeks)
        .sortedWith(
            compareBy<LocalDate> {
                abs(normalizedVisibleWeekStart.daysUntil(it) / CALENDAR_DAYS_IN_WEEK)
            }.thenBy { it },
        )
        .take(remainingCapacity)

    return requiredLoadedWeeks + nearestExtraWeeks
}

internal fun MainData.mergeCalendarData(loaded: MainData): MainData {
    val currentItems = items.associateBy { it.checkingItem.id }
    val loadedItems = loaded.items.associateBy { it.checkingItem.id }
    val itemIds = currentItems.keys + loadedItems.keys

    return MainData(
        startDate = minOf(startDate, loaded.startDate),
        endDate = maxOf(endDate, loaded.endDate),
        items = itemIds.map { id ->
            val current = currentItems[id]
            val added = loadedItems[id]
            CheckingItemWithCompletion(
                checkingItem = added?.checkingItem ?: requireNotNull(current).checkingItem,
                completion = current?.completion.orEmpty() + added?.completion.orEmpty(),
            )
        },
        moods = moods + loaded.moods,
    )
}

internal fun MainData.trimCalendarData(retainedWeekStarts: Set<LocalDate>): MainData {
    require(retainedWeekStarts.isNotEmpty()) { "Cannot trim calendar data to an empty week set" }
    val retainedDates = retainedWeekStarts
        .flatMapTo(mutableSetOf(), ::calendarWeekDates)
    val retainedStartDate = retainedWeekStarts.min()
    val retainedEndDate = retainedWeekStarts.max().plus(CALENDAR_DAYS_IN_WEEK - 1, DAY)

    return MainData(
        startDate = retainedStartDate,
        endDate = retainedEndDate,
        items = items.mapNotNull { itemWithCompletion ->
            val retainedCompletion = itemWithCompletion.completion.filterKeys(retainedDates::contains)
            itemWithCompletion
                .takeIf {
                    retainedCompletion.isNotEmpty() ||
                        it.checkingItem.isRelevantForAny(retainedDates)
                }
                ?.copy(completion = retainedCompletion)
        },
        moods = moods.filterKeys(retainedDates::contains),
    )
}

private fun weeksAround(
    weekStart: LocalDate,
    radiusWeeks: Int,
): Set<LocalDate> = (-radiusWeeks..radiusWeeks).mapTo(mutableSetOf()) { weekOffset ->
    weekStart.plus(weekOffset * CALENDAR_DAYS_IN_WEEK, DAY)
}

private fun weeksBetween(
    from: LocalDate,
    to: LocalDate,
): Int = from.daysUntil(to) / CALENDAR_DAYS_IN_WEEK

private fun calendarPinnedWeekStarts(
    today: LocalDate,
    selectedDate: LocalDate,
): Set<LocalDate> = buildSet {
    add(calendarWeekStart(today))
    add(calendarWeekStart(selectedDate))
    if (selectedDate == today) {
        add(calendarWeekStart(today.plus(TASK_PREVIEW_DAYS - 1, DAY)))
    }
}

private fun cachedContiguousRangeContaining(
    weekStart: LocalDate,
    cachedWeekStarts: Set<LocalDate>,
): Pair<LocalDate, LocalDate>? {
    if (weekStart !in cachedWeekStarts) return null

    var start = weekStart
    var previous = start.minus(CALENDAR_DAYS_IN_WEEK, DAY)
    while (previous in cachedWeekStarts) {
        start = previous
        previous = start.minus(CALENDAR_DAYS_IN_WEEK, DAY)
    }

    var end = weekStart
    var next = end.plus(CALENDAR_DAYS_IN_WEEK, DAY)
    while (next in cachedWeekStarts) {
        end = next
        next = end.plus(CALENDAR_DAYS_IN_WEEK, DAY)
    }

    return start to end
}

internal fun calendarDateRanges(weeksToLoad: Set<LocalDate>): List<CalendarDateRange> {
    val sortedWeeks = weeksToLoad.sorted()
    if (sortedWeeks.isEmpty()) return emptyList()

    val ranges = mutableListOf<CalendarDateRange>()
    var rangeStart = sortedWeeks.first()
    var previousWeek = rangeStart
    sortedWeeks.drop(1).forEach { weekStart ->
        if (weekStart != previousWeek.plus(CALENDAR_DAYS_IN_WEEK, DAY)) {
            ranges += CalendarDateRange(
                startDate = rangeStart,
                endDate = previousWeek.plus(CALENDAR_DAYS_IN_WEEK - 1, DAY),
            )
            rangeStart = weekStart
        }
        previousWeek = weekStart
    }
    ranges += CalendarDateRange(
        startDate = rangeStart,
        endDate = previousWeek.plus(CALENDAR_DAYS_IN_WEEK - 1, DAY),
    )
    return ranges
}

private fun CheckingItem.isRelevantForAny(dates: Set<LocalDate>): Boolean = when (this) {
    is HabitItem -> dates.any { date ->
        dateInHabitRange(date) && date.dayOfWeek in daysForHabit
    }
    is TodoItem -> dates.any(::isPlanned)
    else -> false
}

internal const val CALENDAR_PAGE_COUNT = Int.MAX_VALUE
internal const val CALENDAR_ANCHOR_PAGE = Int.MAX_VALUE / 2
internal const val CALENDAR_MAX_CACHED_WEEKS = 12
internal const val CALENDAR_DAYS_IN_WEEK = 7

/**
 * How far from the visible week the pager can already have pages composed. Weeks inside this
 * radius are always loaded, so a composed page never shows a load state nobody is fulfilling.
 */
private const val CALENDAR_COMPOSED_NEIGHBOUR_WEEKS = 2
private const val CALENDAR_PREFETCH_DISTANCE_WEEKS = 1
private const val CALENDAR_BUFFER_WEEKS = 4
private const val TASK_PREVIEW_DAYS = 5
