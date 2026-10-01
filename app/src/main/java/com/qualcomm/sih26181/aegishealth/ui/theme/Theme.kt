package com.qualcomm.sih26181.aegishealth.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = AegisCyan,
    secondary = AegisViolet,
    tertiary = AegisEmerald,
    background = AegisBackground,
    surface = AegisSurface,
    surfaceVariant = AegisSurfaceVariant,
    onPrimary = AegisBackground,
    onSecondary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    error = AegisRed
)

@Composable
fun CrystaTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
