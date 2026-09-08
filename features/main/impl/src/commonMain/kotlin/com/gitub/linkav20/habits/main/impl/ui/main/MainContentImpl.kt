package com.gitub.linkav20.habits.main.impl.ui.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.linkav20.habits.core.uikit.HabitIcons
import com.github.linkav20.habits.core.uikit.HabitTheme
import com.github.linkav20.habits.core.uikit.components.charts.ChartAnimation
import com.github.linkav20.habits.core.uikit.components.charts.line.LineChart
import com.github.linkav20.habits.core.uikit.components.charts.line.LineChartData
import com.github.linkav20.habits.core.uikit.components.charts.line.LineChartPoint
import com.github.linkav20.habits.core.uikit.components.charts.line.LineChartSeries
import com.github.linkav20.habits.core.uikit.components.button.HabitButton
import com.github.linkav20.habits.core.uikit.components.button.HabitButtonSize
import com.github.linkav20.habits.core.uikit.components.button.HabitButtonType
import com.github.linkav20.habits.core.uikit.components.error.TemplateFullscreenThrowable
import com.github.linkav20.habits.core.uikit.components.topbar.HabitTopBar
import com.github.linkav20.habits.core.uikit.components.topbar.HabitTopBarAction
import com.github.linkav20.habits.core.uikit.utils.toCapitalizedString
import com.github.linkav20.habits.core.utils.dayWithMonthNumber
import com.github.linkav20.habits.core.utils.fullMonthWithDay
import com.github.linkav20.habits.main.impl.resources.Res
import com.github.linkav20.habits.main.impl.resources.calendar_week_error_title
import com.github.linkav20.habits.main.impl.resources.main_retry_button
import com.github.linkav20.habits.main.impl.resources.today_title
import com.github.linkav20.habits.main.impl.resources.tomorrow_title
import com.github.linkav20.habits.main.impl.resources.yesterday_title
import com.gitub.linkav20.habits.main.api.main.MainComponent
import com.gitub.linkav20.habits.main.api.main.MainContent
import com.gitub.linkav20.habits.main.api.main.MainEvent
import com.gitub.linkav20.habits.main.api.main.MainStore
import com.gitub.linkav20.habits.main.api.domain.model.main.CheckingItem
import com.gitub.linkav20.habits.main.api.domain.model.main.toEmoji
import com.gitub.linkav20.habits.main.impl.mvi.main.CALENDAR_PAGE_COUNT
import com.gitub.linkav20.habits.main.impl.mvi.main.calendarPageForWeekStart
import com.gitub.linkav20.habits.main.impl.mvi.main.calendarWeekStart
import com.gitub.linkav20.habits.main.impl.mvi.main.calendarWeekStartForPage
import com.gitub.linkav20.habits.main.impl.ui.main.elements.MoodSheetContent
import com.gitub.linkav20.habits.main.impl.ui.main.elements.TasksSheetContent
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.abs

enum class BottomSheetState {
    MINIMIZED,
    MAXIMIZED,
}

internal class MainContentImpl : MainContent {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content(
        component: MainComponent,
        modifier: Modifier,
    ) {
        val state by component.model.collectAsState()

        when {
            state.data != null -> LoadedContent(component = component, state = state, modifier = modifier)
            state.initialLoadFailed -> InitialLoadError(
                modifier = modifier,
                onRetryClick = { component.handleEvent(MainEvent.RetryInitialLoad) },
            )

            else -> InitialLoading(modifier = modifier)
        }
    }
}

@Composable
private fun InitialLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(HabitTheme.colors.background),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            color = HabitTheme.colors.primary,
        )
    }
}

@Composable
private fun InitialLoadError(
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TemplateFullscreenThrowable(
        modifier = modifier
            .fillMaxSize()
            .background(HabitTheme.colors.background),
        buttonText = stringResource(Res.string.main_retry_button),
        onActionClick = onRetryClick,
    )
}

@Composable
private fun LoadedContent(
    component: MainComponent,
    state: MainStore.State,
    modifier: Modifier = Modifier,
) {
    Content(
        state = state,
        modifier = modifier,
        onChangeSelectedDate = { component.handleEvent(MainEvent.ChangeSelectedDate(it)) },
        onCalendarPageSettled = { weekStart, prefetch ->
            component.handleEvent(
                MainEvent.CalendarPageSettled(
                    weekStart = weekStart,
                    prefetch = prefetch,
                ),
            )
        },
        onCheckItemClick = { item, date ->
            component.handleEvent(
                MainEvent.OnCheckingItemClick(
                    checkingItem = item,
                    date = date,
                ),
            )
        },
        onRetryCalendarWeek = { component.handleEvent(MainEvent.RetryCalendarWeek(it)) },
        setRate = { rate, date -> component.handleEvent(MainEvent.SetRate(rate, date)) },
        onProfileClick = { component.handleEvent(MainEvent.OnProfileClick) },
        onAddItemClick = { component.handleEvent(MainEvent.OnAddItemClick) },
        onViewItemClick = { component.handleEvent(MainEvent.OnOpenItemClick(it)) },
    )
}

