package com.github.linkav20.habits.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.intl.Locale
import com.github.linkav20.habits.core.utils.resources.Res
import com.github.linkav20.habits.core.utils.resources.april_full
import com.github.linkav20.habits.core.utils.resources.august_full
import com.github.linkav20.habits.core.utils.resources.december_full
import com.github.linkav20.habits.core.utils.resources.february_full
import com.github.linkav20.habits.core.utils.resources.january_full
import com.github.linkav20.habits.core.utils.resources.july_full
import com.github.linkav20.habits.core.utils.resources.june_full
import com.github.linkav20.habits.core.utils.resources.march_full
import com.github.linkav20.habits.core.utils.resources.may_full
import com.github.linkav20.habits.core.utils.resources.november_full
import com.github.linkav20.habits.core.utils.resources.october_full
import com.github.linkav20.habits.core.utils.resources.september_full
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDate.Companion.Format
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

public fun LocalDate.dayWithMonthNumber(): String =
    this.format(
        format = Format {
            day()
            char('.')
            monthNumber()
        },
    )

@Composable
public fun LocalDate.fullMonthWithDay(
    locale: Locale = Locale.current,
): String {
    val monthNames = MonthNames(
        stringResource(Res.string.january_full),
        stringResource(Res.string.february_full),
        stringResource(Res.string.march_full),
        stringResource(Res.string.april_full),
        stringResource(Res.string.may_full),
        stringResource(Res.string.june_full),
        stringResource(Res.string.july_full),
        stringResource(Res.string.august_full),
        stringResource(Res.string.september_full),
        stringResource(Res.string.october_full),
        stringResource(Res.string.november_full),
        stringResource(Res.string.december_full),
    )
    return this.format(
        format = Format {
            monthName(monthNames)
            char(',')
            char(' ')
            day()
        },
    )
}

@Composable
public fun LocalDate.fullDate(): String {
    return this.format(
        format = Format {
            day()
            char('.')
            monthNumber()
            char('.')
            year()
        },
    )
}

@Composable
public fun LocalDate.toMonthDay(): String {
    return this.format(
        format = Format {
            day()
            char('.')
            monthNumber()
        },
    )
}

@Composable
public fun LocalDate.toDay(): String {
    return this.format(
        format = Format {
            day(Padding.NONE)
        },
    )
}
