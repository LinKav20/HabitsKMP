package com.github.linkav20.habits.core.uikit.components.dayselector

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.github.linkav20.habits.core.uikit.HabitIcons
import kotlinx.datetime.LocalDate
import com.github.linkav20.habits.core.uikit.HabitTheme
import com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.WheelDatePicker
import com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.core.EPOCH
import com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.core.WheelPickerDefaults
import com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.core.format.DateOrder
import com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.core.format.dateFormatter
import com.github.linkav20.habits.core.uikit.resources.Res
import com.github.linkav20.habits.core.uikit.resources.date_picker_accept
import com.github.linkav20.habits.core.uikit.resources.date_picker_cancel
import com.github.linkav20.habits.core.uikit.resources.date_picker_dialog_title_day
import com.github.linkav20.habits.core.uikit.resources.date_picker_dialog_title_full
import kotlinx.datetime.LocalDate.Companion.Format
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock

enum class SelectData {
    DAY,
    DAY_MONTH,
    DAY_MONTH_YEAR,
}

@Composable
fun HabitDayPicker(
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit = {},
    selectData: SelectData = SelectData.DAY_MONTH_YEAR,
    lowDataBound: LocalDate = LocalDate.EPOCH,
    initialDate: LocalDate? = null,
    modifier: Modifier = Modifier,
    dialogSubtitle: String? = null,
) {
    var showDialog by remember { mutableStateOf(false) }
    Box(
        modifier = modifier.fillMaxWidth().height(56.dp)
            .background(
                color = HabitTheme.colors.backgroundTertiary,
                shape = HabitTheme.shapes.medium,
            )
            .clip(HabitTheme.shapes.medium)
            .padding(horizontal = 16.dp)
            .clickable {
                showDialog = true
            },
        contentAlignment = Alignment.Center,
    ) {
        Row {
            Text(
                modifier = Modifier.weight(1f),
                text = when (selectData) {
                    SelectData.DAY -> initialDate?.format(
                        format = Format {
                            day()
                        },
                    ) ?: stringResource(Res.string.date_picker_dialog_title_day)

                    SelectData.DAY_MONTH -> initialDate?.format(
                        format = Format {
                            day()
                            char('.')
                            monthNumber()
                        },
                    ) ?: stringResource(Res.string.date_picker_dialog_title_full)

                    SelectData.DAY_MONTH_YEAR -> initialDate?.format(
                        format = Format {
                            day()
                            char('.')
                            monthNumber()
                            char('.')
                            year()
                        },
                    ) ?: stringResource(Res.string.date_picker_dialog_title_full)
                },
                color = if (initialDate == null) {
                    HabitTheme.colors.textSecondary
                } else {
                    HabitTheme.colors.textPrimary
                },
                style = HabitTheme.typography.body.copy(
                    fontStyle = FontStyle.Italic,
                ),
            )
            Icon(
                painter = painterResource(HabitIcons.Calendar_24),
                contentDescription = null,
                tint = HabitTheme.colors.iconSecondary,
            )
        }
    }

    if (showDialog) {
        SelectDay(
            onDateSelected = {
                onDateSelected(it)
                showDialog = false
            },
            onDismiss = {
                onDismiss()
                showDialog = false
            },
            selectData = selectData,
            lowDataBound = lowDataBound,
            initialDate = initialDate,
            dialogSubtitle = dialogSubtitle,
        )
    }
}

@Composable
private fun SelectDay(
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit,
    selectData: SelectData = SelectData.DAY_MONTH,
    lowDataBound: LocalDate,
    initialDate: LocalDate? = null,
    modifier: Modifier = Modifier,
    dialogSubtitle: String? = null,
) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors().copy(
                containerColor = HabitTheme.colors.primaryContainer,
            ),
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
            ) {
                Text(
                    text = when (selectData) {
                        SelectData.DAY -> stringResource(Res.string.date_picker_dialog_title_day)
                        else -> stringResource(Res.string.date_picker_dialog_title_full)
                    },
                    style = HabitTheme.typography.h2.copy(fontStyle = FontStyle.Italic),
                    color = HabitTheme.colors.textPrimary,
                )
                dialogSubtitle?.let {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = it,
                        style = HabitTheme.typography.body,
                        color = HabitTheme.colors.textPrimary,
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                var selectedDate by remember(initialDate) {
                    mutableStateOf(
                        initialDate ?: Clock.System.now()
                            .toLocalDateTime(TimeZone.currentSystemDefault())
                            .date,
                    )
                }
                val dateOrder by remember(selectData) {
                    mutableStateOf(
                        when (selectData) {
                            SelectData.DAY -> {
                                selectedDate = LocalDate(
                                    year = 2020,
                                    month = 12,
                                    day = selectedDate.day,
                                )
                                DateOrder.D
                            }

                            SelectData.DAY_MONTH -> {
                                selectedDate = LocalDate(
                                    year = 2020,
                                    month = selectedDate.month,
                                    day = selectedDate.day,
                                )
                                DateOrder.DM
                            }

                            SelectData.DAY_MONTH_YEAR -> DateOrder.DMY
                        },
                    )
                }
                WheelDatePicker(
                    startDate = selectedDate,
                    minDate = lowDataBound,
                    modifier = modifier.fillMaxWidth(),
                    textColor = HabitTheme.colors.textPrimary,
                    dateFormatter = dateFormatter(dateOrder = dateOrder),
                    textStyle = HabitTheme.typography.body,
                    selectorProperties = WheelPickerDefaults.selectorProperties(
                        color = HabitTheme.colors.background,
                        border = BorderStroke(1.dp, HabitTheme.colors.borderAccentPrimary),
                    ),
                    onSnappedDate = { selectedDate = it },
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        modifier = Modifier.clip(HabitTheme.shapes.small).clickable {
                            onDismiss()
                        }.padding(8.dp),
                        text = stringResource(Res.string.date_picker_cancel),
                        style = HabitTheme.typography.body,
                        color = HabitTheme.colors.textSecondary,
                    )
                    Text(
                        modifier = Modifier.clip(HabitTheme.shapes.small).clickable {
                            onDateSelected(selectedDate)
                        }.padding(8.dp),
                        text = stringResource(Res.string.date_picker_accept),
                        color = HabitTheme.colors.textAccentPrimary,
                        style = HabitTheme.typography.body.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                }
            }
        }
    }
}
