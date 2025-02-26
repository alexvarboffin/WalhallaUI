package com.walhalla.wvcompose7

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.walhalla.wvcompose7.slots.*
import com.walhalla.wvcompose7.webview.WebViewScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var isWebView by remember { mutableStateOf(false) }
    var selectedSlot by remember { mutableStateOf(SlotType.PIRATE_FORTUNE) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Пиратские Сокровища") },
                actions = {
                    IconButton(onClick = { isWebView = !isWebView }) {
                        Text(if (isWebView) "🎰" else "🌐")
                    }
                }
            )
        },
        bottomBar = {
            if (!isWebView) {
                NavigationBar {
                    SlotType.values().forEach { slot ->
                        NavigationBarItem(
                            selected = selectedSlot == slot,
                            onClick = { selectedSlot = slot },
                            icon = { Text(slot.icon) },
                            label = { Text(slot.title) }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isWebView) {
                WebViewScreen(url = selectedSlot.webUrl)
            } else {
                when (selectedSlot) {
                    SlotType.PIRATE_FORTUNE -> PirateFortuneSlot()
                    SlotType.TREASURE_HUNT -> TreasureHuntSlot()
                    SlotType.CARIBBEAN_REELS -> CaribbeanReelsSlot()
                    SlotType.KRAKEN_DEPTHS -> KrakenDepthsSlot()
                }
            }
        }
    }
}

enum class SlotType(
    val title: String,
    val icon: String,
    val webUrl: String
) {
    PIRATE_FORTUNE(
        "Удача Пирата",
        "🏴‍☠️",
        "https://google.com/pirate-fortune"
    ),
    TREASURE_HUNT(
        "Охота за Сокровищами",
        "💰",
        "https://google.com/treasure-hunt"
    ),
    CARIBBEAN_REELS(
        "Карибские Барабаны",
        "⚓",
        "https://google.com/caribbean-reels"
    ),
    KRAKEN_DEPTHS(
        "Глубины Кракена",
        "🐙",
        "https://google.com/kraken-depths"
    )
} 