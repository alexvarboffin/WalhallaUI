package com.walhalla.wvcompose7.slots

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun TreasureHuntSlot(
    modifier: Modifier = Modifier
) {
    BaseSlot(
        title = "Охота за Сокровищами",
        symbols = listOf("📦", "💎", "👑", "🗝️", "🏆"),
        primaryColor = Color(0xFF4A2700),
        secondaryColor = Color(0xFF8B4513),
        modifier = modifier
    )
} 