// Every parameter here is a screen event. The list goes away with the pager extraction, not by
// bundling unrelated callbacks into a parameter object.
@Suppress("LongParameterList")
@Composable
private fun Content(
    state: MainStore.State,
    modifier: Modifier = Modifier,
    onChangeSelectedDate: (LocalDate) -> Unit,
    onCalendarPageSettled: (LocalDate, Boolean) -> Unit,
    onRetryCalendarWeek: (LocalDate) -> Unit,
    onCheckItemClick: (CheckingItem, LocalDate) -> Unit,
    setRate: (Int, LocalDate) -> Unit,
    onProfileClick: () -> Unit,
    onAddItemClick: () -> Unit,
    onViewItemClick: (CheckingItem) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val uiModel = remember(state) { state.toUiModel() }
    val currentOnCalendarPageSettled by rememberUpdatedState(onCalendarPageSettled)
    val pagerState = rememberPagerState(
        initialPage = state.currentPage,
        pageCount = { CALENDAR_PAGE_COUNT },
    )
    var positionedCurrentPage by remember { mutableStateOf<Int?>(null) }
    var suppressedPrefetchWeek by remember { mutableStateOf<LocalDate?>(null) }
    var moodBottomSheetVisibility by remember { mutableStateOf<LocalDate?>(null) }

    LaunchedEffect(state.currentPage) {
        if (pagerState.currentPage != state.currentPage) {
            pagerState.scrollToPage(state.currentPage)
        }
        positionedCurrentPage = state.currentPage
    }

    LaunchedEffect(
        pagerState,
        positionedCurrentPage,
        state.currentPage,
        state.data != null,
        state.calendarAnchorWeekStart,
    ) {
        if (state.data == null) return@LaunchedEffect
        if (positionedCurrentPage != state.currentPage) return@LaunchedEffect
        snapshotFlow { pagerState.settledPage }
            .distinctUntilChanged()
            .collect { page ->
                val weekStart = calendarWeekStartForPage(
                    page = page,
                    anchorWeekStart = state.calendarAnchorWeekStart,
                )
                val shouldPrefetch = suppressedPrefetchWeek != weekStart
                if (!shouldPrefetch) {
                    suppressedPrefetchWeek = null
                }
                currentOnCalendarPageSettled(weekStart, shouldPrefetch)
            }
    }

    Scaffold(
        modifier = modifier,
        containerColor = HabitTheme.colors.background,
        topBar = {
            TopBar(
                selectedDate = state.selectedDate,
                today = state.today,
                onChangeSelectedDate = {
                    onChangeSelectedDate(it)
                    val targetWeekStart = calendarWeekStart(it)
                    val targetPage = calendarPageForWeekStart(
                        weekStart = targetWeekStart,
                        anchorWeekStart = state.calendarAnchorWeekStart,
                    )
                    if (targetPage != pagerState.currentPage) {
                        if (targetWeekStart in state.cachedWeekStarts) {
                            suppressedPrefetchWeek = targetWeekStart
                        }
                        scope.launch {
                            val pageDistance = abs(
                                targetPage.toLong() - pagerState.currentPage.toLong(),
                            )
                            if (pageDistance <= TOP_BAR_ANIMATED_JUMP_MAX_PAGES) {
                                pagerState.animateScrollToPage(targetPage)
                            } else {
                                pagerState.scrollToPage(targetPage)
                            }
                        }
                    }
                },
                onProfileClick = onProfileClick,
            )
        },
    ) { paddings ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = paddings.calculateTopPadding()),
        ) {
            val scope = rememberCoroutineScope()
            var bottomSheetState by remember { mutableStateOf(BottomSheetState.MAXIMIZED) }
            val swipeProgress = remember { Animatable(0f) }
            var lastDragAmount by remember { mutableStateOf(Offset.Zero) }

            SharedTransitionLayout {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth(),
                    beyondViewportPageCount = CALENDAR_BEYOND_VIEWPORT_PAGE_COUNT,
                    key = { page ->
                        calendarWeekStartForPage(
                            page = page,
                            anchorWeekStart = state.calendarAnchorWeekStart,
                        )
                    },
                    flingBehavior = PagerDefaults.flingBehavior(
                        state = pagerState,
                        snapAnimationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessLow,
                        ),
                    ),
                ) { page ->
                    val weekStart = calendarWeekStartForPage(
                        page = page,
                        anchorWeekStart = state.calendarAnchorWeekStart,
                    )
                    val week = remember(state, weekStart) { state.toWeekUiModel(weekStart) }
                    AnimatedContent(
                        targetState = bottomSheetState,
                        transitionSpec = {
                            fadeIn(
                                animationSpec = tween(
                                    durationMillis = 300,
                                    easing = LinearEasing,
                                ),
                            ) togetherWith
                                fadeOut(
                                    animationSpec = tween(
                                        durationMillis = 300,
                                        easing = LinearEasing,
                                    ),
                                )
                        },
                    ) { targetState ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    vertical = 8.dp,
                                    horizontal = 16.dp,
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            when (targetState) {
                                BottomSheetState.MINIMIZED -> {
                                    GraphHabitsContent(
                                        state = state,
                                        week = week,
                                        animatedVisibilityScope = this@AnimatedContent,
                                        sharedTransitionScope = this@SharedTransitionLayout,
                                        onChangeSelectedDate = onChangeSelectedDate,
                                        onCheckItemClick = onCheckItemClick,
                                        onRetryCalendarWeek = onRetryCalendarWeek,
                                    )
                                }

                                BottomSheetState.MAXIMIZED -> {
                                    SimpleHabitContent(
                                        state = state,
                                        week = week,
                                        animatedVisibilityScope = this@AnimatedContent,
                                        sharedTransitionScope = this@SharedTransitionLayout,
                                        onChangeSelectedDate = onChangeSelectedDate,
                                        onRetryCalendarWeek = onRetryCalendarWeek,
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Box(
                modifier = Modifier.fillMaxSize().weight(1f),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Column(
                    modifier = Modifier
                        .clip(HabitTheme.shapes.extraLarge)
                        .background(HabitTheme.colors.primaryContainer)
                        .fillMaxWidth()
                        .padding(bottom = paddings.calculateBottomPadding()),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                            .background(HabitTheme.colors.primaryContainer)
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDragStart = {
                                        lastDragAmount = Offset.Zero
                                    },
                                    onDrag = { change, dragAmount ->
                                        change.consume()
                                        lastDragAmount = dragAmount
                                        val newProgress = if (dragAmount.y < 0) 1f else 0f
                                        scope.launch {
                                            swipeProgress.snapTo(newProgress)
                                        }
                                    },
                                    onDragEnd = {
                                        val targetState = when {
                                            lastDragAmount.y < -5f ->
                                                BottomSheetState.MAXIMIZED

                                            lastDragAmount.y > 5f ->
                                                BottomSheetState.MINIMIZED

                                            swipeProgress.value > 0.5f ->
                                                BottomSheetState.MAXIMIZED

                                            else ->
                                                BottomSheetState.MINIMIZED
                                        }

                                        val targetProgress =
                                            if (targetState == BottomSheetState.MAXIMIZED) 1f else 0f

                                        scope.launch {
                                            swipeProgress.animateTo(
                                                targetValue = targetProgress,
                                                animationSpec = tween(
                                                    durationMillis = 500,
                                                    easing = LinearEasing,
                                                ),
                                            )
                                        }
                                        bottomSheetState = targetState
                                    },
                                    onDragCancel = {
                                        val targetProgress =
                                            if (bottomSheetState == BottomSheetState.MAXIMIZED) 1f else 0f
                                        scope.launch {
                                            swipeProgress.animateTo(
                                                targetValue = targetProgress,
                                                animationSpec = tween(
                                                    durationMillis = 300,
                                                    easing = LinearEasing,
                                                ),
                                            )
                                        }
                                    },
                                )
                            },
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp, 4.dp)
                                .background(
                                    color = HabitTheme.colors.backgroundSecondary,
                                    shape = HabitTheme.shapes.full,
                                )
                                .align(Alignment.Center),
                        )
                    }
                    TasksSheetContent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        tasks = uiModel.groupedTasks,
                        onCheckItemClick = onCheckItemClick,
                        onSetMoodClick = { date ->
                            moodBottomSheetVisibility = date
                        },
                        onViewItemClick = onViewItemClick,
                        onAddItemClick = onAddItemClick,
                    )
                }
            }
        }
    }

    if (moodBottomSheetVisibility != null) {
        MoodSheetContent(
            onSaveMood = { rate -> moodBottomSheetVisibility?.let { setRate(rate, it) } },
            onClose = { moodBottomSheetVisibility = null },
        )
    }
}

