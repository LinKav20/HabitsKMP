package com.github.linkav20.habits.core.uikit.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import com.github.linkav20.habits.core.uikit.resources.Res
import com.github.linkav20.habits.core.uikit.resources.friday_abbr
import com.github.linkav20.habits.core.uikit.resources.monday_abbr
import com.github.linkav20.habits.core.uikit.resources.saturday_abbr
import com.github.linkav20.habits.core.uikit.resources.sunday_abbr
import com.github.linkav20.habits.core.uikit.resources.thursday_abbr
import com.github.linkav20.habits.core.uikit.resources.tuesday_abbr
import com.github.linkav20.habits.core.uikit.resources.wednesday_abbr
import kotlinx.datetime.DayOfWeek
import org.jetbrains.compose.resources.stringResource


@Composable
fun DayOfWeek.toCapitalizedString() =
    (when (this) {
        DayOfWeek.MONDAY -> stringResource(Res.string.monday_abbr)
        DayOfWeek.TUESDAY -> stringResource(Res.string.tuesday_abbr)
        DayOfWeek.WEDNESDAY -> stringResource(Res.string.wednesday_abbr)
        DayOfWeek.THURSDAY -> stringResource(Res.string.thursday_abbr)
        DayOfWeek.FRIDAY -> stringResource(Res.string.friday_abbr)
        DayOfWeek.SATURDAY -> stringResource(Res.string.saturday_abbr)
        DayOfWeek.SUNDAY -> stringResource(Res.string.sunday_abbr)
    }).capitalize(Locale.current)
