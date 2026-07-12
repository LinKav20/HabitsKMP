@file:Suppress("TooManyFunctions")

package com.github.linkav20.habits.core.uikit.components.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.linkav20.habits.core.uikit.HabitTheme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

private val LOADER_SIZE = 24.dp
private val LOADER_STROKE_WIDTH = 3.dp

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongParameterList")
@Composable
fun HabitButton(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentColor: Color? = null,
    containerColor: Color? = null,
    icon: DrawableResource? = null,
    iconSize: Dp? = null,
    tailingIcon: DrawableResource? = null,
    state: HabitButtonState = HabitButtonState.DEFAULT,
    type: HabitButtonType = HabitButtonType.PRIMARY,
    size: HabitButtonSize = HabitButtonSize.M,
    contentPadding: PaddingValues = getPaddings(size),
) {
    CompositionLocalProvider(
        LocalRippleConfiguration provides getRippleConfiguration(
            type = type,
            contentColor = contentColor,
        ),
    ) {
        val buttonContainerColor = getContainerColor(
            type = type,
            containerColor = containerColor,
        )
        val disabledContainerColor = getDisabledContainerColor(
            type = type,
            state = state,
        )
        val buttonContentColor = getContentColor(
            contentColor = contentColor,
            type = type,
        )

        val disabledButtonContentColor = getDisabledContentColor(
            contentColor = contentColor,
            type = type,
        )

        val border = getBorder(
            contentColor = contentColor,
            state = state,
            type = type,
        )
        val height = getHeight(size)

        Button(
            modifier = modifier.height(height),
            onClick = onClick,
            shape = if (type == HabitButtonType.TERTIARY) {
                HabitTheme.shapes.extraLarge
            } else {
                HabitTheme.shapes.medium
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonContainerColor,
                contentColor = buttonContentColor,
                disabledContentColor = disabledButtonContentColor,
                disabledContainerColor = disabledContainerColor,
            ),
            border = border,
            enabled = state == HabitButtonState.DEFAULT,
            contentPadding = contentPadding,
        ) {
            if (state == HabitButtonState.LOADING) {
                LoadingContent(type = type, contentColor = contentColor)
            } else {
                DefaultContent(
                    icon = icon,
                    iconSize = iconSize,
                    tailingIcon = tailingIcon,
                    title = title,
                    color = if (state == HabitButtonState.DISABLED) {
                        disabledButtonContentColor
                    } else {
                        buttonContentColor
                    },
                )
            }
        }
    }
}

enum class HabitButtonType {
    PRIMARY,
    SECONDARY,
    TERTIARY,
    WHITE,
    OUTLINE,
    GHOST,
}

enum class HabitButtonState {
    DEFAULT,
    DISABLED,
    LOADING,
}

enum class HabitButtonSize {
    S,
    M,
    L,
    XL,
}

@Composable
private fun getHeight(size: HabitButtonSize) =
    when (size) {
        HabitButtonSize.XL -> 68.dp
        HabitButtonSize.L -> 56.dp
        HabitButtonSize.M -> 48.dp
        HabitButtonSize.S -> 36.dp
    }

@Composable
private fun getPaddings(size: HabitButtonSize) =
    when (size) {
        HabitButtonSize.XL -> PaddingValues(horizontal = 28.dp, vertical = 24.dp)
        HabitButtonSize.L -> PaddingValues(horizontal = 24.dp, vertical = 16.dp)
        HabitButtonSize.M -> PaddingValues(horizontal = 16.dp, vertical = 12.dp)
        HabitButtonSize.S -> PaddingValues(horizontal = 16.dp, vertical = 6.dp)
    }

@Composable
private fun getBorder(
    type: HabitButtonType,
    state: HabitButtonState,
    contentColor: Color?,
) = if (type == HabitButtonType.OUTLINE) {
    BorderStroke(
        width = 2.dp,
        color = if (state == HabitButtonState.DISABLED) {
            HabitTheme.colors.borderDisabled
        } else {
            contentColor ?: HabitTheme.colors.borderAccentPrimary
        },
    )
} else {
    null
}

@Composable
private fun getContainerColor(
    type: HabitButtonType,
    containerColor: Color?,
) = when (type) {
    HabitButtonType.PRIMARY -> containerColor ?: HabitTheme.colors.iconAccentPrimary
    HabitButtonType.SECONDARY -> containerColor ?: HabitTheme.colors.background
    HabitButtonType.TERTIARY -> containerColor ?: HabitTheme.colors.backgroundTertiary
    HabitButtonType.GHOST -> Color.Transparent
    HabitButtonType.OUTLINE -> containerColor ?: Color.Transparent
    HabitButtonType.WHITE -> containerColor ?: HabitTheme.colors.background
}

