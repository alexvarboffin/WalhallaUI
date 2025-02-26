package com.walhalla.wvcompose7.game.achievements.models

import androidx.compose.ui.graphics.Color

enum class AchievementRarity(val color: Color) {
    COMMON(Color(0xFF9E9E9E)),      // Серый
    UNCOMMON(Color(0xFF4CAF50)),    // Зеленый
    RARE(Color(0xFF2196F3)),        // Синий
    EPIC(Color(0xFF9C27B0)),        // Фиолетовый
    LEGENDARY(Color(0xFFFFD700)),   // Золотой
    DEMONIC(Color(0xFFFF0000))      // Красный
}

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val rarity: AchievementRarity,
    val points: Int,
    val isSecret: Boolean = false,
    val isUnlocked: Boolean = false,
    val progress: Float = 0f,         // От 0 до 1
    val requiredProgress: Float = 1f  // Значение для разблокировки
)

// Список всех достижений в игре
object Achievements {
    val FIRST_WIN = Achievement(
        id = "first_win",
        title = "Первая победа",
        description = "Выиграйте свою первую игру",
        rarity = AchievementRarity.COMMON,
        points = 10
    )
    
    val LUCKY_STREAK = Achievement(
        id = "lucky_streak",
        title = "Счастливчик",
        description = "Выиграйте 5 игр подряд",
        rarity = AchievementRarity.UNCOMMON,
        points = 25
    )
    
    val HIGH_ROLLER = Achievement(
        id = "high_roller",
        title = "Высокие ставки",
        description = "Сделайте ставку в 1000 или больше",
        rarity = AchievementRarity.RARE,
        points = 50
    )
    
    val BLACKJACK_MASTER = Achievement(
        id = "blackjack_master",
        title = "Мастер блэкджека",
        description = "Получите блэкджек 10 раз",
        rarity = AchievementRarity.EPIC,
        points = 100
    )
    
    val MILLIONAIRE = Achievement(
        id = "millionaire",
        title = "Миллионер",
        description = "Накопите 1,000,000 кредитов",
        rarity = AchievementRarity.LEGENDARY,
        points = 200
    )
    
    val DEMON_PACT = Achievement(
        id = "demon_pact",
        title = "Сделка с дьяволом",
        description = "Заключите демонический пакт",
        rarity = AchievementRarity.DEMONIC,
        points = 666,
        isSecret = true
    )
    
    // Список всех достижений
    val ALL = listOf(
        FIRST_WIN,
        LUCKY_STREAK,
        HIGH_ROLLER,
        BLACKJACK_MASTER,
        MILLIONAIRE,
        DEMON_PACT
    )
} 