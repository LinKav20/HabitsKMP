package com.github.linkav20.habits.core.uikit.components.snackbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.linkav20.habits.core.uikit.HabitIcons
import com.github.linkav20.habits.core.uikit.HabitTheme
import com.github.linkav20.habits.core.uikit.resources.Res
import com.github.linkav20.habits.core.uikit.resources.ic_24_cross_vector
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun TemplateSnackBar(
    snackBarData: ImmutableList<HabitSnackBarData>,
    onClick: (HabitSnackBarData) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier.fillMaxSize(),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.systemBars)
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(items = snackBarData) { data ->
                AnimatedVisibility(
                    visibleState = data.visible,
                    enter = expandVertically(),
                    exit = shrinkVertically(),
                ) {
                    TemplateSnackBarItem(
                        data = data,
                        onClick = {
                            onClick.invoke(data)
                            data.action?.invoke()
                        },
                        modifier = Modifier,
                    )
                }
            }
        }
    }
}

@Suppress("SpreadOperator")
@Composable
internal fun TemplateSnackBarItem(
    data: HabitSnackBarData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = 2.dp,
            color = data.type.toColor(),
        ),
    ) {
        Column(
            modifier = Modifier.Companion
                .background(HabitTheme.colors.primaryContainer)
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(data.type.toIcon()),
                    contentDescription = null,
                    tint = data.type.toColor(),
                    modifier = Modifier.size(24.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text =
                        data.titleText
                            ?: data.titleRes?.let { stringResource(it, *data.titleFormatArgs.toTypedArray()) }
                            ?: "",
                    modifier = Modifier.weight(1f),
                    style = HabitTheme.typography.body,
                    fontWeight = FontWeight.Bold,
                    color = HabitTheme.colors.textPrimary,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(Res.drawable.ic_24_cross_vector),
                    contentDescription = null,
                    tint = HabitTheme.colors.iconPrimary,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Top)
                        .clickable { onClick.invoke() },
                )
            }
            if (!data.messageText.isNullOrBlank() || data.messageRes != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text =
                        data.messageText
                            ?: data.messageRes?.let { stringResource(it, *data.messageFormatArgs.toTypedArray()) }
                            ?: "",
                    style = HabitTheme.typography.body,
                    fontWeight = FontWeight.W400,
                    color = HabitTheme.colors.textPrimary,
                )
            }
        }
    }
}

@Stable
data class HabitSnackBarData(
    val deduplicateId: String? = null,
    val titleText: String? = null,
    val titleRes: StringResource? = null,
    val titleFormatArgs: List<Any> = emptyList(),
    val messageText: String? = null,
    val messageRes: StringResource? = null,
    val messageFormatArgs: List<Any> = emptyList(),
    val type: HabitSnackBarType = HabitSnackBarType.INFO,
    val action: (() -> Unit)? = null,
    val duration: HabitSnackBarDuration = HabitSnackBarDuration.Short,
    // The visible field is needed to control the appearance/disappearance of a SnackBar with animation.
    val visible: MutableTransitionState<Boolean> =
        MutableTransitionState(false).apply { targetState = true },
)

@Stable
enum class HabitSnackBarDuration {
    Short,
    Long,
    Indefinite,
}

@Stable
enum class HabitSnackBarType {
    SUCCESS,
    INFO,
    ERROR,
    WARNING,
}

@Composable
private fun HabitSnackBarType.toColor(): Color {
    return when (this) {
        HabitSnackBarType.SUCCESS -> HabitTheme.colors.textSuccess
        HabitSnackBarType.INFO -> HabitTheme.colors.textInfo
        HabitSnackBarType.ERROR -> HabitTheme.colors.textError
        HabitSnackBarType.WARNING -> HabitTheme.colors.textWarning
    }
}

@Composable
private fun HabitSnackBarType.toIcon(): DrawableResource {
    return when (this) {
        HabitSnackBarType.SUCCESS -> HabitIcons.Success_24
        HabitSnackBarType.INFO -> HabitIcons.Info_24
        HabitSnackBarType.ERROR -> HabitIcons.Error_24
        HabitSnackBarType.WARNING -> HabitIcons.Info_24
    }
}
