package com.walhalla.wvcompose7.game.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DemonsViewModel : ViewModel() {
    private val _activePacts = MutableStateFlow<Set<Demon>>(emptySet())
    val activePacts: StateFlow<Set<Demon>> = _activePacts.asStateFlow()
    
    private val _demonicPower = MutableStateFlow(0)
    val demonicPower: StateFlow<Int> = _demonicPower.asStateFlow()
    
    fun makeDemonPact() {
        viewModelScope.launch {
            // Увеличиваем демоническую силу
            _demonicPower.value += 666
            
            // Заключаем пакт со случайным демоном
            val newDemon = Demon.values().random()
            _activePacts.value = _activePacts.value + newDemon
        }
    }
    
    fun breakPact(demon: Demon) {
        if (_demonicPower.value >= 1000) {
            _demonicPower.value -= 1000
            _activePacts.value = _activePacts.value - demon
        }
    }
    
    fun hasPact(demon: Demon): Boolean {
        return demon in _activePacts.value
    }
}

enum class Demon {
    MAMMON {
        override val title = "Князь Алчности"
        override val power = "Утраивает все выигрыши"
        override val challenge = "Потеря души при банкротстве"
    },
    ASMODEUS {
        override val title = "Князь Азарта"
        override val power = "Гарантированный джекпот раз в день"
        override val challenge = "Невозможность выйти из игры при проигрыше"
    },
    BELIAL {
        override val title = "Князь Обмана"
        override val power = "Возможность подменять карты"
        override val challenge = "Случайная потеря половины баланса"
    };
    
    abstract val title: String
    abstract val power: String
    abstract val challenge: String
} 