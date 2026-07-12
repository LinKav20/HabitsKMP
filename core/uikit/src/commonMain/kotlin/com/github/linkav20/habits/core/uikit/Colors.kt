package com.github.linkav20.habits.core.uikit

import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ColorScheme as M3ColorScheme

internal val LocalIsDarkModeScheme = compositionLocalOf { false }
internal val LocalColorScheme = compositionLocalOf { lightColorScheme() }

internal fun lightColorScheme(): HabitColorScheme {
    return HabitColorScheme(
        primary = TemplateColor.primaryLight,
        onPrimary = TemplateColor.onPrimaryLight,
        primaryContainer = TemplateColor.primaryContainerLight,
        onPrimaryContainer = TemplateColor.onPrimaryContainerLight,
        secondary = TemplateColor.secondaryLight,
        onSecondary = TemplateColor.onSecondaryLight,
        secondaryContainer = TemplateColor.secondaryContainerLight,
        onSecondaryContainer = TemplateColor.onSecondaryContainerLight,
        tertiary = TemplateColor.tertiaryLight,
        onTertiary = TemplateColor.onTertiaryLight,
        tertiaryContainer = TemplateColor.tertiaryContainerLight,
        onTertiaryContainer = TemplateColor.onTertiaryContainerLight,
        error = TemplateColor.errorLight,
        onError = TemplateColor.onErrorLight,
        errorContainer = TemplateColor.errorContainerLight,
        onErrorContainer = TemplateColor.onErrorContainerLight,
        background = TemplateColor.backgroundLight,
        onBackground = TemplateColor.onBackgroundLight,
        surface = TemplateColor.surfaceLight,
        onSurface = TemplateColor.onSurfaceLight,
        surfaceVariant = TemplateColor.surfaceVariantLight,
        onSurfaceVariant = TemplateColor.onSurfaceVariantLight,
        outline = TemplateColor.outlineLight,
        outlineVariant = TemplateColor.outlineVariantLight,
        scrim = TemplateColor.scrimLight,
        inverseSurface = TemplateColor.inverseSurfaceLight,
        inverseOnSurface = TemplateColor.inverseOnSurfaceLight,
        inversePrimary = TemplateColor.inversePrimaryLight,
        surfaceDim = TemplateColor.surfaceDimLight,
        surfaceBright = TemplateColor.surfaceBrightLight,
        surfaceContainerLowest = TemplateColor.surfaceContainerLowestLight,
        surfaceContainerLow = TemplateColor.surfaceContainerLowLight,
        surfaceContainer = TemplateColor.surfaceContainerLight,
        surfaceContainerHigh = TemplateColor.surfaceContainerHighLight,
        surfaceContainerHighest = TemplateColor.surfaceContainerHighestLight,
        inverseOutline = Color.White,
        surfaceTint = TemplateColor.surfaceVariantLight,

        // Icon colors
        iconPrimary = TemplateColor.onSurfaceLight,
        iconSecondary = Color(0xFF8B8C94),
        iconDisabled = TemplateColor.outlineVariantLight,
        iconAccentPrimary = Color(162, 0, 0),
        iconAccentSecondary = TemplateColor.secondaryLight,
        iconInverse = Color.White,
        iconError = TemplateColor.errorLight,
        iconWarning = TemplateColor.warningLight,
        iconSuccess = TemplateColor.successLight,
        iconInfo = TemplateColor.infoLight,
        iconClearThemeColor = Color.Red,

        // Border
        borderBasic = TemplateColor.onSurfaceLight,
        borderDisabled = TemplateColor.outlineVariantLight,
        borderAccentPrimary = Color(162, 0, 0),
        borderAccentSecondary = TemplateColor.secondaryLight,
        borderError = TemplateColor.errorLight,
        borderWarning = TemplateColor.warningLight,
        borderSuccess = TemplateColor.successLight,
        borderInfo = TemplateColor.infoLight,

        // Text
        textPrimary = Color(0xFF212121),
        textSecondary = Color(0xFF7D7D7D),
        textDisabled = TemplateColor.outlineLight,
        textInverse = Color.White,
        textAccentPrimary = Color(162, 0, 0),
        textAccentSecondary = TemplateColor.secondaryLight,
        textError = TemplateColor.errorLight,
        textSuccess = TemplateColor.successContainerLight,
        textWarning = TemplateColor.warningContainerLight,
        textInfo = TemplateColor.infoContainerLight,

        // Some colors
        backgroundSecondary = Color.LightGray,
        backgroundTertiary = Color(0xFFF4F4F4),
    )
}

