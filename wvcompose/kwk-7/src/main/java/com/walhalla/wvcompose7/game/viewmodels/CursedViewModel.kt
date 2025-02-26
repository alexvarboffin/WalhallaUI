package com.walhalla.wvcompose7.game.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CursedViewModel : ViewModel() {
    private val _activeCurses = MutableStateFlow<Set<Curse>>(emptySet())
    val activeCurses: StateFlow<Set<Curse>> = _activeCurses.asStateFlow()
    
    private val _soulPower = MutableStateFlow(0)
    val soulPower: StateFlow<Int> = _soulPower.asStateFlow()
    
    fun acceptCurse() {
        viewModelScope.launch {
            // Увеличиваем силу души
            _soulPower.value += 100
            
            // Добавляем случайное проклятие
            val newCurse = Curse.values().random()
            _activeCurses.value = _activeCurses.value + newCurse
        }
    }
    
    fun removeCurse(curse: Curse) {
        if (_soulPower.value >= 500) {
            _soulPower.value -= 500
            _activeCurses.value = _activeCurses.value - curse
        }
    }
    
    fun hasCurse(curse: Curse): Boolean {
        return curse in _activeCurses.value
    }
}

enum class Curse {
    GREED {
        override val power = "Увеличивает все выигрыши на 50%"
        override val drawback = "Каждый проигрыш удваивается"
    },
    GAMBLING {
        override val power = "Шанс выигрыша увеличен на 25%"
        override val drawback = "Невозможно остановить игру при выигрышной серии"
    },
    POWER {
        override val power = "Возможность управлять результатом раз в день"
        override val drawback = "Потеря всех накоплений при проигрыше"
    };
    
    abstract val power: String
    abstract val drawback: String
} 