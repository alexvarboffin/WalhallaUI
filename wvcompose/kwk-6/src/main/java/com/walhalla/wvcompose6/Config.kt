package com.walhalla.wvcompose6

object Config {
    const val GAME_NAME = "Hot Vegas Nights"
    const val GAME_URL = "https://google.com/hot-vegas-nights"
    
    // Навигационные пункты меню с VIP-элементами
    val MENU_ITEMS = listOf(
        MenuItem("🎰 Горячие игры", "https://google.com/hot-games"),
        MenuItem("💎 VIP Салон", "https://google.com/vip-salon"),
        MenuItem("👑 Элитный клуб", "https://google.com/elite-club"),
        MenuItem("💰 Мой кабинет", "https://google.com/profile")
    )
}

data class MenuItem(
    val title: String,
    val url: String
) 