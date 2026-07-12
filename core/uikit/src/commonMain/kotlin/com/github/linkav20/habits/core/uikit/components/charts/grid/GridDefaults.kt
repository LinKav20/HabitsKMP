package com.github.linkav20.habits.core.uikit.components.charts.grid

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.linkav20.habits.core.uikit.HabitTheme

internal object GridDefaults {

    val HORIZONTAL_LINES_OFFSET = 10.dp
    const val NUMBER_OF_GRID_HORIZONTAL_LINES = 10
    const val NUMBER_OF_GRID_VERTICAL_LINES = 7
    const val MIN_NUMBER_OF_GRID_HORIZONTAL_LINES = 0
    const val MIN_NUMBER_OF_GRID_VERTICAL_LINES = 0
    const val ROUND_MIN_MAX_CLOSEST_TO = 50

    val YAxisLabel: @Composable (value: Any) -> Unit = { value ->
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            fontSize = 12.sp,
            text = value.toString(),
            textAlign = TextAlign.End,
            maxLines = 1,
        )
    }

    val OverlayHeaderLabel: @Composable (value: Any) -> Unit = { value ->
        Text(
            text = value.toString(),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = HabitTheme.typography.body,
        )
    }

    val OverlayDataEntryLabel: @Composable (dataName: String, value: Any) -> Unit = { dataName, value ->
        Text(
            text = "$dataName: $value",
        )
    }

    val XAxisLabel: @Composable (value: Any) -> Unit = { value ->
        Text(
            fontSize = 12.sp,
            text = value.toString(),
            textAlign = TextAlign.Center,
        )
    }

    val LegendItemLabel: @Composable (String) -> Unit = {
        Text(
            text = it,
        )
    }
}
