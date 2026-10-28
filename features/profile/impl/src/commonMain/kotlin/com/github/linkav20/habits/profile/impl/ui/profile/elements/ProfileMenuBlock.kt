package com.github.linkav20.habits.profile.impl.ui.profile.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.linkav20.habits.core.uikit.HabitTheme

internal data class MenuItem(
    val title: String,
    val action: () -> Unit,
)

@Composable
internal fun ProfileMenuBlock(
    items: List<MenuItem>,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(HabitTheme.shapes.large)
            .background(HabitTheme.colors.surfaceContainerLowest),
    ) {
        Column {
            items.forEachIndexed { index, item ->
                if (index > 0) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = HabitTheme.colors.surfaceContainerHighest,
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = if (items.size == 1) 68.dp else 56.dp)
                        .clickable(
                            role = Role.Button,
                            onClick = item.action,
                        )
                        .padding(horizontal = 22.dp, vertical = 12.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Text(
                        text = item.title,
                        style = HabitTheme.typography.body,
                        color = HabitTheme.colors.textPrimary,
                    )
                }
            }
        }
    }
}