internal fun darkColorScheme(): HabitColorScheme {
    return HabitColorScheme(
        primary = TemplateColor.primaryDark,
        onPrimary = TemplateColor.onPrimaryDark,
        primaryContainer = TemplateColor.primaryContainerDark,
        onPrimaryContainer = TemplateColor.onPrimaryContainerDark,
        secondary = TemplateColor.secondaryDark,
        onSecondary = TemplateColor.onSecondaryDark,
        secondaryContainer = TemplateColor.secondaryContainerDark,
        onSecondaryContainer = TemplateColor.onSecondaryContainerDark,
        tertiary = TemplateColor.tertiaryDark,
        onTertiary = TemplateColor.onTertiaryDark,
        tertiaryContainer = TemplateColor.tertiaryContainerDark,
        onTertiaryContainer = TemplateColor.onTertiaryContainerDark,
        error = TemplateColor.errorDark,
        onError = TemplateColor.onErrorDark,
        errorContainer = TemplateColor.errorContainerDark,
        onErrorContainer = TemplateColor.onErrorContainerDark,
        background = TemplateColor.backgroundDark,
        onBackground = TemplateColor.onBackgroundDark,
        surface = TemplateColor.surfaceDark,
        onSurface = TemplateColor.onSurfaceDark,
        surfaceVariant = TemplateColor.surfaceVariantDark,
        onSurfaceVariant = TemplateColor.onSurfaceVariantDark,
        outline = TemplateColor.outlineDark,
        outlineVariant = TemplateColor.outlineVariantDark,
        scrim = TemplateColor.scrimDark,
        inverseSurface = TemplateColor.inverseSurfaceDark,
        inverseOnSurface = TemplateColor.inverseOnSurfaceDark,
        inversePrimary = TemplateColor.inversePrimaryDark,
        surfaceDim = TemplateColor.surfaceDimDark,
        surfaceBright = TemplateColor.surfaceBrightDark,
        surfaceContainerLowest = TemplateColor.surfaceContainerLowestDark,
        surfaceContainerLow = TemplateColor.surfaceContainerLowDark,
        surfaceContainer = TemplateColor.surfaceContainerDark,
        surfaceContainerHigh = TemplateColor.surfaceContainerHighDark,
        surfaceContainerHighest = TemplateColor.surfaceContainerHighestDark,
        inverseOutline = Color.White,
        surfaceTint = TemplateColor.surfaceContainerDark,

        iconPrimary = Color(0xFFFFE3E3),
        iconSecondary = Color(0xFFB8A1A1),    // iOS-like secondary icon color for dark theme
        iconDisabled = TemplateColor.outlineVariantDark,
        iconAccentPrimary = Color(0xFFA13042),           // Bright red accent (analog of 162,0,0 in light)
        iconAccentSecondary = TemplateColor.secondaryDark,
        iconInverse = Color.White,
        iconError = TemplateColor.errorDark,
        iconWarning = TemplateColor.warningDark,
        iconSuccess = TemplateColor.successDark,
        iconInfo = TemplateColor.infoDark,
        iconClearThemeColor = Color.Red,

        // Border
        borderBasic = TemplateColor.onSurfaceDark,
        borderDisabled = TemplateColor.outlineVariantDark,
        borderAccentPrimary = Color(0xFFA13042),           // Bright red accent
        borderAccentSecondary = TemplateColor.secondaryDark,
        borderError = TemplateColor.errorDark,
        borderWarning = TemplateColor.warningDark,
        borderSuccess = TemplateColor.successDark,
        borderInfo = TemplateColor.infoDark,

        // Text
        textPrimary = Color(0xFFFFE3E3),
        textSecondary = Color(0xFFB8A1A1),
        textDisabled = TemplateColor.outlineDark,
        textInverse = TemplateColor.onSurfaceLight,
        textAccentPrimary = Color(0xFFA13042),
        textAccentSecondary = TemplateColor.secondaryDark,
        textError = TemplateColor.errorDark,
        textSuccess = TemplateColor.successDark,
        textWarning = TemplateColor.warningDark,
        textInfo = TemplateColor.infoDark,

        backgroundSecondary = Color(0xFF454545),
        backgroundTertiary = Color(0xFF262626),
    )
}

