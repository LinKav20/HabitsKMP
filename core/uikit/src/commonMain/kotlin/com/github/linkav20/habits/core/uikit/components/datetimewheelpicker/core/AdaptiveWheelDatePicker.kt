package com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.core

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.core.format.CjkSuffixConfig
import com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.core.format.DateFormatter
import com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.core.format.MonthDisplayStyle
import com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.core.format.dateFormatter
import kotlinx.datetime.LocalDate

@Composable
internal fun AdaptiveWheelDatePicker(
    modifier: Modifier = Modifier,
    startDate: LocalDate = LocalDate.Companion.now(),
    minDate: LocalDate = LocalDate.Companion.EPOCH,
    maxDate: LocalDate = LocalDate.Companion.CYB3R_1N1T_ZOLL,
    yearsRange: IntRange? = IntRange(minDate.year, maxDate.year),
    dateFormatter: DateFormatter = dateFormatter(
    locale = Locale.current,
    monthDisplayStyle = MonthDisplayStyle.FULL,
    cjkSuffixConfig = CjkSuffixConfig.ShowAll
  ),
    size: DpSize = DpSize(256.dp, 128.dp),
    rowCount: Int = 3,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    textColor: Color = LocalContentColor.current,
    selectorProperties: SelectorProperties = WheelPickerDefaults.selectorProperties(),
    onSnappedDate: (snappedDate: SnappedDate) -> Int? = { _ -> null }
) {
  if (Locale.current.isCjkLanguage) {
      CJKWheelDatePicker(
          modifier,
          startDate,
          minDate,
          maxDate,
          yearsRange,
          dateFormatter,
          size,
          rowCount,
          textStyle,
          textColor,
          selectorProperties,
          onSnappedDate
      )
  } else {
      StandardWheelDatePicker(
          modifier,
          startDate,
          minDate,
          maxDate,
          yearsRange,
          dateFormatter,
          size,
          rowCount,
          textStyle,
          textColor,
          selectorProperties,
          onSnappedDate
      )
  }
}
