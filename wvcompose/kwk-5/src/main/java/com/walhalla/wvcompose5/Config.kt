package com.walhalla.wvcompose5

object Config {
    const val GAME_NAME = "Vegas Roulette"
    const val GAME_URL = "https://google.com/vegas-roulette"
    
    // Навигационные пункты меню с провокационными названиями
    val MENU_ITEMS = listOf(
        MenuItem("🎰 Казино", "https://google.com/vegas-roulette"),
        MenuItem("💎 VIP-зал", "https://google.com/vegas-roulette/vip"),
        MenuItem("👑 Короли", "https://google.com/vegas-roulette/kings"),
        MenuItem("💰 Профиль", "https://google.com/vegas-roulette/profile")
    )
}

data class MenuItem(
    val title: String,
    val url: String
) 