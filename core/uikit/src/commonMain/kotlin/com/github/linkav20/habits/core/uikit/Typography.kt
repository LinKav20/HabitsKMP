package com.github.linkav20.habits.core.uikit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.github.linkav20.habits.core.uikit.resources.Res
import com.github.linkav20.habits.core.uikit.resources.days_one
import com.github.linkav20.habits.core.uikit.resources.google_sans
import org.jetbrains.compose.resources.Font

@Composable
private fun DefaultTextStyle(
    size: TextUnit,
    lineHeight: TextUnit = TextUnit.Unspecified,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    family: FontFamily = FontFamily(Font(Res.font.google_sans)),
    weight: FontWeight = FontWeight.Normal,
) = TextStyle.Default.copy(
    fontSize = size,
    fontFamily = family,
    fontWeight = weight,
    lineHeight = lineHeight,
    letterSpacing = letterSpacing,
)

@Composable
private fun AccentDefaultTextStyle(
    size: TextUnit,
    lineHeight: TextUnit,
    family: FontFamily = FontFamily(Font(Res.font.days_one)),
    weight: FontWeight = FontWeight.Normal,
) = TextStyle.Default.copy(
    fontSize = size,
    fontFamily = family,
    fontWeight = weight,
    lineHeight = lineHeight,
)

@Suppress("LongParameterList")
@Stable
data class HabitTypography(
    val title: TextStyle,
    val subtitle: TextStyle,
    val body: TextStyle,
    val textSmall: TextStyle,

    val h1: TextStyle,
    val h2: TextStyle,
    val bodyAccent: TextStyle,
)

internal val LocalTypography =
    staticCompositionLocalOf<HabitTypography> {
        error("No TemplateTypography provided")
    }

@Suppress("LongMethod")
@Composable
internal fun templateTypography(): HabitTypography =
    HabitTypography(
        title = DefaultTextStyle(
            size = 32.sp,
            lineHeight = 72.sp,
            weight = FontWeight.ExtraBold,
        ),
        subtitle = DefaultTextStyle(
            size = 22.sp,
            lineHeight = 36.sp,
            weight = FontWeight.ExtraBold,
        ),
        body = DefaultTextStyle(
            size = 18.sp,
            lineHeight = 24.sp,
            weight = FontWeight(550),
        ),
        textSmall = DefaultTextStyle(
            size = 14.sp,
            lineHeight = 18.sp,
            weight = FontWeight(550),
        ),
        h1 = AccentDefaultTextStyle(
            size = 32.sp,
            lineHeight = 72.sp,
            weight = FontWeight.Bold,
        ),
        h2 = AccentDefaultTextStyle(
            size = 30.sp,
            lineHeight = 42.sp,
            weight = FontWeight.Medium,
        ),
        bodyAccent = AccentDefaultTextStyle(
            size = 18.sp,
            lineHeight = 20.sp,
            weight = FontWeight.Bold,
        ),
    )
