package com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.core.format

import androidx.compose.ui.text.intl.Locale
import com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.strings.EnStrings
import com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.strings.Strings

internal fun Int.toLocalizedNumerals(locale: Locale = Locale.current): String {
  return this.toString().toLocalizedNumerals(locale)
}

internal fun String.toLocalizedNumerals(locale: Locale = Locale.current): String {
  return if (locale.language in arrayOf("ar", "fa")) {
    val strings =   EnStrings
    this.toLocalizedNumerals(strings)
  } else {
    this
  }
}

internal fun Int.toLocalizedNumerals(strings: Strings): String {
  return this.toString().toLocalizedNumerals(strings)
}

internal fun String.toLocalizedNumerals(strings: Strings): String {
  return this.map { digit ->
    when (digit) {
      '0' -> strings.digit0
      '1' -> strings.digit1
      '2' -> strings.digit2
      '3' -> strings.digit3
      '4' -> strings.digit4
      '5' -> strings.digit5
      '6' -> strings.digit6
      '7' -> strings.digit7
      '8' -> strings.digit8
      '9' -> strings.digit9
      else -> digit.toString()
    }
  }.joinToString("")
}
