package com.walhalla.wvcompose7.slots

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun DevilsFortuneSlot(
    modifier: Modifier = Modifier
) {
    BaseSlot(
        title = "Дьявольская Удача",
        symbols = listOf("😈", "🔥", "💀", "👻", "🦇"),
        primaryColor = Color(0xFF1A0000),
        secondaryColor = Color(0xFF8B0000),
        modifier = modifier
    )
} 