private const val CALENDAR_BEYOND_VIEWPORT_PAGE_COUNT = 1
private const val TOP_BAR_ANIMATED_JUMP_MAX_PAGES = 2L

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun GraphHabitsContent(
    state: MainStore.State,
    week: WeekUiModel,
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onChangeSelectedDate: (LocalDate) -> Unit,
    onCheckItemClick: (CheckingItem, LocalDate) -> Unit,
    onRetryCalendarWeek: (LocalDate) -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(vertical = 16.dp),
        ) {
            when (week.loadState) {
                WeekLoadState.LOADED -> HabitsWithCompletion(
                    habits = week.habits,
                    onCheckItemClick = onCheckItemClick,
                )

                WeekLoadState.LOADING -> CalendarWeekLoading()

                WeekLoadState.FAILED -> CalendarWeekError(
                    onRetryClick = { onRetryCalendarWeek(week.weekStart) },
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .height(250.dp),
            )
            week.days
                .forEach { date ->
                    Column(
                        modifier = Modifier.weight(1f).height(300.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        DayNumber(
                            date = date,
                            selectedDate = state.selectedDate,
                            today = state.today,
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope,
                            onChangeSelectedDate = onChangeSelectedDate,
                        )
                        DayAbbr(
                            day = date,
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope,
                        )
                    }
                }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .height(250.dp),
            )
            MoodChart(
                modifier = Modifier.weight(7f).height(250.dp),
                points = week.chartPoints,
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun SimpleHabitContent(
    state: MainStore.State,
    week: WeekUiModel,
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onChangeSelectedDate: (LocalDate) -> Unit,
    onRetryCalendarWeek: (LocalDate) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            week.days
                .forEachIndexed { index, day ->
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        DayNumber(
                            date = day,
                            selectedDate = state.selectedDate,
                            today = state.today,
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope,
                            onChangeSelectedDate = onChangeSelectedDate,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = week.moodRates[index].toEmoji(),
                            style = HabitTheme.typography.body,
                            color = HabitTheme.colors.textPrimary,
                        )
                        DayAbbr(
                            day = day,
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope,
                        )
                    }
                }
        }
        if (week.loadState == WeekLoadState.FAILED) {
            CalendarWeekErrorRow(
                onRetryClick = { onRetryCalendarWeek(week.weekStart) },
            )
        }
    }
}