@Immutable
data class HabitColorScheme(
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val inversePrimary: Color,
    val secondary: Color,
    val onSecondary: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,
    val tertiary: Color,
    val onTertiary: Color,
    val tertiaryContainer: Color,
    val onTertiaryContainer: Color,
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color,
    val surfaceVariant: Color,
    val onSurfaceVariant: Color,
    val inverseSurface: Color,
    val inverseOnSurface: Color,
    val error: Color,
    val onError: Color,
    val errorContainer: Color,
    val onErrorContainer: Color,
    val outline: Color,
    val outlineVariant: Color,
    val scrim: Color,
    val surfaceBright: Color,
    val surfaceDim: Color,
    val surfaceContainer: Color,
    val surfaceContainerHigh: Color,
    val surfaceContainerHighest: Color,
    val surfaceContainerLow: Color,
    val surfaceContainerLowest: Color,
    val inverseOutline: Color,
    val surfaceTint: Color,

    // Icons
    val iconPrimary: Color,
    val iconSecondary: Color,
    val iconDisabled: Color,
    val iconAccentPrimary: Color,
    val iconAccentSecondary: Color,
    val iconInverse: Color,
    val iconError: Color,
    val iconWarning: Color,
    val iconSuccess: Color,
    val iconInfo: Color,
    val iconClearThemeColor: Color,

    // Border
    val borderBasic: Color,
    val borderDisabled: Color,
    val borderAccentPrimary: Color,
    val borderAccentSecondary: Color,
    val borderError: Color,
    val borderWarning: Color,
    val borderSuccess: Color,
    val borderInfo: Color,

    // Text
    val textPrimary: Color,
    val textSecondary: Color,
    val textDisabled: Color,
    val textInverse: Color,
    val textAccentPrimary: Color,
    val textAccentSecondary: Color,
    val textError: Color,
    val textSuccess: Color,
    val textWarning: Color,
    val textInfo: Color,

    // Some colors
    val backgroundSecondary: Color,
    val backgroundTertiary: Color,
)

object TemplateColor {
    val primaryLight = Color(0xFF4B4B4B)
    val onPrimaryLight = Color(0xFFFFFFFF)
    val primaryContainerLight = Color(0xFFFFFFFF)
    val onPrimaryContainerLight = Color(0xFFE1E0DF)
    val secondaryLight = Color(0xFF5F5E5E)
    val onSecondaryLight = Color(0xFFFFFFFF)
    val secondaryContainerLight = Color(0xFFE2DFDE)
    val onSecondaryContainerLight = Color(0xFF636262)
    val tertiaryLight = Color(0xFF5F5E5E)
    val onTertiaryLight = Color(0xFFFFFFFF)
    val tertiaryContainerLight = Color(0xFFC6C3C3)
    val onTertiaryContainerLight = Color(0xFF515050)
    val errorLight = Color(0xFFBA1A1A)
    val onErrorLight = Color(0xFFFFFFFF)
    val errorContainerLight = Color(0xFFFFDAD6)
    val onErrorContainerLight = Color(0xFF93000A)
    val backgroundLight = Color(255, 245, 245)
    val onBackgroundLight = Color(0xFF1C1B1B)
    val surfaceLight = Color(0xFFFCF8F8)
    val onSurfaceLight = Color(0xFF1C1B1B)
    val surfaceVariantLight = Color(0xFFE0E3E3)
    val onSurfaceVariantLight = Color(0xFF444748)
    val outlineLight = Color(0xFF747878)
    val outlineVariantLight = Color(0xFFC4C7C7)
    val scrimLight = Color(0xFF000000)
    val inverseSurfaceLight = Color(0xFF313030)
    val inverseOnSurfaceLight = Color(0xFFF4F0EF)
    val inversePrimaryLight = Color(0xFFC7C6C6)
    val surfaceDimLight = Color(0xFFDDD9D8)
    val surfaceBrightLight = Color(0xFFFCF8F8)
    val surfaceContainerLowestLight = Color(0xFFFFFFFF)
    val surfaceContainerLowLight = Color(0xFFF7F3F2)
    val surfaceContainerLight = Color(0xFFF1EDEC)
    val surfaceContainerHighLight = Color(0xFFEBE7E7)
    val surfaceContainerHighestLight = Color(0xFFE5E2E1)

