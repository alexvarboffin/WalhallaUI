package com.walhalla.wvcompose7.game.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walhalla.wvcompose7.game.mechanics.LuckManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RouletteViewModel : ViewModel() {
    private val _playerBalance = MutableStateFlow(1000)
    val playerBalance: StateFlow<Int> = _playerBalance.asStateFlow()
    
    private val _currentBet = MutableStateFlow(0)
    val currentBet: StateFlow<Int> = _currentBet.asStateFlow()
    
    private val _lastNumber = MutableStateFlow<Int?>(null)
    val lastNumber: StateFlow<Int?> = _lastNumber.asStateFlow()
    
    private val _isSpinning = MutableStateFlow(false)
    val isSpinning: StateFlow<Boolean> = _isSpinning.asStateFlow()
    
    private val _winAmount = MutableStateFlow(0)
    val winAmount: StateFlow<Int> = _winAmount.asStateFlow()
    
    private val luckManager = LuckManager()
    
    private val bets = mutableMapOf<BetType, Int>()
    
    fun placeBet(type: BetType, amount: Int) {
        if (_playerBalance.value >= amount && !_isSpinning.value) {
            _playerBalance.value -= amount
            _currentBet.value += amount
            bets[type] = (bets[type] ?: 0) + amount
        }
    }
    
    fun onSpinComplete(number: Int) {
        viewModelScope.launch {
            _lastNumber.value = number
            _isSpinning.value = false
            
            var totalWin = 0
            bets.forEach { (betType, amount) ->
                if (betType.isWinning(number)) {
                    val win = amount * betType.payout
                    totalWin += win
                }
            }
            
            if (totalWin > 0) {
                // Применяем бонус удачи
                val luckBonus = luckManager.getLuckBonus()
                totalWin = (totalWin * (1 + luckBonus * 0.1f)).toInt()
                
                _playerBalance.value += totalWin
                _winAmount.value = totalWin
                luckManager.onGameResult(true, _currentBet.value, totalWin)
            } else {
                _winAmount.value = 0
                luckManager.onGameResult(false, _currentBet.value, 0)
            }
            
            // Сбрасываем ставки
            _currentBet.value = 0
            bets.clear()
        }
    }
    
    fun clearBets() {
        if (!_isSpinning.value) {
            _playerBalance.value += _currentBet.value
            _currentBet.value = 0
            bets.clear()
        }
    }
    
    fun startSpin() {
        if (_currentBet.value > 0 && !_isSpinning.value) {
            _isSpinning.value = true
            _winAmount.value = 0
        }
    }
}

sealed class BetType(val payout: Int) {
    class Straight(val number: Int) : BetType(35) {
        override fun isWinning(result: Int) = number == result
    }
    
    object Red : BetType(1) {
        override fun isWinning(result: Int) = result != 0 && result % 2 == 0
    }
    
    object Black : BetType(1) {
        override fun isWinning(result: Int) = result != 0 && result % 2 == 1
    }
    
    object Even : BetType(1) {
        override fun isWinning(result: Int) = result != 0 && result % 2 == 0
    }
    
    object Odd : BetType(1) {
        override fun isWinning(result: Int) = result != 0 && result % 2 == 1
    }
    
    object FirstHalf : BetType(1) {
        override fun isWinning(result: Int) = result in 1..18
    }
    
    object SecondHalf : BetType(1) {
        override fun isWinning(result: Int) = result in 19..36
    }
    
    object FirstDozen : BetType(2) {
        override fun isWinning(result: Int) = result in 1..12
    }
    
    object SecondDozen : BetType(2) {
        override fun isWinning(result: Int) = result in 13..24
    }
    
    object ThirdDozen : BetType(2) {
        override fun isWinning(result: Int) = result in 25..36
    }
    
    abstract fun isWinning(result: Int): Boolean
} 