@Composable
private fun CalendarWeekLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(24.dp),
            color = HabitTheme.colors.iconAccentPrimary,
            strokeWidth = 2.dp,
        )
    }
}

@Composable
private fun CalendarWeekError(
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(Res.string.calendar_week_error_title),
            style = HabitTheme.typography.textSmall,
            color = HabitTheme.colors.textSecondary,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        HabitButton(
            title = stringResource(Res.string.main_retry_button),
            onClick = onRetryClick,
            size = HabitButtonSize.S,
            type = HabitButtonType.GHOST,
        )
    }
}

@Composable
private fun CalendarWeekErrorRow(
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(Res.string.calendar_week_error_title),
            style = HabitTheme.typography.textSmall,
            color = HabitTheme.colors.textSecondary,
        )
        HabitButton(
            title = stringResource(Res.string.main_retry_button),
            onClick = onRetryClick,
            size = HabitButtonSize.S,
            type = HabitButtonType.GHOST,
        )
    }
}

@Composable
private fun TopBar(
    selectedDate: LocalDate,
    today: LocalDate,
    onChangeSelectedDate: (LocalDate) -> Unit,
    onProfileClick: () -> Unit,
) {
    HabitTopBar(
        containerColor = HabitTheme.colors.background,
        titleFun = {
            Text(
                modifier = Modifier.clickable { onChangeSelectedDate(today) },
                text = when (selectedDate) {
                    today -> stringResource(Res.string.today_title, selectedDate.dayWithMonthNumber())
                    today.minus(1, DateTimeUnit.DAY) -> stringResource(
                        Res.string.yesterday_title,
                        selectedDate.dayWithMonthNumber(),
                    )

                    today.plus(1, DateTimeUnit.DAY) -> stringResource(
                        Res.string.tomorrow_title,
                        selectedDate.dayWithMonthNumber(),
                    )

                    else -> selectedDate.fullMonthWithDay()
                },
                color = HabitTheme.colors.textPrimary,
                style = HabitTheme.typography.h1.copy(
                    fontStyle = FontStyle.Italic,
                ),
            )
        },
        actions = listOf(
            HabitTopBarAction(
                icon = painterResource(HabitIcons.ProfileInCircle_24),
                onClick = onProfileClick,
            ),
        ),
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun DayNumber(
    date: LocalDate,
    selectedDate: LocalDate,
    today: LocalDate,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onChangeSelectedDate: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    with(sharedTransitionScope) {
        val iconAccentPrimaryColor = HabitTheme.colors.iconAccentPrimary
        Box(
            modifier = modifier
                .sharedElement(
                    sharedContentState = rememberSharedContentState(key = "day_${date.day}"),
                    animatedVisibilityScope = animatedVisibilityScope,
                )
                .size(32.dp)
                .clip(HabitTheme.shapes.full)
                .drawBehind {
                    if (selectedDate == date) {
                        drawCircle(
                            color = iconAccentPrimaryColor,
                            radius = size.minDimension / 2,
                            center = center,
                        )
                    }
                }
                .clickable { onChangeSelectedDate(date) },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = date.day.toString(),
                style = HabitTheme.typography.bodyAccent.copy(
                    fontWeight = FontWeight.Light,
                ),
                color = when {
                    selectedDate == date -> HabitTheme.colors.iconInverse
                    today == date -> HabitTheme.colors.iconAccentPrimary
                    else -> HabitTheme.colors.iconPrimary
                },
            )
        }
    }
}

@Composable
private fun DayAbbr(
    day: LocalDate,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    with(sharedTransitionScope) {
        Text(
            modifier = Modifier
                .sharedElement(
                    sharedContentState = rememberSharedContentState(
                        key = "${day}_day_of_week_${day.dayOfWeek}",
                    ),
                    animatedVisibilityScope = animatedVisibilityScope,
                ),
            text = day.dayOfWeek.toCapitalizedString(),
            style = HabitTheme.typography.textSmall,
            color = HabitTheme.colors.textSecondary,
        )
    }
}

@Composable
private fun MoodChart(
    points: List<LineChartPoint>,
    modifier: Modifier = Modifier,
) {
    val points = remember(points) {
        LineChartData(
            series = listOf(
                LineChartSeries(
                    dataName = "how i feel",
                    lineWidth = 0.dp,
                    lineColor = Color.Red, // todo
                    listOfPoints = points,
                ),
            ),
        )
    }
    LineChart(
        modifier = modifier,
        animation = ChartAnimation.Disabled,
        lineChartData = points,
    )
}

@Composable
private fun HabitsWithCompletion(
    habits: List<HabitRowUiModel>,
    onCheckItemClick: (CheckingItem, LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (habits.isEmpty()) {
        EmptyHabitsInGraph()
    } else {
        LazyColumn(modifier = modifier) {
            items(habits) { item ->
                val habit = item.item
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = habit.emoji,
                        style = HabitTheme.typography.bodyAccent,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {}
                            .padding(8.dp),
                        color = HabitTheme.colors.textPrimary,
                    )
                    item.completion
                        .forEach {
                            Box(
                                modifier = Modifier.weight(1f).fillMaxWidth(),
                                contentAlignment = Alignment.Center,
                            ) {
                                if (habit.dateInHabitRange(it.first)) {
                                    if (it.second != null) {
                                        if (it.second == true) {
                                            Icon(
                                                painter = painterResource(HabitIcons.ItemDone_20),
                                                contentDescription = null,
                                                tint = HabitTheme.colors.iconAccentPrimary,
                                                modifier = Modifier.size(20.dp).clickable {
                                                    onCheckItemClick(item.item, it.first)
                                                },
                                            )
                                        } else {
                                            Icon(
                                                painter = painterResource(HabitIcons.ItemFailed_20),
                                                contentDescription = null,
                                                tint = HabitTheme.colors.iconAccentPrimary,
                                                modifier = Modifier.size(20.dp).clickable {
                                                    onCheckItemClick(item.item, it.first)
                                                },
                                            )
                                        }
                                    } else if (it.first.dayOfWeek in item.item.daysForHabit) {
                                        Icon(
                                            painter = painterResource(HabitIcons.ItemPlanned_20),
                                            contentDescription = null,
                                            tint = HabitTheme.colors.iconAccentPrimary,
                                            modifier = Modifier.size(20.dp).clickable {
                                                onCheckItemClick(item.item, it.first)
                                            },
                                        )
                                    } else {
                                        Box(
                                            modifier = Modifier.size(20.dp).clickable {
                                                onCheckItemClick(item.item, it.first)
                                            },
                                        )
                                    }
                                }
                            }
                        }
                }
            }
        }
    }
}

@Composable
private fun EmptyHabitsInGraph() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "У вас нет привычек",
            color = HabitTheme.colors.textSecondary,
            style = HabitTheme.typography.textSmall,
        )
    }
}
