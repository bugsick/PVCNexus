package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NexusCyan,
    onPrimary = Color(0xFF001F28),
    primaryContainer = NexusIndigo,
    onPrimaryContainer = Color(0xFFE0E7FF),
    secondary = NexusPurpleLight,
    onSecondary = Color(0xFF1E1035),
    secondaryContainer = Color(0xFF2E1A47),
    onSecondaryContainer = Color(0xFFF3E8FF),
    tertiary = NexusEmerald,
    onTertiary = Color(0xFF003822),
    background = NexusBackground,
    onBackground = NexusTextPrimary,
    surface = NexusSurface,
    onSurface = NexusTextPrimary,
    surfaceVariant = NexusSurfaceElevated,
    onSurfaceVariant = NexusTextSecondary,
    outline = Color(0xFF334155),
    outlineVariant = Color(0xFF1E293B),
    error = NexusRose,
    onError = Color.White
)

@Composable
fun PvcNexusTheme(
    darkTheme: Boolean = true, // Default to premium dark glassmorphism theme
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
