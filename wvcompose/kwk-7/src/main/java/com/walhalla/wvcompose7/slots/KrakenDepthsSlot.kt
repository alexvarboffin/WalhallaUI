package com.walhalla.wvcompose7.slots

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun KrakenDepthsSlot(
    modifier: Modifier = Modifier
) {
    BaseSlot(
        title = "Глубины Кракена",
        symbols = listOf("🐙", "🦈", "⚓", "🚢", "🌊"),
        primaryColor = Color(0xFF000080),
        secondaryColor = Color(0xFF0000CD),
        modifier = modifier
    )
} 