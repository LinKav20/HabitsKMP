package com.github.linkav20.habits.core.uikit.insets

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Modifier.applySystemBarsHorizontalPaddings(): Modifier {
    return windowInsetsPadding(
        WindowInsets.systemBars.only(
            WindowInsetsSides.Horizontal,
        ),
    )
}
