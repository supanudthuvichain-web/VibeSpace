package com.vibespace.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Material3 dark color scheme mapped from our custom VibeSpace palette.
 */
private val DarkColorScheme = darkColorScheme(
    primary = VibeSpaceColors.primary,
    onPrimary = VibeSpaceColors.onSurface,
    primaryContainer = VibeSpaceColors.primaryContainer,
    secondary = VibeSpaceColors.primaryLight,
    onSecondary = VibeSpaceColors.onSurface,
    background = VibeSpaceColors.background,
    onBackground = VibeSpaceColors.onBackground,
    surface = VibeSpaceColors.surface,
    onSurface = VibeSpaceColors.onSurface,
    surfaceVariant = VibeSpaceColors.surfaceVariant,
    onSurfaceVariant = VibeSpaceColors.onSurfaceVariant,
    error = VibeSpaceColors.error,
    onError = VibeSpaceColors.onError,
    outline = VibeSpaceColors.outline,
    scrim = VibeSpaceColors.scrim,
)

/**
 * CompositionLocal to provide direct access to VibeSpaceColors
 * throughout the Compose tree without going through MaterialTheme.
 */
val LocalVibeSpaceColors = staticCompositionLocalOf { VibeSpaceColors }

/**
 * VibeSpace theme wrapper.
 * Applies the dark color scheme, typography, and provides
 * our custom color palette via CompositionLocal.
 */
@Composable
fun VibeSpaceTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalVibeSpaceColors provides VibeSpaceColors) {
        MaterialTheme(
            colorScheme = DarkColorScheme,
            typography = VibeSpaceTypography,
            content = content
        )
    }
}

/**
 * Convenience accessor for VibeSpace colors.
 * Usage: VibeSpaceTheme.colors.primary
 */
object VibeSpaceTheme {
    val colors: VibeSpaceColors
        @Composable
        @ReadOnlyComposable
        get() = LocalVibeSpaceColors.current

    val typography: androidx.compose.material3.Typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography
}
