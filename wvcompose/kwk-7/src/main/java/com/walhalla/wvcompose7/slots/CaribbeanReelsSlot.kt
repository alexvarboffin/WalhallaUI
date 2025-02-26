package com.walhalla.wvcompose7.slots

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun CaribbeanReelsSlot(
    modifier: Modifier = Modifier
) {
    BaseSlot(
        title = "Карибские Барабаны",
        symbols = listOf("🏝️", "🌊", "🐠", "🦜", "🌴"),
        primaryColor = Color(0xFF006D5B),
        secondaryColor = Color(0xFF00A896),
        modifier = modifier
    )
} 