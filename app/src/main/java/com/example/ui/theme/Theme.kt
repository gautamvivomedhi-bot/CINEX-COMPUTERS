package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = CyanPrimary,
    onPrimary = Color(0xFF003544),
    primaryContainer = Color(0xFF004D63),
    onPrimaryContainer = CyanLight,
    secondary = AccentAmber,
    onSecondary = Color(0xFF4A2800),
    secondaryContainer = Color(0xFF6B3C00),
    onSecondaryContainer = Color(0xFFFFDDB8),
    tertiary = StatusEmerald,
    background = TechNavyBackground,
    onBackground = TextPrimaryDark,
    surface = TechNavySurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = TechNavyCard,
    onSurfaceVariant = TextSecondaryDark,
    outline = TechNavyCardBorder
)

private val LightColorScheme = lightColorScheme(
    primary = CyanDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFBAEDFF),
    onPrimaryContainer = Color(0xFF001F29),
    secondary = AccentAmber,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFE0B2),
    onSecondaryContainer = Color(0xFF2E1500),
    tertiary = StatusEmerald,
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightCard,
    onSurfaceVariant = LightTextSecondary,
    outline = Color(0xFFCBD5E1)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to sleek tech dark mode for gaming & computer store aesthetics
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
