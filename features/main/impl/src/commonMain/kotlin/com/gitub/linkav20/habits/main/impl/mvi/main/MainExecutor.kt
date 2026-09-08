package com.gitub.linkav20.habits.main.impl.mvi.main

import kotlinx.coroutines.channels.SendChannel
import com.github.linkav20.habits.core.mvi.navigation.impl.NavigationExecutor
import com.github.linkav20.habits.core.utils.AppVisibility
import com.github.linkav20.habits.core.utils.Time
import com.github.linkav20.habits.database.habits.model.todo.TodoRepeatRule
import com.gitub.linkav20.habits.main.api.main.MainIntent
import com.gitub.linkav20.habits.main.api.main.MainNav
import com.gitub.linkav20.habits.main.api.main.MainStore
import com.gitub.linkav20.habits.main.api.domain.model.main.CheckingItem
import com.gitub.linkav20.habits.main.api.domain.model.main.HabitItem
import com.gitub.linkav20.habits.main.api.domain.model.main.MainData
import com.gitub.linkav20.habits.main.api.domain.model.main.TodoItem
import com.gitub.linkav20.habits.main.impl.domain.usecase.GetMoodAndItemsUseCase
import com.gitub.linkav20.habits.main.impl.domain.usecase.SetItemIsDoneUseCase
import com.gitub.linkav20.habits.main.impl.domain.usecase.SetMoodUseCase
import com.gitub.linkav20.habits.main.impl.domain.usecase.UpdateTodoUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

