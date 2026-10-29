package com.github.linkav20.habits.core.uikit.emojipicker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.linkav20.habits.core.uikit.HabitIcons
import com.github.linkav20.habits.core.uikit.HabitTheme
import com.github.linkav20.habits.core.uikit.components.chip.HabitFilterChip
import com.github.linkav20.habits.core.uikit.resources.Res
import com.github.linkav20.habits.core.uikit.resources.choose_emoji_title
import com.github.linkav20.habits.core.uikit.resources.close
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmojiPicker(
    selectedEmoji: String,
    onSelectEmoji: (String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onClose,
        sheetState = sheetState,
        dragHandle = null,
        containerColor = HabitTheme.colors.primaryContainer,
        contentColor = HabitTheme.colors.textPrimary,
        shape = HabitTheme.shapes.extraLarge.copy(
            bottomStart = CornerSize(0.dp),
            bottomEnd = CornerSize(0.dp),
        ),
    ) {
        Column(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8f)) {
            Spacer(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp)
                    .size(width = 32.dp, height = 4.dp)
                    .background(HabitTheme.colors.backgroundSecondary, RoundedCornerShape(50)),
            )
            EmojiPickerHeader(
                onClose = {
                    scope.launch {
                        sheetState.hide()
                        onClose()
                    }
                },
            )
            EmojiPickerContent(
                selectedEmoji = selectedEmoji,
                onSelectEmoji = { emoji ->
                    scope.launch {
                        sheetState.hide()
                        onSelectEmoji(emoji)
                    }
                },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun EmojiPickerHeader(onClose: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(Res.string.choose_emoji_title),
            modifier = Modifier.weight(1f),
            style = HabitTheme.typography.h2.copy(fontStyle = FontStyle.Italic),
            color = HabitTheme.colors.textPrimary,
        )
        IconButton(onClick = onClose) {
            Icon(
                painter = painterResource(HabitIcons.Cross_24),
                contentDescription = stringResource(Res.string.close),
                tint = HabitTheme.colors.iconSecondary,
            )
        }
    }
}

@Composable
private fun EmojiPickerContent(
    selectedEmoji: String,
    onSelectEmoji: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedCategoryIndex by rememberSaveable { mutableIntStateOf(0) }
    val categories by produceState<ImmutableList<EmojiPickerCategory>>(initialValue = persistentListOf()) {
        value = withContext(Dispatchers.Default) { emojiPickerCategories }
    }

    if (categories.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = HabitTheme.colors.textAccentPrimary)
        }
    } else {
        Column(modifier = modifier.fillMaxWidth()) {
            EmojiCategoryTabs(
                categories = categories,
                selectedCategoryIndex = selectedCategoryIndex,
                onCategorySelected = { selectedCategoryIndex = it },
            )
            key(selectedCategoryIndex) {
                EmojiGrid(
                    emojis = categories[selectedCategoryIndex].emojis,
                    selectedEmoji = selectedEmoji,
                    onSelectEmoji = onSelectEmoji,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun EmojiCategoryTabs(
    categories: ImmutableList<EmojiPickerCategory>,
    selectedCategoryIndex: Int,
    onCategorySelected: (Int) -> Unit,
) {
    LazyRow(
        contentPadding = PaddingValues(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        itemsIndexed(categories) { index, category ->
            HabitFilterChip(
                title = stringResource(category.title),
                selected = selectedCategoryIndex == index,
                onClick = { onCategorySelected(index) },
            )
        }
    }
}

@Composable
private fun EmojiGrid(
    emojis: ImmutableList<String>,
    selectedEmoji: String,
    onSelectEmoji: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val gridState = rememberLazyGridState()
    val surfaceColor = HabitTheme.colors.primaryContainer

    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Adaptive(minSize = 52.dp),
        modifier = modifier
            .fillMaxWidth()
            .nestedScroll(EmojiGridScrollConnection)
            .emojiBottomFade(gridState, surfaceColor),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        items(items = emojis, key = { it }) { emoji ->
            EmojiCell(
                emoji = emoji,
                selected = selectedEmoji == emoji,
                onClick = { onSelectEmoji(emoji) },
            )
        }
    }
}

private fun Modifier.emojiBottomFade(
    gridState: LazyGridState,
    surfaceColor: Color,
): Modifier = drawWithContent {
    drawContent()
    if (gridState.canScrollForward) {
        val fadeHeight = 32.dp.toPx().coerceAtMost(size.height)
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(surfaceColor.copy(alpha = 0f), surfaceColor),
                startY = size.height - fadeHeight,
                endY = size.height,
            ),
            topLeft = Offset(0f, size.height - fadeHeight),
            size = Size(size.width, fadeHeight),
        )
    }
}

private object EmojiGridScrollConnection : NestedScrollConnection {
    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource,
    ): Offset = Offset(0f, available.y)

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity = Velocity(0f, available.y)
}

@Composable
private fun EmojiCell(
    emoji: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(HabitTheme.shapes.medium)
            .background(if (selected) HabitTheme.colors.secondaryContainer else Color.Transparent)
            .border(
                width = 1.dp,
                color = if (selected) HabitTheme.colors.textAccentPrimary else Color.Transparent,
                shape = HabitTheme.shapes.medium,
            )
            .selectable(selected = selected, role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = emoji,
            fontSize = 30.sp,
            lineHeight = 36.sp,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
        )
    }
}
