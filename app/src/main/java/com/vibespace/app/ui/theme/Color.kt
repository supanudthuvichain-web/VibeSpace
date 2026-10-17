package com.vibespace.app.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * VibeSpace color palette — dark theme with blue accent.
 */
object VibeSpaceColors {
    // Backgrounds
    val background = Color(0xFF0A0A0A)
    val surface = Color(0xFF1A1A2E)
    val surfaceVariant = Color(0xFF16213E)
    val surfaceElevated = Color(0xFF1E1E2E)

    // Primary accent — Blue
    val primary = Color(0xFF2196F3)
    val primaryLight = Color(0xFF64B5F6)
    val primaryDark = Color(0xFF1565C0)
    val primaryContainer = Color(0xFF1A3A5C)

    // Text
    val onBackground = Color(0xFFE0E0E0)
    val onSurface = Color(0xFFFFFFFF)
    val onSurfaceVariant = Color(0xFFB0B0B0)
    val textSecondary = Color(0xFF9E9E9E)
    val textTertiary = Color(0xFF6E6E6E)

    // Semantic
    val error = Color(0xFFCF6679)
    val onError = Color(0xFF000000)

    // Mini-player
    val miniPlayerBackground = Color(0xFF1E1E2E)
    val miniPlayerBorder = Color(0xFF2A2A3E)

    // Divider & Outline
    val divider = Color(0xFF2A2A2A)
    val outline = Color(0xFF3A3A3A)

    // Scrim for bottom sheet overlay
    val scrim = Color(0x80000000)

    // Gradients
    val gradientStart = Color(0xFF1A1A2E)
    val gradientEnd = Color(0xFF0A0A0A)

    // Playback progress
    val progressTrack = Color(0xFF333333)
    val progressIndicator = primary
}
