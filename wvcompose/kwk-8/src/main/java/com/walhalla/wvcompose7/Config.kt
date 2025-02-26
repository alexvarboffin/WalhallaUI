package com.walhalla.wvcompose7

object Config {
    const val GAME_NAME = "Pirate's Fortune"
    const val GAME_URL = "https://google.com/pirates-fortune"
    
    // Навигационные пункты меню в пиратском стиле
    val MENU_ITEMS = listOf(
        MenuItem("🏴‍☠️ Таверна", "https://google.com/pirates-fortune/tavern"),
        MenuItem("💰 Сокровища", "https://google.com/pirates-fortune/treasures"),
        MenuItem("⚔️ Капитаны", "https://google.com/pirates-fortune/captains"),
        MenuItem("🗺️ Карта", "https://google.com/pirates-fortune/map")
    )
}

data class MenuItem(
    val title: String,
    val url: String
) 