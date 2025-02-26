package com.walhalla.wvcompose6.ui.theme

import android.app.Activity

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val CasinoColorScheme = darkColorScheme(
    primary = GoldColor,
    secondary = RubyRed,
    tertiary = PurpleAccent,
    background = DarkBackground,
    surface = CardBackground,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun WVComposeTheme(
    darkTheme: Boolean = true, // Всегда используем темную тему
    content: @Composable () -> Unit
) {
    val colorScheme = CasinoColorScheme
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