internal class MainExecutor(
    channel: SendChannel<MainNav>,
    private val getMoodAndItemsUseCase: GetMoodAndItemsUseCase,
    private val setItemDoneUseCase: SetItemIsDoneUseCase,
    private val setMoodUseCase: SetMoodUseCase,
    private val updateTodoUseCase: UpdateTodoUseCase,
    private val appVisibility: AppVisibility,
) :
    NavigationExecutor<MainIntent, MainAction, MainStore.State, MainMessage, Any, MainNav>(
        channel,
    ) {

    private var observeCurrentDateJob: Job? = null

    /** Loads driven by calendar scrolling. Cancelled by the next scroll, never by a refresh. */
    private var calendarWindowJob: Job? = null

    /** Full refresh after external data changes. Cancelled only by the next refresh. */
    private var refreshJob: Job? = null

    /** Initial load and its retries. */
    private var initialLoadJob: Job? = null

    private var visibleCalendarWeekStart: LocalDate? = null
    private val appVisibilityListener: AppVisibility.Listener = object : AppVisibility.Listener {
        override fun onAppGoesBackground() {
            stopObserveCurrentDate()
        }

        override fun onAppGoesForeground() {
            observeCurrentDate()
        }
    }

    override fun executeAction(action: MainAction) {
        super.executeAction(action)
        when (action) {
            MainAction.Init -> {
                appVisibility.registerListener(appVisibilityListener)
                startInitialLoad().invokeOnCompletion {
                    observeCurrentDate(forceUpdate = true)
                }
            }
        }
    }

    override fun executeIntent(intent: MainIntent) {
        super.executeIntent(intent)
        when (intent) {
            is MainIntent.ChangeSelectedDate -> {
                dispatch(
                    MainMessage.ChangeSelectedDate(
                        selectedDate = intent.date,
                    ),
                )
            }

            is MainIntent.CalendarPageSettled -> {
                loadCalendarWindow(
                    visibleWeekStart = intent.weekStart,
                    prefetch = intent.prefetch,
                )
            }

            is MainIntent.RetryCalendarWeek -> {
                loadCalendarWindow(
                    visibleWeekStart = intent.weekStart,
                    prefetch = true,
                )
            }

            is MainIntent.RetryInitialLoad -> {
                startInitialLoad()
            }

            is MainIntent.ChangeItemIsDone -> {
                val state = state()
                val data = state.data ?: return

                val updatedData = data.copy(
                    items = data.items.map { itemWithCompletion ->
                        if (itemWithCompletion.checkingItem == intent.item) {
                            val updatedCompletion = itemWithCompletion.completion.toMutableMap().apply {
                                val value = changeItemIsDone(
                                    item = itemWithCompletion.checkingItem,
                                    isDone = this[intent.date],
                                    date = intent.date,
                                )
                                if (value == null) {
                                    remove(intent.date)
                                } else {
                                    this[intent.date] = value
                                }
                            }
                            itemWithCompletion.copy(completion = updatedCompletion)
                        } else {
                            itemWithCompletion
                        }
                    },
                )

                dispatch(
                    MainMessage.UpdateCompletion(
                        data = updatedData,
                    ),
                )
            }

            is MainIntent.SetRate -> {
                val state = state()
                val data = state.data ?: return
                updateMoodInDatabase(
                    rate = intent.rate,
                    date = intent.date,
                )
                val updatedData = data.copy(
                    moods = data.moods + (intent.date to intent.rate),
                )
                dispatch(
                    MainMessage.UpdateMood(
                        data = updatedData,
                    ),
                )
            }

            is MainIntent.UpdateData -> {
                reloadCachedCalendarData()
            }
        }
    }

    private fun stopObserveCurrentDate() {
        observeCurrentDateJob?.cancel()
        observeCurrentDateJob = null
    }

    private fun observeCurrentDate(forceUpdate: Boolean = false) {
        stopObserveCurrentDate()
        observeCurrentDateJob = scope.launch {
            var shouldForceUpdate = forceUpdate
            while (true) {
                delay(1000)
                val state = state()
                val today = Time.now()
                val selectedDate = if (state.today == state.selectedDate) {
                    today
                } else {
                    state.selectedDate
                }
                if (state.today != today || shouldForceUpdate) {
                    val data = failAllUndoneTasks() ?: continue
                    shouldForceUpdate = false
                    val currentPage = calendarPageForWeekStart(
                        weekStart = calendarWeekStart(selectedDate),
                        anchorWeekStart = state.calendarAnchorWeekStart,
                    )
                    currentCoroutineContext().ensureActive()
                    dispatch(
                        MainMessage.LoadInitialData(
                            today = today,
                            selectedDate = selectedDate,
                            currentPage = currentPage,
                            data = data,
                            cachedWeekStarts = state.cachedWeekStarts,
                        ),
                    )
                }
            }
        }
    }

    private fun failAllUndoneTasks(): MainData? {
        val state = state()
        val data = state.data ?: return null
        val today = Time.now()
        val pastDates = state.cachedWeekStarts
            .flatMap(::calendarWeekDates)
            .filter { date -> date < today }

        val updatedItems = data.items.map { itemWithCompletion ->
            val item = itemWithCompletion.checkingItem
            if (item is TodoItem && item.repeatPeriod is TodoItem.RepeatPeriod.Single) {
                // todo do not reschedule if done
                val (item, isDone) = rescheduleItem(item)
                val updatedCompletion = if (isDone == null) {
                    emptyMap()
                } else {
                    itemWithCompletion.completion.keys.associateWith { isDone }
                }
                itemWithCompletion.copy(
                    checkingItem = item,
                    completion = updatedCompletion,
                )
            } else {
                val updatedCompletion = itemWithCompletion.completion.toMutableMap()
                pastDates.forEach { date ->
                    if (item.isPlanned(date)) {
                        val value = changeItemCompletion(
                            item = item,
                            isDone = updatedCompletion[date],
                            date = date,
                        )
                        if (value == null) {
                            updatedCompletion.remove(date)
                        } else {
                            updatedCompletion[date] = value
                        }
                    }
                }
                itemWithCompletion.copy(completion = updatedCompletion)
            }
        }

        return data.copy(
            items = updatedItems,
        )
    }

    private fun startInitialLoad(): Job {
        initialLoadJob?.cancel()
        return scope.launch {
            loadInitialData(today = Time.now())
        }.also { initialLoadJob = it }
    }

    private suspend fun loadInitialData(
        today: LocalDate,
    ) {
        dispatch(MainMessage.InitialLoadStarted)
        val weeks = Time.getWeekRange(
            day = today,
            weeksAfter = END,
            weeksBefore = START,
        )
        getMoodAndItemsUseCase.invoke(
            startDate = weeks.first,
            endDate = weeks.second,
        ).onSuccess { data ->
            val cachedWeekStarts = calendarWeekStarts(data.startDate, data.endDate)
            val anchorWeekStart = state().calendarAnchorWeekStart
            visibleCalendarWeekStart = calendarWeekStart(today)
            currentCoroutineContext().ensureActive()
            dispatch(
                MainMessage.LoadInitialData(
                    today = today,
                    selectedDate = today,
                    data = data,
                    cachedWeekStarts = cachedWeekStarts,
                    currentPage = calendarPageForWeekStart(
                        weekStart = calendarWeekStart(today),
                        anchorWeekStart = anchorWeekStart,
                    ),
                ),
            )
        }.onFailure {
            currentCoroutineContext().ensureActive()
            dispatch(MainMessage.InitialLoadFailed)
        }
    }

    private fun loadCalendarWindow(
        visibleWeekStart: LocalDate,
        prefetch: Boolean,
    ) {
        val normalizedVisibleWeekStart = calendarWeekStart(visibleWeekStart)
        visibleCalendarWeekStart = normalizedVisibleWeekStart
        calendarWindowJob?.cancel()
        calendarWindowJob = scope.launch {
            val state = state()
            if (state.data == null) return@launch
            val plan = calendarLoadPlan(
                visibleWeekStart = normalizedVisibleWeekStart,
                cachedWeekStarts = state.cachedWeekStarts,
                today = state.today,
                selectedDate = state.selectedDate,
            )
            val weeksToLoad = if (prefetch) {
                plan.requiredWeekStarts + plan.prefetchWeekStarts
            } else {
                plan.requiredWeekStarts
            }
            loadCalendarRanges(
                ranges = calendarDateRanges(weeksToLoad),
                visibleWeekStart = normalizedVisibleWeekStart,
            )
        }
    }

    private fun reloadCachedCalendarData() {
        refreshJob?.cancel()
        refreshJob = scope.launch {
            val state = state()
            if (state.data == null || state.cachedWeekStarts.isEmpty()) {
                // Nothing to refresh yet: the screen never loaded, so recover it instead.
                startInitialLoad()
                return@launch
            }
            loadCalendarRanges(
                ranges = calendarDateRanges(state.cachedWeekStarts),
                visibleWeekStart = visibleCalendarWeekStart ?: calendarWeekStart(state.today),
            )
        }
    }

    /**
     * Loads [ranges], merges them into the cached data and re-applies the retention policy.
     *
     * Both the scroll-driven window and the refresh go through here so that the emptiness guard
     * before [trimCalendarData] and the bookkeeping of failed weeks cannot drift apart.
     */
    private suspend fun loadCalendarRanges(
        ranges: List<CalendarDateRange>,
        visibleWeekStart: LocalDate,
    ) {
        val loadedChunks = mutableListOf<MainData>()
        val loadedWeekStarts = mutableSetOf<LocalDate>()
        val failedWeekStarts = mutableSetOf<LocalDate>()

        ranges.forEach { range ->
            val rangeWeekStarts = calendarWeekStarts(range.startDate, range.endDate)
            getMoodAndItemsUseCase.invoke(
                startDate = range.startDate,
                endDate = range.endDate,
            ).onSuccess { loadedData ->
                loadedChunks += loadedData
                loadedWeekStarts += rangeWeekStarts
            }.onFailure {
                failedWeekStarts += rangeWeekStarts
            }
        }

        val state = state()
        val data = state.data ?: return
        var updatedData = data
        loadedChunks.forEach { loadedData ->
            updatedData = updatedData.mergeCalendarData(loadedData)
        }
        val retainedWeekStarts = calendarWeeksToRetain(
            loadedWeekStarts = state.cachedWeekStarts + loadedWeekStarts,
            visibleWeekStart = visibleWeekStart,
            today = state.today,
            selectedDate = state.selectedDate,
        )
        // A week is only worth remembering as failed while it stays inside the working window
        // and has no cached data to fall back on.
        val relevantWeekStarts = calendarRequiredWeekStarts(
            visibleWeekStart = visibleWeekStart,
            today = state.today,
            selectedDate = state.selectedDate,
        )
        val updatedFailedWeekStarts = (state.failedWeekStarts - loadedWeekStarts + failedWeekStarts)
            .intersect(relevantWeekStarts)
            .minus(retainedWeekStarts)

        val shouldDispatch = retainedWeekStarts.isNotEmpty() && (
            loadedChunks.isNotEmpty() ||
                retainedWeekStarts != state.cachedWeekStarts ||
                updatedFailedWeekStarts != state.failedWeekStarts
            )
        if (!shouldDispatch) return

        currentCoroutineContext().ensureActive()
        dispatch(
            MainMessage.DataUpdated(
                data = updatedData.trimCalendarData(retainedWeekStarts),
                cachedWeekStarts = retainedWeekStarts,
                failedWeekStarts = updatedFailedWeekStarts,
            ),
        )
    }

    private fun changeItemIsDone(
        item: CheckingItem,
        isDone: Boolean?,
        date: LocalDate,
    ): Boolean? {
        val isDone = if (isDone == null || !isDone) {
            true
        } else if (
            date < Time.now() &&
            (item as? HabitItem)?.daysForHabit?.contains(date.dayOfWeek) == true
        ) {
            false
        } else {
            null
        }
        scope.launch {
            setItemDoneUseCase.invoke(
                item = item,
                date = date,
                isDone = isDone,
            )
        }
        return isDone
    }

    private fun changeItemCompletion(
        item: CheckingItem,
        isDone: Boolean?,
        date: LocalDate,
    ): Boolean? {
        val isItemDone = when (item) {
            is HabitItem -> {
                if (item.daysForHabit.contains(date.dayOfWeek) && isDone == null) {
                    false
                } else {
                    isDone
                }
            }

            is TodoItem -> {
                when (item.repeatPeriod) {
                    is TodoItem.RepeatPeriod.Single -> {
                        null
                    }

                    else -> {
                        if (item.isPlanned(date) && isDone == null) {
                            false
                        } else {
                            isDone
                        }
                    }
                }
            }

            else -> null
        }
        if (isItemDone != isDone) {
            scope.launch {
                setItemDoneUseCase.invoke(
                    item = item,
                    date = date,
                    isDone = isItemDone,
                )
            }
        }
        return isItemDone
    }

    private fun CheckingItem.isPlanned(date: LocalDate): Boolean = when (this) {
        is HabitItem -> dateInHabitRange(date) && date.dayOfWeek in daysForHabit
        is TodoItem -> isPlanned(date)
        else -> false
    }

    override fun dispose() {
        stopObserveCurrentDate()
        initialLoadJob?.cancel()
        calendarWindowJob?.cancel()
        refreshJob?.cancel()
        appVisibility.unregisterListener(appVisibilityListener)
        super.dispose()
    }

    private fun updateMoodInDatabase(
        rate: Int,
        date: LocalDate,
    ) {
        scope.launch {
            setMoodUseCase.invoke(
                rate = rate,
                date = date,
            ).onSuccess {
                // todo
            }.onFailure {
                // todo
            }
        }
    }

    private fun rescheduleItem(item: TodoItem): Pair<TodoItem, Boolean?> {
        return if (item.shouldReschedule() == true) {
            val newDate = Time.now()
            scope.launch {
                val newItem = item.copy(
                    repeatPeriod = (item.repeatPeriod as TodoItem.RepeatPeriod.Single).copy(startDate = newDate),
                )
                updateTodoUseCase.invoke(newItem)
            }
            item.copy(repeatPeriod = TodoItem.RepeatPeriod.Single(newDate)) to null
        } else if (item.shouldFail() == true) {
            val newDate = item.endDate
            if (newDate == null) {
                item to null
            } else {
                scope.launch {
                    val newItem = item.copy(
                        repeatPeriod = (item.repeatPeriod as TodoItem.RepeatPeriod.Single).copy(startDate = newDate),
                    )
                    updateTodoUseCase.invoke(newItem)
                }
                item.copy(repeatPeriod = TodoItem.RepeatPeriod.Single(newDate)) to false
            }
        } else {
            item to null
        }
    }

    companion object {
        private const val END = 2
        private const val START = 4
    }
}
