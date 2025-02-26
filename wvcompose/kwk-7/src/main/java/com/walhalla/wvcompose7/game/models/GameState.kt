package com.walhalla.wvcompose7.game.models

enum class GameState {
    INTRO,      // Начальный экран
    BETTING,    // Ставки
    PLAYING,    // Игровой процесс
    SPLIT,      // Разделение карт
    INSURANCE,  // Страховка
    DEALER,     // Ход дилера
    GAME_OVER,  // Конец игры
    BONUS_GAME  // Бонусная игра
} 