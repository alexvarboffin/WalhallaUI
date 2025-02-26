package com.walhalla.wvcompose7

object Config {
    const val GAME_NAME = "Devil's Casino"
    const val GAME_URL = "https://google.com/devils-casino"
    
    // Навигационные пункты меню с провокационными названиями
    val MENU_ITEMS = listOf(
        MenuItem("😈 Искушение", "https://google.com/devils-casino/temptation"),
        MenuItem("🔥 Грехи", "https://google.com/devils-casino/sins"),
        MenuItem("👿 Демоны", "https://google.com/devils-casino/demons"),
        MenuItem("💀 Проклятые", "https://google.com/devils-casino/cursed")
    )
}

data class MenuItem(
    val title: String,
    val url: String
) 