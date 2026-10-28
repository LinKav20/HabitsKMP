package com.github.linkav20.habit.createitem.impl.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.github.linkav20.habit.createitem.impl.ui.elements.AddHabitFromContent
import com.github.linkav20.habit.createitem.impl.ui.elements.AddTodoFromContent
import com.github.linkav20.habits.core.featureflags.FeatureFlags
import com.github.linkav20.habits.core.uikit.HabitTheme
import com.github.linkav20.habits.core.uikit.components.button.HabitButton
import com.github.linkav20.habits.core.uikit.components.button.HabitButtonState
import com.github.linkav20.habits.core.uikit.components.button.HabitButtonType
import com.github.linkav20.habits.core.uikit.components.segmentedbutton.HabitSegmentedButton
import com.github.linkav20.habits.core.uikit.components.segmentedbutton.Type
import com.github.linkav20.habits.core.uikit.components.topbar.HabitCenterTopBar
import com.github.linkav20.habits.createitem.api.AddHabitComponent
import com.github.linkav20.habits.createitem.api.AddHabitContent
import com.github.linkav20.habits.createitem.api.AddHabitEvent
import com.github.linkav20.habits.createitem.api.AddHabitStore
import com.github.linkav20.habits.createitem.api.model.AddHabitForm
import com.github.linkav20.habits.createitem.api.model.AddTodoForm
import com.github.linkav20.habits.createitem.api.model.CreationForm
import com.github.linkav20.habits.createitem.impl.resources.Res
import com.github.linkav20.habits.createitem.impl.resources.add_habit_create_button
import com.github.linkav20.habits.createitem.impl.resources.add_habit_habit_tab_title
import com.github.linkav20.habits.createitem.impl.resources.add_habit_title
import com.github.linkav20.habits.createitem.impl.resources.add_habit_todo_tab_title
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.stringResource

internal class AddHabitContentImpl : AddHabitContent {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content(
        component: AddHabitComponent,
        modifier: Modifier,
    ) {
        val state by component.model.collectAsState()
        Content(
            state = state,
            onChangeForm = { component.handleEvent(AddHabitEvent.OpenNewCreationForm(it)) },
            onBackClick = { component.handleEvent(AddHabitEvent.OnBackCLick) },
            onHabitTitleChanged = { component.handleEvent(AddHabitEvent.UpdateHabitTitle(it)) },
            onHabitRepeatDaysChanged = { component.handleEvent(AddHabitEvent.UpdateHabitRepeatDays(it)) },
            onHabitDeadlineChanged = { component.handleEvent(AddHabitEvent.UpdateHabitDeadline(it)) },
            onHabitEmojiChanged = { component.handleEvent(AddHabitEvent.UpdateHabitEmoji(it)) },
            onTodoTitleUpdated = { component.handleEvent(AddHabitEvent.UpdateTodoTitle(it)) },
            onTodoDeadlineUpdated = { component.handleEvent(AddHabitEvent.UpdateTodoDeadline(it)) },
            onTodoStartDateChanged = { component.handleEvent(AddHabitEvent.UpdateTodoStartDate(it)) },
            onTodoChangeRegular = { component.handleEvent(AddHabitEvent.UpdateTodoIsRegular) },
            onTodoChangeRepeatPeriod = { component.handleEvent(AddHabitEvent.UpdateTodoRepeatPeriod(it)) },
            onCreate = { component.handleEvent(AddHabitEvent.CreateForm) },
        )
    }
}

@Composable
private fun Content(
    state: AddHabitStore.State,
    onChangeForm: (CreationForm) -> Unit,
    onBackClick: () -> Unit,
    onHabitTitleChanged: (String) -> Unit,
    onHabitRepeatDaysChanged: (Set<DayOfWeek>) -> Unit,
    onHabitDeadlineChanged: (LocalDate?) -> Unit,
    onHabitEmojiChanged: (String) -> Unit,
    onTodoTitleUpdated: (String) -> Unit,
    onTodoDeadlineUpdated: (LocalDate) -> Unit,
    onTodoStartDateChanged: (LocalDate) -> Unit,
    onTodoChangeRegular: () -> Unit,
    onTodoChangeRepeatPeriod: (AddTodoForm.RepeatPeriod) -> Unit,
    onCreate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = HabitTheme.colors.primaryContainer,
        topBar = {
            TopBar(
                selectedTab = state.creationForm,
                onTabChange = { onChangeForm(it) },
                onBackClick = onBackClick,
            )
        },
        bottomBar = {
            HabitButton(
                modifier = Modifier.fillMaxWidth().padding(
                    horizontal = 22.dp,
                    vertical = 16.dp,
                )
                    .navigationBarsPadding(),
                title = stringResource(Res.string.add_habit_create_button),
                type = HabitButtonType.OUTLINE,
                state = if (state.canCreate()) {
                    HabitButtonState.DEFAULT
                } else {
                    HabitButtonState.DISABLED
                },
                onClick = onCreate,
            )
        },
    ) { paddingValues ->
        when (val form = state.creationForm) {
            is AddTodoForm -> {
                AddTodoFromContent(
                    todoForm = form,
                    onTitleUpdated = onTodoTitleUpdated,
                    onDeadlineUpdated = onTodoDeadlineUpdated,
                    onChangeRegular = onTodoChangeRegular,
                    onStartDateChanged = onTodoStartDateChanged,
                    onChangeRepeatPeriod = onTodoChangeRepeatPeriod,
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(
                            top = 40.dp,
                            start = 22.dp,
                            end = 22.dp,
                        ),
                )
            }

            is AddHabitForm -> {
                AddHabitFromContent(
                    habitForm = form,
                    onHabitTitleChanged = onHabitTitleChanged,
                    onHabitRepeatDaysChanged = onHabitRepeatDaysChanged,
                    onHabitDeadlineChanged = onHabitDeadlineChanged,
                    onHabitEmojiChanged = onHabitEmojiChanged,
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(
                            top = 40.dp,
                            start = 22.dp,
                            end = 22.dp,
                        ),
                )
            }
        }
    }
}

@Composable
private fun TopBar(
    selectedTab: CreationForm,
    onTabChange: (CreationForm) -> Unit,
    onBackClick: () -> Unit,
) {
    Column {
        HabitCenterTopBar(
            containerColor = HabitTheme.colors.background,
            title = stringResource(Res.string.add_habit_title),
            onBackClick = onBackClick,
        )
        Box(
            modifier = Modifier.fillMaxWidth().background(
                color = HabitTheme.colors.background,
                shape = HabitTheme.shapes.large.copy(
                    topStart = CornerSize(0.dp),
                    topEnd = CornerSize(0.dp),
                ),
            ).clip(
                HabitTheme.shapes.large.copy(
                    topStart = CornerSize(0.dp),
                    topEnd = CornerSize(0.dp),
                ),
            ).padding(
                start = 26.dp,
                end = 26.dp,
                bottom = 18.dp,
            ),
        ) {
            if (FeatureFlags.TODOS_ENABLED) {
                val habitTab = stringResource(Res.string.add_habit_habit_tab_title)
                val todoTab = stringResource(Res.string.add_habit_todo_tab_title)
                HabitSegmentedButton(
                    selectedOption = when (selectedTab) {
                        is AddHabitForm -> habitTab
                        is AddTodoForm -> todoTab
                    },
                    options = setOf(
                        todoTab,
                        habitTab,
                    ),
                    onOptionSelected = {
                        when (it) {
                            todoTab -> onTabChange(AddTodoForm())
                            habitTab -> onTabChange(AddHabitForm())
                        }
                    },
                    type = Type.OUTLINE,
                )
            }
        }
    }
}
