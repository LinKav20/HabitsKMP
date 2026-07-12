package com.github.linkav20.habits.core.uikit.components.datetimewheelpicker

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.core.AdaptiveWheelDateTimePicker
import com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.core.CYB3R_1N1T_ZOLL
import com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.core.EPOCH
import com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.core.SelectorProperties
import com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.core.WheelPickerDefaults
import com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.core.format.CjkSuffixConfig
import com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.core.format.DateFormatter
import com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.core.format.MonthDisplayStyle
import com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.core.format.TimeFormatter
import com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.core.format.dateFormatter
import com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.core.format.timeFormatter
import com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.core.now
import kotlinx.datetime.LocalDateTime

@Composable
fun WheelDateTimePicker(
  modifier: Modifier = Modifier,
  startDateTime: LocalDateTime = LocalDateTime.now(),
  minDateTime: LocalDateTime = LocalDateTime.EPOCH,
  maxDateTime: LocalDateTime = LocalDateTime.CYB3R_1N1T_ZOLL,
  yearsRange: IntRange? = IntRange(minDateTime.year, maxDateTime.year),
  dateFormatter: DateFormatter = dateFormatter(
    locale = Locale.current,
    monthDisplayStyle = MonthDisplayStyle.SHORT,
    cjkSuffixConfig = CjkSuffixConfig.HideAll
  ),
  timeFormatter: TimeFormatter = timeFormatter(Locale.current),
  size: DpSize = DpSize(256.dp, 128.dp),
  rowCount: Int = 3,
  textStyle: TextStyle = MaterialTheme.typography.titleMedium,
  textColor: Color = LocalContentColor.current,
  selectorProperties: SelectorProperties = WheelPickerDefaults.selectorProperties(),
  onSnappedDateTime: (snappedDateTime: LocalDateTime) -> Unit = {}
) {
  AdaptiveWheelDateTimePicker(
    modifier,
    startDateTime,
    minDateTime,
    maxDateTime,
    yearsRange,
    dateFormatter,
    timeFormatter,
    size,
    rowCount,
    textStyle,
    textColor,
    selectorProperties,
    onSnappedDateTime = { snappedDateTime ->
      onSnappedDateTime(snappedDateTime.snappedLocalDateTime)
      snappedDateTime.snappedIndex
    }
  )
}
