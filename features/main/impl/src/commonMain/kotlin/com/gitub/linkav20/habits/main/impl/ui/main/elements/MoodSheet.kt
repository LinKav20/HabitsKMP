package com.gitub.linkav20.habits.main.impl.ui.main.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.linkav20.habits.core.uikit.HabitIcons
import com.github.linkav20.habits.core.uikit.HabitTheme
import com.github.linkav20.habits.core.uikit.components.button.HabitButton
import com.github.linkav20.habits.core.uikit.components.slider.HabitSeekBar
import com.gitub.linkav20.habits.main.api.domain.model.main.toEmoji
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import kotlin.math.round

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodSheetContent(
    onSaveMood: (Int) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val modelBottomSheetState = rememberModalBottomSheetState()

    val coroutineScope = rememberCoroutineScope()
    val hideModalBottomSheet: () -> Unit =
        {
            coroutineScope.launch {
                modelBottomSheetState.hide()
                onClose()
            }
        }

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = hideModalBottomSheet,
        sheetState = modelBottomSheetState,
        containerColor = HabitTheme.colors.primaryContainer,
        shape = HabitTheme.shapes.large,
        dragHandle = null,
    ) {
        MoodContent(
            onCloseClick = hideModalBottomSheet,
            onSaveMood = onSaveMood,
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 16.dp,
            ).fillMaxWidth(),
        )
    }
}

@Composable
private fun MoodContent(
    onCloseClick: () -> Unit,
    onSaveMood: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var rate by remember { mutableFloatStateOf(9f / 2) }
    Column(modifier = modifier) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd,
        ) {
            Icon(
                modifier = Modifier.clickable { onCloseClick() },
                painter = painterResource(HabitIcons.Cross_24),
                contentDescription = null,
                tint = HabitTheme.colors.iconSecondary,
            )
        }
        Column(
            modifier = Modifier.padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "Как вы себя сегодня чувствуете?",
                color = HabitTheme.colors.textPrimary,
                style = HabitTheme.typography.subtitle,
            )
            Text(
                text = round(rate + 0.001f).toInt().toEmoji(),
                style = HabitTheme.typography.title.copy(fontSize = 72.sp),
            )
            HabitSeekBar(
                modifier = Modifier.padding(4.dp),
                progress = rate,
                maxProgress = 9f,
                onValueChange = { rate = it },
                showThumbAlways = true,
                trackHeight = 6.dp,
                thumbRadius = 8.dp,
            )
            HabitButton(
                modifier = Modifier.fillMaxWidth(),
                title = "Save",
                onClick = {
                    onSaveMood(round(rate + 0.001f).toInt())
                    onCloseClick()
                },
            )
        }
    }
}
