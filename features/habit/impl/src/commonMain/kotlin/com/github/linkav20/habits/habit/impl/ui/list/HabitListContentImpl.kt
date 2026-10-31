package com.github.linkav20.habits.habit.impl.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.github.linkav20.habits.core.uikit.HabitIcons
import com.github.linkav20.habits.core.uikit.components.topbar.HabitTopBarAction
import org.jetbrains.compose.resources.painterResource
import com.github.linkav20.habits.core.uikit.HabitTheme
import com.github.linkav20.habits.core.uikit.components.topbar.HabitCenterTopBar
import com.github.linkav20.habits.habit.api.list.HabitListEvent
import com.github.linkav20.habits.habit.api.list.HabitListComponent
import com.github.linkav20.habits.habit.api.list.HabitListItem
import com.github.linkav20.habits.habit.api.list.HabitListStore
import com.github.linkav20.habits.habit.api.list.HabitListContent
import com.github.linkav20.habits.habit.impl.resources.*
import org.jetbrains.compose.resources.stringResource

internal class HabitListContentImpl : HabitListContent {
    @Composable
    override fun Content(component: HabitListComponent, modifier: Modifier) {
        HabitListScreen(
            state = component.model.collectAsState(),
            onBackClick = { component.handleEvent(HabitListEvent.OnBack) },
            onRetryClick = { component.handleEvent(HabitListEvent.OnReload) },
            onHabitClick = { id -> component.handleEvent(HabitListEvent.OnHabitChanged(id)) },
            modifier = modifier,
        )
    }
}

@Composable
private fun HabitListScreen(
    state: State<HabitListStore.State>,
    onBackClick: () -> Unit,
    onRetryClick: () -> Unit,
    onHabitClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = HabitTheme.colors.background,
        topBar = { HabitListTopBar(onBackClick = onBackClick) },
    ) { padding ->
        HabitListBody(
            state = state.value,
            onRetryClick = onRetryClick,
            onHabitClick = onHabitClick,
            modifier = Modifier.fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding),
        )
    }
}

@Composable
private fun HabitListTopBar(onBackClick: () -> Unit) {
    HabitCenterTopBar(
        title = stringResource(Res.string.habit_list_title),
        containerColor = HabitTheme.colors.background,
        onBackClick = onBackClick,
        actions = listOf(
            HabitTopBarAction(
                icon = painterResource(HabitIcons.Filter_24),
                onClick = {},
            ),
        ),
    )
}

@Composable
private fun HabitListBody(
    state: HabitListStore.State,
    onRetryClick: () -> Unit,
    onHabitClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        when (state) {
            HabitListStore.State.Loading -> HabitListLoading(Modifier.align(Alignment.Center))
            HabitListStore.State.Error -> HabitListError(
                onRetryClick = onRetryClick,
                modifier = Modifier.align(Alignment.Center),
            )

            is HabitListStore.State.Content -> {
                if (state.items.isEmpty()) {
                    HabitListEmpty(Modifier.align(Alignment.Center))
                } else {
                    HabitListItems(
                        habits = state.items,
                        onHabitClick = onHabitClick,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}

@Composable
private fun HabitListLoading(modifier: Modifier = Modifier) {
    CircularProgressIndicator(modifier)
}

@Composable
private fun HabitListError(onRetryClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(stringResource(Res.string.habit_list_error), color = HabitTheme.colors.textSecondary)
        TextButton(onClick = onRetryClick) {
            Text(stringResource(Res.string.habit_list_retry))
        }
    }
}

@Composable
private fun HabitListEmpty(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(Res.string.habit_list_empty),
        modifier = modifier.padding(24.dp),
        color = HabitTheme.colors.textSecondary,
    )
}

@Composable
private fun HabitListItems(
    habits: List<HabitListItem>,
    onHabitClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        items(habits, key = { it.id }) { habit ->
            HabitListCard(
                id = habit.id,
                title = habit.title,
                emoji = habit.emoji,
                onHabitClick = onHabitClick,
            )
        }
    }
}

@Composable
private fun HabitListCard(
    id: String,
    title: String,
    emoji: String,
    onHabitClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 68.dp)
            .clip(HabitTheme.shapes.large)
            .background(HabitTheme.colors.surfaceContainerLowest)
            .clickable(role = Role.Button) { onHabitClick(id) }
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = emoji,
            style = HabitTheme.typography.body,
        )
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            style = HabitTheme.typography.body,
            color = HabitTheme.colors.textPrimary,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
