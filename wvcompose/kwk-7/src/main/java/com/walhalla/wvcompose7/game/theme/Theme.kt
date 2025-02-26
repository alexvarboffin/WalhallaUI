package com.walhalla.wvcompose7.game.theme

import android.app.Activity
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = HellFire,
    secondary = BloodRed,
    tertiary = DemonPurple,
    background = DarkAbyss,
    surface = DarkSurface,
    onPrimary = DarkAbyss,
    onSecondary = TextLight,
    onTertiary = TextLight,
    onBackground = TextLight,
    onSurface = TextLight
)

@Composable
fun CasinoTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
} 