    val primaryDark = Color(0xFFC7C6C6)
    val onPrimaryDark = Color(0xFF303031)
    val primaryContainerDark = Color(0xFF171717)
    val onPrimaryContainerDark = Color(0xFFE1E0DF)
    val secondaryDark = Color(0xFFC8C6C5)
    val onSecondaryDark = Color(0xFF313030)
    val secondaryContainerDark = Color(0xFF474746)
    val onSecondaryContainerDark = Color(0xFFB7B5B4)
    val tertiaryDark = Color(0xFFE2DFDF)
    val onTertiaryDark = Color(0xFF313030)
    val tertiaryContainerDark = Color(0xFFC6C3C3)
    val onTertiaryContainerDark = Color(0xFF515050)
    val errorDark = Color(0xFFFFB4AB)
    val onErrorDark = Color(0xFF690005)
    val errorContainerDark = Color(0xFF93000A)
    val onErrorContainerDark = Color(0xFFFFDAD6)
    val backgroundDark = Color(0xFF322222)
    val onBackgroundDark = Color(0xFFE5E2E1)
    val surfaceDark = Color(0xFF141313)
    val onSurfaceDark = Color(0xFFE5E2E1)
    val surfaceVariantDark = Color(0xFF444748)
    val onSurfaceVariantDark = Color(0xFFC4C7C7)
    val outlineDark = Color(0xFF8E9192)
    val outlineVariantDark = Color(0xFF444748)
    val scrimDark = Color(0xFF000000)
    val inverseSurfaceDark = Color(0xFFE5E2E1)
    val inverseOnSurfaceDark = Color(0xFF313030)
    val inversePrimaryDark = Color(0xFF5E5E5E)
    val surfaceDimDark = Color(0xFF141313)
    val surfaceBrightDark = Color(0xFF3A3939)
    val surfaceContainerLowestDark = Color(0xFF0E0E0E)
    val surfaceContainerLowDark = Color(0xFF1C1B1B)
    val surfaceContainerDark = Color(0xFF201F1F)
    val surfaceContainerHighDark = Color(0xFF2A2A2A)
    val surfaceContainerHighestDark = Color(0xFF353434)

    val warningLight = Color(0xFF8F4E00)
    val onWarningLight = Color(0xFFFFFFFF)
    val warningContainerLight = Color(0xFFFF8F00)
    val onWarningContainerLight = Color(0xFF623400)
    val successLight = Color(0xFF006E1C)
    val onSuccessLight = Color(0xFFFFFFFF)
    val successContainerLight = Color(0xFF4CAF50)
    val onSuccessContainerLight = Color(0xFF003C0B)
    val infoLight = Color(0xFF006493)
    val onInfoLight = Color(0xFFFFFFFF)
    val infoContainerLight = Color(0xFF03A9F4)
    val onInfoContainerLight = Color(0xFF003A58)

    val warningLightMediumContrast = Color(0xFF552C00)
    val onWarningLightMediumContrast = Color(0xFFFFFFFF)
    val warningContainerLightMediumContrast = Color(0xFFA45A00)
    val onWarningContainerLightMediumContrast = Color(0xFFFFFFFF)
    val successLightMediumContrast = Color(0xFF00400C)
    val onSuccessLightMediumContrast = Color(0xFFFFFFFF)
    val successContainerLightMediumContrast = Color(0xFF117E26)
    val onSuccessContainerLightMediumContrast = Color(0xFFFFFFFF)
    val infoLightMediumContrast = Color(0xFF003A57)
    val onInfoLightMediumContrast = Color(0xFFFFFFFF)
    val infoContainerLightMediumContrast = Color(0xFF0074A9)
    val onInfoContainerLightMediumContrast = Color(0xFFFFFFFF)

    val warningLightHighContrast = Color(0xFF462300)
    val onWarningLightHighContrast = Color(0xFFFFFFFF)
    val warningContainerLightHighContrast = Color(0xFF703C00)
    val onWarningContainerLightHighContrast = Color(0xFFFFFFFF)
    val successLightHighContrast = Color(0xFF003509)
    val onSuccessLightHighContrast = Color(0xFFFFFFFF)
    val successContainerLightHighContrast = Color(0xFF005614)
    val onSuccessContainerLightHighContrast = Color(0xFFFFFFFF)
    val infoLightHighContrast = Color(0xFF002F48)
    val onInfoLightHighContrast = Color(0xFFFFFFFF)
    val infoContainerLightHighContrast = Color(0xFF004E73)
    val onInfoContainerLightHighContrast = Color(0xFFFFFFFF)

