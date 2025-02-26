package com.walhalla.wvcompose7.game.bonus

import kotlin.random.Random

data class DevilsDeal(
    val type: DealType,
    val multiplier: Float,
    val duration: Int,
    val soulCost: Int,
    val description: String
)

enum class DealType {
    DOUBLE_OR_NOTHING,    // Удвоение выигрыша или потеря всего
    SOUL_EXCHANGE,        // Обмен души на бонусы
    CURSED_LUCK,         // Повышенные шансы, но с проклятием
    DEMONIC_POWER,       // Мощные способности с высокой ценой
    HELLFIRE_JACKPOT     // Огромный выигрыш с риском
}

class DevilsDealManager {
    private var activeDeal: DevilsDeal? = null
    private var dealsCompleted = 0
    private var soulsCollected = 0
    
    fun generateDeal(): DevilsDeal {
        val type = DealType.values().random()
        val baseMultiplier = when (type) {
            DealType.DOUBLE_OR_NOTHING -> 2.0f
            DealType.SOUL_EXCHANGE -> 1.5f
            DealType.CURSED_LUCK -> 3.0f
            DealType.DEMONIC_POWER -> 2.5f
            DealType.HELLFIRE_JACKPOT -> 5.0f
        }
        
        val duration = Random.nextInt(3, 8)
        val soulCost = calculateSoulCost(type, baseMultiplier)
        
        return DevilsDeal(
            type = type,
            multiplier = baseMultiplier * (1 + dealsCompleted * 0.1f),
            duration = duration,
            soulCost = soulCost,
            description = generateDescription(type, baseMultiplier, duration, soulCost)
        )
    }
    
    fun acceptDeal(deal: DevilsDeal): Boolean {
        if (activeDeal != null) return false
        
        activeDeal = deal
        dealsCompleted++
        soulsCollected += deal.soulCost
        return true
    }
    
    fun isDevilsDealActive(): Boolean = activeDeal != null
    
    fun getActiveDeal(): DevilsDeal? = activeDeal
    
    fun endCurrentDeal() {
        activeDeal = null
    }
    
    private fun calculateSoulCost(type: DealType, multiplier: Float): Int {
        val baseCost = when (type) {
            DealType.DOUBLE_OR_NOTHING -> 100
            DealType.SOUL_EXCHANGE -> 50
            DealType.CURSED_LUCK -> 200
            DealType.DEMONIC_POWER -> 150
            DealType.HELLFIRE_JACKPOT -> 500
        }
        
        return (baseCost * multiplier).toInt()
    }
    
    private fun generateDescription(
        type: DealType,
        multiplier: Float,
        duration: Int,
        soulCost: Int
    ): String {
        return when (type) {
            DealType.DOUBLE_OR_NOTHING ->
                "Удвой свой выигрыш, но рискни потерять всё. " +
                "Множитель: x$multiplier. Длительность: $duration ходов. " +
                "Цена души: $soulCost"
            
            DealType.SOUL_EXCHANGE ->
                "Обменяй часть своей души на силу удачи. " +
                "Множитель: x$multiplier. Длительность: $duration ходов. " +
                "Цена души: $soulCost"
            
            DealType.CURSED_LUCK ->
                "Получи невероятную удачу, но будь готов к проклятию. " +
                "Множитель: x$multiplier. Длительность: $duration ходов. " +
                "Цена души: $soulCost"
            
            DealType.DEMONIC_POWER ->
                "Обрети силу демонов для победы. " +
                "Множитель: x$multiplier. Длительность: $duration ходов. " +
                "Цена души: $soulCost"
            
            DealType.HELLFIRE_JACKPOT ->
                "Рискни всем ради огромного выигрыша в адском пламени. " +
                "Множитель: x$multiplier. Длительность: $duration ходов. " +
                "Цена души: $soulCost"
        }
    }
    
    fun getStats(): DevilsDealStats {
        return DevilsDealStats(
            dealsCompleted = dealsCompleted,
            soulsCollected = soulsCollected,
            currentDeal = activeDeal
        )
    }
}

data class DevilsDealStats(
    val dealsCompleted: Int,
    val soulsCollected: Int,
    val currentDeal: DevilsDeal?
) 