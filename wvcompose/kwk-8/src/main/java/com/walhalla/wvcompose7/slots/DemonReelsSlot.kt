package com.walhalla.wvcompose7.slots

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun DemonReelsSlot(
    modifier: Modifier = Modifier
) {
    BaseSlot(
        title = "Демонические Барабаны",
        symbols = listOf("👿", "😈", "🦹", "🧙", "🔮"),
        primaryColor = Color(0xFF2B0A3D),
        secondaryColor = Color(0xFF6B238E),
        modifier = modifier
    )
} 