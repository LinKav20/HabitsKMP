package com.github.linkav20.habits.core.uikit.components.avatar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.github.linkav20.habits.core.uikit.HabitTheme

private const val defaultAvatarLetter = "U"

@Composable
fun Avatar(
    name: String?,
    photoUrl: String?,
    modifier: Modifier = Modifier,
    shape: Shape = HabitTheme.shapes.large,
    size: Dp = 96.dp,
    styleTitle: TextStyle = HabitTheme.typography.title,
) {
    val initials = if (!name.isNullOrBlank()) {
        name.first().uppercase()
    } else {
        defaultAvatarLetter
    }
    Card(
        modifier = modifier.size(size),
        shape = shape,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            if (!photoUrl.isNullOrBlank()) {
                CoilAvatar(
                    imageUrl = photoUrl,
                    modifier = Modifier
                        .size(size),
                )
            } else {
                Text(
                    text = initials,
                    fontWeight = W400,
                    style = styleTitle,
                    color = HabitTheme.colors.textInfo,
                )
            }
        }
    }
}

@Composable
private fun CoilAvatar(
    imageUrl: String,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = ImageRequest
            .Builder(context = LocalPlatformContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        placeholder = null,
        error = null,
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = modifier,
    )
}
