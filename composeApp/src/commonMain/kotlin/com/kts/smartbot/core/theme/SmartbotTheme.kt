package com.kts.smartbot.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF6E64F4),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFE8E5FF),
    onPrimaryContainer = Color(0xFF241B61),
    secondary = Color(0xFF7A72FF),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFECE9FF),
    onSecondaryContainer = Color(0xFF2D246E),
    tertiary = Color(0xFF8B84FF),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFF0EEFF),
    onTertiaryContainer = Color(0xFF2F2A63),
    background = Color(0xFFF7F7FF),
    onBackground = Color(0xFF181827),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF181827),
    surfaceVariant = Color(0xFFE9E8F7),
    onSurfaceVariant = Color(0xFF3D3951),
    outline = Color(0xFF85809A),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFC9C3FF),
    onPrimary = Color(0xFF2F266E),
    primaryContainer = Color(0xFF342F60),
    onPrimaryContainer = Color(0xFFE8E5FF),
    secondary = Color(0xFFD1CBFF),
    onSecondary = Color(0xFF322B6C),
    secondaryContainer = Color(0xFF2A2D40),
    onSecondaryContainer = Color(0xFFE8E5FF),
    tertiary = Color(0xFFDAD6FF),
    onTertiary = Color(0xFF342D6B),
    tertiaryContainer = Color(0xFF2E3046),
    onTertiaryContainer = Color(0xFFF0EEFF),
    background = Color(0xFF10111A),
    onBackground = Color(0xFFF4F5FF),
    surface = Color(0xFF181A24),
    onSurface = Color(0xFFF4F5FF),
    surfaceVariant = Color(0xFF242736),
    onSurfaceVariant = Color(0xFFD2D6E8),
    outline = Color(0xFF9398AE),
)

@Composable
fun SmartbotTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        typography = Typography(),
        content = content,
    )
}
