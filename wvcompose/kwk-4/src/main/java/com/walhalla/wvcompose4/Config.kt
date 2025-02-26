package com.walhalla.wvcompose4

object Config {
    const val GAME_NAME = "Slot Machine"
    const val GAME_URL = "https://google.com/slot-machine"
    
    // Навигационные пункты меню
    val MENU_ITEMS = listOf(
        MenuItem("Главная", "https://google.com/slot-machine"),
        MenuItem("Правила", "https://google.com/slot-machine/rules"),
        MenuItem("Рейтинг", "https://google.com/slot-machine/leaderboard"),
        MenuItem("Профиль", "https://google.com/slot-machine/profile")
    )
}

data class MenuItem(
    val title: String,
    val url: String
) 