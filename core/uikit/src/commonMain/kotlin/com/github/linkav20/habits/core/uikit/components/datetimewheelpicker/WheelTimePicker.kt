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
import com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.core.StandardWheelTimePicker
import com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.core.MAX
import com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.core.MIN
import com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.core.SelectorProperties
import com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.core.WheelPickerDefaults
import com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.core.format.TimeFormatter
import com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.core.format.timeFormatter
import com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.core.now
import kotlinx.datetime.LocalTime

@Composable
fun WheelTimePicker(
  modifier: Modifier = Modifier,
  startTime: LocalTime = LocalTime.now(),
  minTime: LocalTime = LocalTime.MIN,
  maxTime: LocalTime = LocalTime.MAX,
  timeFormatter: TimeFormatter = timeFormatter(Locale.current),
  size: DpSize = DpSize(128.dp, 128.dp),
  rowCount: Int = 3,
  textStyle: TextStyle = MaterialTheme.typography.titleMedium,
  textColor: Color = LocalContentColor.current,
  selectorProperties: SelectorProperties = WheelPickerDefaults.selectorProperties(),
  onSnappedTime: (snappedTime: LocalTime) -> Unit = {},
) {
  StandardWheelTimePicker(
    modifier,
    startTime,
    minTime,
    maxTime,
    timeFormatter,
    size,
    rowCount,
    textStyle,
    textColor,
    selectorProperties,
    onSnappedTime = { snappedTime, _ ->
      onSnappedTime(snappedTime.snappedLocalTime)
      snappedTime.snappedIndex
    }
  )
}
