package com.walhalla.wvcompose7.game.mechanics

import com.walhalla.wvcompose7.game.models.LuckSystem
import kotlin.random.Random

class LuckManager {
    private val luckSystem = LuckSystem()
    private var consecutiveWins = 0
    private var totalBets = 0
    private var totalWinnings = 0
    private var highestWin = 0
    
    fun onGameResult(isWin: Boolean, betAmount: Int, winAmount: Int) {
        if (isWin) {
            consecutiveWins++
            totalWinnings += winAmount
            highestWin = maxOf(highestWin, winAmount)
            
            // Начисляем очки удачи
            val basePoints = (winAmount.toFloat() / betAmount * 10).toInt()
            val streakBonus = consecutiveWins * 5
            luckSystem.addLuckPoints(basePoints + streakBonus)
            
            // Проверяем возможность демонического пакта
            if (shouldOfferDemonPact()) {
                // Логика предложения демонического пакта
            }
        } else {
            consecutiveWins = 0
            luckSystem.resetStreak()
        }
        
        totalBets++
    }
    
    fun getLuckBonus(): Int {
        return luckSystem.calculateLuckBonus()
    }
    
    fun makeDemonPact() {
        if (!luckSystem.isDemonPactActive()) {
            luckSystem.makeDemonPact()
        }
    }
    
    private fun shouldOfferDemonPact(): Boolean {
        if (luckSystem.isDemonPactActive()) return false
        
        // Предлагаем пакт если:
        // 1. Игрок в выигрыше (totalWinnings > 0)
        // 2. У него хорошая серия побед (consecutiveWins >= 3)
        // 3. Случайный шанс (10%)
        return totalWinnings > 0 && 
               consecutiveWins >= 3 && 
               Random.nextFloat() < 0.1f
    }
    
    fun getStats(): LuckStats {
        return LuckStats(
            consecutiveWins = consecutiveWins,
            totalBets = totalBets,
            totalWinnings = totalWinnings,
            highestWin = highestWin,
            luckPoints = luckSystem.getLuckPoints(),
            streakMultiplier = luckSystem.getStreakMultiplier(),
            isDemonPactActive = luckSystem.isDemonPactActive()
        )
    }
}

data class LuckStats(
    val consecutiveWins: Int,
    val totalBets: Int,
    val totalWinnings: Int,
    val highestWin: Int,
    val luckPoints: Int,
    val streakMultiplier: Float,
    val isDemonPactActive: Boolean
) 