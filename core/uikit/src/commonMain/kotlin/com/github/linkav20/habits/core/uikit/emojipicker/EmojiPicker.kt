package com.github.linkav20.habits.core.uikit.emojipicker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.github.linkav20.habits.core.uikit.HabitTheme
import com.github.linkav20.habits.core.uikit.resources.Res
import com.github.linkav20.habits.core.uikit.resources.choose_emoji_title
import org.jetbrains.compose.resources.stringResource
import org.kodein.emoji.Emoji
import org.kodein.emoji.allGroups
import org.kodein.emoji.allOf
import org.kodein.emoji.list

@Composable
fun EmojiPicker(
    selectedEmoji: String,
    onSelectEmoji: (String) -> Unit,
    onClose: () -> Unit,
    columnsCount: Int = 6,
    modifier: Modifier = Modifier,
) {
    Dialog(
        onDismissRequest = onClose,
    ) {
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(
                containerColor = HabitTheme.colors.primaryContainer,
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp,
            ),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.choose_emoji_title),
                    color = HabitTheme.colors.textPrimary,
                    style = HabitTheme.typography.h2.copy(fontStyle = FontStyle.Italic),
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(columnsCount),
                ) {
                    items(items = Emoji.list().toList()) { emoji ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .then(
                                    if (selectedEmoji == emoji.toString()) {
                                        Modifier.background(
                                            color = HabitTheme.colors.background,
                                            shape = HabitTheme.shapes.small,
                                        ).clip(HabitTheme.shapes.small)
                                    } else {
                                        Modifier
                                    },
                                ).clickable {
                                    onSelectEmoji(emoji.toString())
                                },
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = emoji.toString(),
                                style = HabitTheme.typography.subtitle,
                            )
                        }
                    }
                }
            }
        }
    }
}
