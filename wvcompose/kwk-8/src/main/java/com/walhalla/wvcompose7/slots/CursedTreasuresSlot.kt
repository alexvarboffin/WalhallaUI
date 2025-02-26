package com.walhalla.wvcompose7.slots

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun CursedTreasuresSlot(
    modifier: Modifier = Modifier
) {
    BaseSlot(
        title = "Проклятые Сокровища",
        symbols = listOf("💎", "💰", "👑", "🏆", "💍"),
        primaryColor = Color(0xFF1A1A1A),
        secondaryColor = Color(0xFF4A4A4A),
        modifier = modifier
    )
} 