    val warningDark = Color(0xFFFFB87B)
    val onWarningDark = Color(0xFF4C2700)
    val warningContainerDark = Color(0xFFFF8F00)
    val onWarningContainerDark = Color(0xFF623400)
    val successDark = Color(0xFF78DC77)
    val onSuccessDark = Color(0xFF00390A)
    val successContainerDark = Color(0xFF4CAF50)
    val onSuccessContainerDark = Color(0xFF003C0B)
    val infoDark = Color(0xFF8DCDFF)
    val onInfoDark = Color(0xFF00344F)
    val infoContainerDark = Color(0xFF03A9F4)
    val onInfoContainerDark = Color(0xFF003A58)

    val warningDarkMediumContrast = Color(0xFFFFD4B3)
    val onWarningDarkMediumContrast = Color(0xFF3D1E00)
    val warningContainerDarkMediumContrast = Color(0xFFFF8F00)
    val onWarningContainerDarkMediumContrast = Color(0xFF361A00)
    val successDarkMediumContrast = Color(0xFF8EF38B)
    val onSuccessDarkMediumContrast = Color(0xFF002D06)
    val successContainerDarkMediumContrast = Color(0xFF4CAF50)
    val onSuccessContainerDarkMediumContrast = Color(0xFF000F01)
    val infoDarkMediumContrast = Color(0xFFBDE1FF)
    val onInfoDarkMediumContrast = Color(0xFF00283F)
    val infoContainerDarkMediumContrast = Color(0xFF03A9F4)
    val onInfoContainerDarkMediumContrast = Color(0xFF001523)

    val warningDarkHighContrast = Color(0xFFFFEDE0)
    val onWarningDarkHighContrast = Color(0xFF000000)
    val warningContainerDarkHighContrast = Color(0xFFFFB26F)
    val onWarningContainerDarkHighContrast = Color(0xFF160800)
    val successDarkHighContrast = Color(0xFFC5FFBC)
    val onSuccessDarkHighContrast = Color(0xFF000000)
    val successContainerDarkHighContrast = Color(0xFF74D873)
    val onSuccessContainerDarkHighContrast = Color(0xFF000F01)
    val infoDarkHighContrast = Color(0xFFE5F1FF)
    val onInfoDarkHighContrast = Color(0xFF000000)
    val infoContainerDarkHighContrast = Color(0xFF83CAFF)
    val onInfoContainerDarkHighContrast = Color(0xFF000D17)
}

internal fun HabitColorScheme.toM3ColorScheme(): M3ColorScheme {
    return M3ColorScheme(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primaryContainer,
        onPrimaryContainer = onPrimaryContainer,
        inversePrimary = inversePrimary,
        secondary = secondary,
        onSecondary = onSecondary,
        secondaryContainer = secondaryContainer,
        onSecondaryContainer = onSecondaryContainer,
        tertiary = tertiary,
        onTertiary = onTertiary,
        tertiaryContainer = tertiaryContainer,
        onTertiaryContainer = onTertiaryContainer,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        surfaceVariant = surfaceVariant,
        onSurfaceVariant = onSurfaceVariant,
        surfaceTint = surfaceTint,
        inverseSurface = inverseSurface,
        inverseOnSurface = inverseOnSurface,
        error = error,
        onError = onError,
        errorContainer = errorContainer,
        onErrorContainer = onErrorContainer,
        outline = outline,
        outlineVariant = outlineVariant,
        scrim = scrim,
        surfaceBright = surfaceBright,
        surfaceDim = surfaceDim,
        surfaceContainer = surfaceContainer,
        surfaceContainerHigh = surfaceContainerHigh,
        surfaceContainerHighest = surfaceContainerHighest,
        surfaceContainerLow = surfaceContainerLow,
        surfaceContainerLowest = surfaceContainerLowest,
    )
}

@Composable
fun navBarItemColors(): NavigationBarItemColors {
    val selectedIconColor = HabitTheme.colors.iconAccentPrimary
    val selectedTextColor = HabitTheme.colors.iconAccentSecondary
    val selectedIndicatorColor = Color.Transparent
    val unselectedIconColor = HabitTheme.colors.iconDisabled
    val unselectedTextColor = HabitTheme.colors.textDisabled
    val disabledIconColor = HabitTheme.colors.iconDisabled
    val disabledTextColor = HabitTheme.colors.textDisabled

    return remember {
        NavigationBarItemColors(
            selectedIconColor = selectedIconColor,
            selectedTextColor = selectedTextColor,
            selectedIndicatorColor = selectedIndicatorColor,
            unselectedIconColor = unselectedIconColor,
            unselectedTextColor = unselectedTextColor,
            disabledIconColor = disabledIconColor,
            disabledTextColor = disabledTextColor,
        )
    }
}
