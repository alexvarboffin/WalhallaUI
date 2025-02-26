package com.walhalla.wvcompose7.game.progression

import com.walhalla.wvcompose7.game.achievements.models.Achievement
import com.walhalla.wvcompose7.game.achievements.models.AchievementRarity
import kotlin.math.pow

data class PlayerProgress(
    val level: Int = 1,
    val experience: Int = 0,
    val requiredExperience: Int = 100,
    val totalGamesPlayed: Int = 0,
    val totalWins: Int = 0,
    val totalLosses: Int = 0,
    val highestWinStreak: Int = 0,
    val currentWinStreak: Int = 0,
    val totalEarnings: Long = 0,
    val highestBet: Int = 0,
    val unlockedAchievements: List<Achievement> = emptyList(),
    val demonPactsCompleted: Int = 0,
    val cursesEndured: Int = 0,
    val blessingsReceived: Int = 0
) {
    val winRate: Float
        get() = if (totalGamesPlayed > 0) {
            totalWins.toFloat() / totalGamesPlayed
        } else {
            0f
        }
    
    val nextLevelProgress: Float
        get() = experience.toFloat() / requiredExperience
    
    val achievementPoints: Int
        get() = unlockedAchievements.sumOf { it.points }
    
    fun addExperience(amount: Int): PlayerProgress {
        var newExperience = experience + amount
        var newLevel = level
        var newRequiredExperience = requiredExperience
        
        while (newExperience >= newRequiredExperience) {
            newExperience -= newRequiredExperience
            newLevel++
            newRequiredExperience = calculateRequiredExperience(newLevel)
        }
        
        return copy(
            level = newLevel,
            experience = newExperience,
            requiredExperience = newRequiredExperience
        )
    }
    
    fun addGameResult(isWin: Boolean, bet: Int, earnings: Int): PlayerProgress {
        val newTotalGamesPlayed = totalGamesPlayed + 1
        val newTotalWins = if (isWin) totalWins + 1 else totalWins
        val newTotalLosses = if (!isWin) totalLosses + 1 else totalLosses
        val newCurrentWinStreak = if (isWin) currentWinStreak + 1 else 0
        val newHighestWinStreak = maxOf(highestWinStreak, newCurrentWinStreak)
        val newTotalEarnings = totalEarnings + earnings
        val newHighestBet = maxOf(highestBet, bet)
        
        return copy(
            totalGamesPlayed = newTotalGamesPlayed,
            totalWins = newTotalWins,
            totalLosses = newTotalLosses,
            currentWinStreak = newCurrentWinStreak,
            highestWinStreak = newHighestWinStreak,
            totalEarnings = newTotalEarnings,
            highestBet = newHighestBet
        )
    }
    
    fun unlockAchievement(achievement: Achievement): PlayerProgress {
        if (achievement !in unlockedAchievements) {
            return copy(
                unlockedAchievements = unlockedAchievements + achievement,
                experience = experience + calculateAchievementExperience(achievement)
            )
        }
        return this
    }
    
    fun completeDemonPact(): PlayerProgress {
        return copy(
            demonPactsCompleted = demonPactsCompleted + 1,
            experience = experience + 100 // Бонусный опыт за сделку с дьяволом
        )
    }
    
    fun addCurse(): PlayerProgress {
        return copy(cursesEndured = cursesEndured + 1)
    }
    
    fun addBlessing(): PlayerProgress {
        return copy(blessingsReceived = blessingsReceived + 1)
    }
    
    private fun calculateRequiredExperience(level: Int): Int {
        return (100 * (1.5f.pow(level - 1))).toInt()
    }
    
    private fun calculateAchievementExperience(achievement: Achievement): Int {
        return when (achievement.rarity) {
            AchievementRarity.COMMON -> 50
            AchievementRarity.UNCOMMON -> 100
            AchievementRarity.RARE -> 200
            AchievementRarity.EPIC -> 500
            AchievementRarity.LEGENDARY -> 1000
            AchievementRarity.DEMONIC -> 2000
        }
    }
    
    companion object {
        fun createNew(): PlayerProgress = PlayerProgress()
    }
} 