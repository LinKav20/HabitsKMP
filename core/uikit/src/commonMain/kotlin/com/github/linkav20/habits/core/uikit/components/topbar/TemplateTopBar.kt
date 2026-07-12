package com.github.linkav20.habits.core.uikit.components.topbar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.github.linkav20.habits.core.uikit.HabitIcons
import com.github.linkav20.habits.core.uikit.HabitTheme
import com.github.linkav20.habits.core.uikit.components.input.HabitInputField
import com.github.linkav20.habits.core.uikit.resources.Res
import com.github.linkav20.habits.core.uikit.resources.ic_24_search
import com.github.linkav20.habits.core.uikit.resources.search_placeholder_top_bar
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private val ICON_SIZE = 48.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitTopBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    titleFun: @Composable (() -> Unit)? = null,
    subtitle: String? = null,
    actions: List<HabitTopBarAction> = emptyList(),
    containerColor: Color = HabitTheme.colors.surfaceContainer,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    search: TemplateTopBarSearch? = null,
    onBackClick: (() -> Unit)? = null,
    content: @Composable (() -> Unit)? = null,
) {
    Column(modifier = modifier) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors().copy(
                containerColor = containerColor,
            ),
            windowInsets = windowInsets,
            title = {
                titleFun?.invoke() ?: TemplateTopBarTitle(
                    title = title,
                    subtitle = subtitle,
                    search = search,
                )
            },
            navigationIcon = {
                if (onBackClick != null) {
                    TopBarIcon(
                        icon = painterResource(HabitIcons.ChevronLeft_24),
                        onClick = onBackClick,
                    )
                }
            },
            actions = {
                actions.forEach { action ->
                    TopBarIcon(
                        icon = action.icon,
                        tint = action.tint,
                        onClick = action.onClick,
                    )
                }
            },
        )
        content?.invoke()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitCenterTopBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    subtitle: String? = null,
    actions: List<HabitTopBarAction> = emptyList(),
    containerColor: Color = HabitTheme.colors.surfaceContainer,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    search: TemplateTopBarSearch? = null,
    onBackClick: (() -> Unit)? = null,
    content: @Composable (() -> Unit)? = null,
) {
    Column(modifier = modifier) {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors().copy(
                containerColor = containerColor,
            ),
            windowInsets = windowInsets,
            title = {
                TemplateTopBarTitle(
                    title = title,
                    subtitle = subtitle,
                    search = search,
                )
            },
            navigationIcon = {
                if (onBackClick != null) {
                    TopBarIcon(
                        icon = painterResource(HabitIcons.ChevronLeft_24),
                        onClick = onBackClick,
                    )
                }
            },
            actions = {
                actions.forEach { action ->
                    TopBarIcon(
                        icon = action.icon,
                        onClick = action.onClick,
                    )
                }
            },
        )
        content?.invoke()
    }
}

@Composable
private fun TemplateTopBarTitle(
    title: String?,
    subtitle: String?,
    search: TemplateTopBarSearch?,
) {
    Column {
        if (search != null) {
            HabitInputField(
                value = search.value,
                onValueChange = search.onValueChanged,
                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.ic_24_search),
                        tint = HabitTheme.colors.iconPrimary,
                        contentDescription = null,
                    )
                },
                placeholder = stringResource(search.placeholder),
                placeholderMaxLines = 1,
            )
        } else {
            if (title != null) {
                Text(
                    text = title,
                    color = HabitTheme.colors.textPrimary,
                    style = HabitTheme.typography.h2.copy(
                        fontStyle = FontStyle.Italic,
                    ),
                )
            }
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    color = HabitTheme.colors.textSecondary,
                    style = HabitTheme.typography.body,
                )
            }
        }
    }
}

data class HabitTopBarAction(
    val icon: Painter,
    val tint: Color? = null,
    val onClick: () -> Unit,
)

data class TemplateTopBarSearch(
    val value: String,
    val placeholder: StringResource = Res.string.search_placeholder_top_bar,
    val onValueChanged: (String) -> Unit,
)

@Composable
private fun TopBarIcon(
    icon: Painter,
    tint: Color? = null,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(ICON_SIZE),
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = tint ?: HabitTheme.colors.iconAccentSecondary,
        )
    }
}
