package com.walhalla.wvcompose7.game.models

import kotlin.random.Random

class LuckSystem {
    private var luckPoints: Int = 0
    private var streakMultiplier: Float = 1.0f
    private var demonPact: Boolean = false
    
    fun addLuckPoints(points: Int) {
        luckPoints += (points * streakMultiplier).toInt()
        updateStreakMultiplier()
    }
    
    fun spendLuckPoints(cost: Int): Boolean {
        if (luckPoints >= cost) {
            luckPoints -= cost
            return true
        }
        return false
    }
    
    fun getLuckPoints(): Int = luckPoints
    
    fun getStreakMultiplier(): Float = streakMultiplier
    
    private fun updateStreakMultiplier() {
        streakMultiplier = minOf(streakMultiplier + 0.1f, 3.0f)
    }
    
    fun resetStreak() {
        streakMultiplier = 1.0f
    }
    
    fun makeDemonPact() {
        demonPact = true
        luckPoints *= 2
        streakMultiplier *= 1.5f
    }
    
    fun isDemonPactActive(): Boolean = demonPact
    
    fun calculateLuckBonus(): Int {
        val baseBonus = Random.nextInt(1, 6)
        return if (demonPact) baseBonus * 2 else baseBonus
    }
    
    fun reset() {
        luckPoints = 0
        streakMultiplier = 1.0f
        demonPact = false
    }
} 