@Composable
private fun getDisabledContainerColor(
    type: HabitButtonType,
    state: HabitButtonState,
) = when (type) {
    HabitButtonType.PRIMARY -> {
        if (state == HabitButtonState.LOADING) {
            HabitTheme.colors.primaryContainer
        } else {
            HabitTheme.colors.surfaceContainer
        }
    }

    HabitButtonType.SECONDARY -> {
        if (state == HabitButtonState.LOADING) {
            HabitTheme.colors.surfaceContainer
        } else {
            HabitTheme.colors.primaryContainer
        }
    }

    HabitButtonType.TERTIARY -> {
        if (state == HabitButtonState.LOADING) {
            HabitTheme.colors.surfaceContainer
        } else {
            HabitTheme.colors.primaryContainer
        }
    }

    HabitButtonType.GHOST -> Color.Transparent

    HabitButtonType.OUTLINE -> Color.Transparent

    HabitButtonType.WHITE -> Color.Black
}

@Composable
private fun getContentColor(
    contentColor: Color?,
    type: HabitButtonType,
): Color {
    return contentColor ?: when (type) {
        HabitButtonType.PRIMARY -> HabitTheme.colors.iconInverse
        HabitButtonType.SECONDARY -> HabitTheme.colors.secondary
        HabitButtonType.TERTIARY -> HabitTheme.colors.textSecondary
        HabitButtonType.GHOST -> HabitTheme.colors.primary
        HabitButtonType.OUTLINE -> HabitTheme.colors.iconAccentPrimary
        HabitButtonType.WHITE -> Color.Black
    }
}

@Composable
private fun getDisabledContentColor(
    contentColor: Color?,
    type: HabitButtonType,
): Color {
    return contentColor ?: when (type) {
        HabitButtonType.PRIMARY -> HabitTheme.colors.iconInverse
        HabitButtonType.SECONDARY -> HabitTheme.colors.secondary
        HabitButtonType.TERTIARY -> HabitTheme.colors.textDisabled
        HabitButtonType.GHOST -> HabitTheme.colors.primary
        HabitButtonType.OUTLINE -> HabitTheme.colors.textDisabled
        HabitButtonType.WHITE -> HabitTheme.colors.textDisabled
    }
}

@Composable
private fun LoadingContent(
    type: HabitButtonType,
    contentColor: Color?,
) {
    CircularProgressIndicator(
        modifier = Modifier.size(LOADER_SIZE),
        strokeWidth = LOADER_STROKE_WIDTH,
        color = when (type) {
            HabitButtonType.PRIMARY -> HabitTheme.colors.iconInverse
            HabitButtonType.SECONDARY -> contentColor ?: HabitTheme.colors.secondary
            HabitButtonType.TERTIARY -> HabitTheme.colors.textSecondary
            HabitButtonType.GHOST -> contentColor ?: HabitTheme.colors.primary
            HabitButtonType.OUTLINE -> contentColor ?: HabitTheme.colors.primary
            HabitButtonType.WHITE -> Color.Black
        },
    )
}

@Composable
private fun DefaultContent(
    icon: DrawableResource?,
    iconSize: Dp?,
    tailingIcon: DrawableResource?,
    title: String,
    color: Color?,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Image(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(iconSize ?: ButtonDefaults.IconSize)
                    .align(Alignment.CenterVertically),
                colorFilter = ColorFilter.tint(color ?: Color.Transparent),
            )
            if (title.isNotBlank()) {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
        Text(
            text = title,
            style = HabitTheme.typography.body,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.align(Alignment.CenterVertically),
        )

        if (tailingIcon != null) {
            Spacer(modifier = Modifier.width(8.dp))

            Image(
                painter = painterResource(tailingIcon),
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.IconSize)
                    .align(Alignment.CenterVertically),
                colorFilter = ColorFilter.tint(color ?: Color.Transparent),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun getRippleConfiguration(
    contentColor: Color?,
    type: HabitButtonType,
): RippleConfiguration {
    return RippleConfiguration(
        color = contentColor?.copy(alpha = 0.4f) ?: when (type) {
            HabitButtonType.PRIMARY -> HabitTheme.colors.primary
            HabitButtonType.SECONDARY -> HabitTheme.colors.secondary
            HabitButtonType.TERTIARY -> HabitTheme.colors.surfaceContainer
            HabitButtonType.GHOST -> HabitTheme.colors.surfaceContainer
            HabitButtonType.OUTLINE -> HabitTheme.colors.surfaceContainer
            HabitButtonType.WHITE -> HabitTheme.colors.surfaceContainer
        },
        rippleAlpha = RippleAlpha(
            pressedAlpha = 0.7f,
            focusedAlpha = 0.4f,
            draggedAlpha = 0.4f,
            hoveredAlpha = 0.4f,
        ),
    )
}
