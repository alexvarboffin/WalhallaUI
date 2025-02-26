package com.walhalla.wvcompose7.slots

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun HellSpinsSlot(
    modifier: Modifier = Modifier
) {
    BaseSlot(
        title = "Адские Спины",
        symbols = listOf("🔥", "⚡", "🌋", "🗡️", "⚔️"),
        primaryColor = Color(0xFF330000),
        secondaryColor = Color(0xFFCC0000),
        modifier = modifier
    )
} 