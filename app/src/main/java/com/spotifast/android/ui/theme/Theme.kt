package com.spotifast.android.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val WinampColorScheme = darkColorScheme(
    primary = WinampLcdGreen,
    secondary = WinampAccentAmber,
    background = WinampMetalDark,
    surface = WinampMetalBackground,
    onPrimary = WinampLcdBackground,
    onBackground = WinampLcdGreen,
    onSurface = WinampLcdGreen,
)

@Composable
fun SpotifastTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = WinampColorScheme,
        content = content
    )
}
