package com.walhalla.wvcompose7.slots

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun PirateFortuneSlot(
    modifier: Modifier = Modifier
) {
    BaseSlot(
        title = "Удача Пирата",
        symbols = listOf("🏴‍☠️", "⚔️", "💰", "🗺️", "⚓"),
        primaryColor = Color(0xFF001F3F),
        secondaryColor = Color(0xFF003366),
        modifier = modifier
    )
} 