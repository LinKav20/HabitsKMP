package com.gitub.linkav20.habits.main.impl.ui.main.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.github.linkav20.habits.core.uikit.HabitIcons
import com.github.linkav20.habits.core.uikit.HabitTheme
import com.github.linkav20.habits.core.uikit.components.button.HabitButton
import com.github.linkav20.habits.core.uikit.components.button.HabitButtonSize
import com.github.linkav20.habits.core.utils.fullMonthWithDay
import com.github.linkav20.habits.main.impl.resources.Res
import com.github.linkav20.habits.main.impl.resources.later_tasks_title
import com.github.linkav20.habits.main.impl.resources.simple_todo_days_until_deadline
import com.github.linkav20.habits.main.impl.resources.today_tasks_title
import com.github.linkav20.habits.main.impl.resources.tomorrow_tasks_title
import com.gitub.linkav20.habits.main.api.domain.model.main.CheckingItem
import com.gitub.linkav20.habits.main.api.domain.model.main.HabitItem
import com.gitub.linkav20.habits.main.api.domain.model.main.TodoItem
import com.gitub.linkav20.habits.main.api.domain.model.main.taskslist.TasksGroup
import com.gitub.linkav20.habits.main.api.domain.model.main.taskslist.TasksItem
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TasksSheetContent(
    tasks: Map<TasksGroup, List<Pair<TasksItem, Pair<LocalDate, Boolean?>>>>,
    onCheckItemClick: (CheckingItem, LocalDate) -> Unit,
    onSetMoodClick: (LocalDate) -> Unit,
    onAddItemClick: () -> Unit,
    onViewItemClick: (CheckingItem) -> Unit,
    modifier: Modifier,
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp),
    ) {
        tasks.entries.forEach { group ->
            item(key = "header_${group.key}") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        modifier = Modifier.weight(1f).animateItem(),
                        text = when (group.key) {
                            is TasksGroup.Date -> (group.key as TasksGroup.Date).date.fullMonthWithDay()
                            TasksGroup.Today -> stringResource(Res.string.today_tasks_title)
                            TasksGroup.Tomorrow -> stringResource(Res.string.tomorrow_tasks_title)
                            TasksGroup.Later -> stringResource(Res.string.later_tasks_title)
                        },
                        style = HabitTheme.typography.h2.copy(
                            fontStyle = FontStyle.Italic,
                        ),
                        color = HabitTheme.colors.textPrimary,
                    )
                    if (group.key is TasksGroup.Today) {
                        Icon(
                            modifier = Modifier.size(28.dp).clickable {
                                onAddItemClick()
                            },
                            painter = painterResource(HabitIcons.AddInCircle_24),
                            tint = HabitTheme.colors.iconSecondary,
                            contentDescription = null,
                        )
                    }
                }
            }
            val tasks = group.value
            items(
                items = tasks,
                key = { taskWithCompletion ->
                    val task = when (val item = taskWithCompletion.first) {
                        is TasksItem.Item -> item.item.toString()
                        TasksItem.RateDay -> item.toString()
                        TasksItem.Empty -> item.toString()
                    }
                    val date = taskWithCompletion.second.first
                    "${task}_${date}"
                },
            ) { taskWithCompletion ->
                val doneInfo = taskWithCompletion.second
                when (val task = taskWithCompletion.first) {
                    is TasksItem.Item -> {
                        CheckingItem(
                            modifier = Modifier.padding(horizontal = 12.dp).animateItem(),
                            task = task.item,
                            isDone = doneInfo.second ?: false,
                            date = if (group.key == TasksGroup.Later) doneInfo.first else null,
                            onCheckItemClick = { onCheckItemClick(task.item, doneInfo.first) },
                            onViewItemClick = { onViewItemClick(task.item) },
                        )
                    }

                    is TasksItem.Empty ->
                        EmptyBlock(
                            modifier = Modifier
                                .padding(horizontal = 12.dp).animateItem(),
                        )

                    TasksItem.RateDay -> {
                        Column(
                            modifier = Modifier.animateItem(),
                        ) {
                            Spacer(modifier = Modifier.height(8.dp))
                            HabitButton(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                                size = HabitButtonSize.XL,
                                title = "How was your day?",
                                onClick = { onSetMoodClick(doneInfo.first) },
                            )
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun CheckingItem(
    isDone: Boolean,
    task: CheckingItem,
    date: LocalDate?,
    modifier: Modifier = Modifier,
    onCheckItemClick: () -> Unit,
    onViewItemClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .padding(vertical = 12.dp)
            .fillMaxWidth()
            .background(
                color = HabitTheme.colors.backgroundTertiary,
                shape = HabitTheme.shapes.medium,
            )
            .clip(HabitTheme.shapes.medium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.weight(1f).clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            ) {
                onViewItemClick()
            }.padding(
                horizontal = 18.dp,
                vertical = 28.dp,
            ),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            (task as? HabitItem)?.emoji?.let {
                Text(
                    text = it,
                    style = HabitTheme.typography.body,
                    color = HabitTheme.colors.textPrimary,
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = task.title,
                    style = if (isDone) {
                        HabitTheme.typography.body.copy(
                            textDecoration = TextDecoration.LineThrough,
                        )
                    } else {
                        HabitTheme.typography.body
                    },
                    color = if (isDone) {
                        HabitTheme.colors.textSecondary
                    } else {
                        HabitTheme.colors.textPrimary
                    },
                )
                if (task is TodoItem && task.repeatPeriod is TodoItem.RepeatPeriod.Single) {
                    val daysToDeadline = task.daysUntilDeadline()
                    Text(
                        text = task.getAccent() + pluralStringResource(
                            Res.plurals.simple_todo_days_until_deadline,
                            daysToDeadline,
                            daysToDeadline,
                        ),
                        style = HabitTheme.typography.textSmall,
                        color = if (daysToDeadline > TodoItem.CRITICAL_DAYS_UNTIL_DEADLINE) {
                            HabitTheme.colors.textSecondary
                        } else {
                            HabitTheme.colors.textAccentPrimary
                        },
                    )
                }
                date?.let {
                    Text(
                        text = it.fullMonthWithDay(),
                        style = if (isDone) {
                            HabitTheme.typography.textSmall.copy(
                                textDecoration = TextDecoration.LineThrough,
                            )
                        } else {
                            HabitTheme.typography.textSmall
                        },
                        color = HabitTheme.colors.textSecondary,
                    )
                }
            }
        }
        RadioButton(
            modifier = Modifier.padding(end = 18.dp),
            selected = isDone,
            onClick = onCheckItemClick,
            colors = RadioButtonDefaults.colors().copy(
                selectedColor = HabitTheme.colors.borderAccentPrimary,
                unselectedColor = HabitTheme.colors.borderBasic,
            ),
        )
    }
}

@Composable
private fun EmptyBlock(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(vertical = 12.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
            text = "У вас нет никаких запланированных дел и привычек",
            color = HabitTheme.colors.textPrimary,
            style = HabitTheme.typography.subtitle,
            textAlign = TextAlign.Center,
        )
        Text(
            modifier = Modifier.fillMaxSize().padding(horizontal = 18.dp),
            text = "Вы можете запланировать их",
            color = HabitTheme.colors.textSecondary,
            style = HabitTheme.typography.body,
            textAlign = TextAlign.Center,
        )
    }
}
