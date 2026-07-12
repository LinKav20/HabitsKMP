package com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.core.format

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.intl.Locale
import com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.strings.EnStrings
import com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.strings.RuStrings
import com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.strings.Strings

@Stable
interface TimeFormatter {
    val timeFormat: TimeFormat
    val formatHour: (Int) -> String
    val formatMinute: (Int) -> String
    val formatAmText: () -> String
    val formatPmText: () -> String
}

private class TimeFormatterImpl(
    override val timeFormat: TimeFormat,
    override val formatHour: (Int) -> String,
    override val formatMinute: (Int) -> String,
    override val formatAmText: () -> String,
    override val formatPmText: () -> String,
) : TimeFormatter

fun timeFormatter(
    timeFormat: TimeFormat = TimeFormat.HOUR_24,
    formatHour: (Int) -> String = { hour ->
        hour.toString().padStart(2, '0').toLocalizedNumerals()
    },
    formatMinute: (Int) -> String = { minute ->
        minute.toString().padStart(2, '0').toLocalizedNumerals()
    },
    formatAmText: () -> String = {
        "AM"
    },
    formatPmText: () -> String = {
        "PM"
    },
): TimeFormatter = TimeFormatterImpl(
    timeFormat = timeFormat,
    formatHour = formatHour,
    formatMinute = formatMinute,
    formatAmText = formatAmText,
    formatPmText = formatPmText,
)

internal fun timeFormatter(
    strings: Strings,
    timeFormat: TimeFormat = TimeFormat.HOUR_24,
    formatHour: (Int) -> String = { hour ->
        hour.toString().padStart(2, '0').toLocalizedNumerals(strings)
    },
    formatMinute: (Int) -> String = { minute ->
        minute.toString().padStart(2, '0').toLocalizedNumerals(strings)
    },
    formatAmText: () -> String = { strings.timeAM },
    formatPmText: () -> String = { strings.timePM },
): TimeFormatter = TimeFormatterImpl(
    timeFormat = timeFormat,
    formatHour = formatHour,
    formatMinute = formatMinute,
    formatAmText = formatAmText,
    formatPmText = formatPmText,
)

@Composable
fun timeFormatter(
    locale: Locale,
): TimeFormatter {
    val lyricist = rememberStrings(currentLanguageTag = locale.language)

    return remember(lyricist, locale) {
        timeFormatter(
            strings = lyricist,
            timeFormat = when {
                locale.language == "en" || locale.region in listOf("US", "GB") -> TimeFormat.AM_PM
                else -> TimeFormat.HOUR_24
            },
        )
    }
}

@Composable
fun rememberStrings(
    currentLanguageTag: String,
): Strings {
    val translations = remember {
        mapOf(
            "en" to EnStrings,
            "ru" to RuStrings,
            // Добавьте другие языки по необходимости
        )
    }

    return remember(currentLanguageTag, translations) {
        val languageTag = when {
            translations.containsKey(currentLanguageTag) -> currentLanguageTag
            currentLanguageTag.startsWith("ru") -> "ru"
            currentLanguageTag.startsWith("zh") -> "zh"
            translations.keys.any { currentLanguageTag.startsWith(it) } ->
                translations.keys.first { currentLanguageTag.startsWith(it) }

            else -> "en" // fallback
        }

        translations[languageTag] ?: EnStrings
    }
}
