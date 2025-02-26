package com.walhalla.wvcompose7.game.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VipViewModel : ViewModel() {
    private val _vipLevel = MutableStateFlow(0)
    val vipLevel: StateFlow<Int> = _vipLevel.asStateFlow()
    
    private val _vipPoints = MutableStateFlow(0)
    val vipPoints: StateFlow<Int> = _vipPoints.asStateFlow()
    
    private val _unlockedBenefits = MutableStateFlow<Set<VipBenefit>>(emptySet())
    val unlockedBenefits: StateFlow<Set<VipBenefit>> = _unlockedBenefits.asStateFlow()
    
    fun purchaseVip() {
        viewModelScope.launch {
            _vipLevel.value++
            _vipPoints.value += 1000
            
            // Разблокируем новые привилегии в зависимости от уровня
            val newBenefits = when (_vipLevel.value) {
                1 -> setOf(VipBenefit.DAILY_BONUS, VipBenefit.CASHBACK)
                2 -> setOf(VipBenefit.DAILY_BONUS, VipBenefit.CASHBACK, VipBenefit.EXCLUSIVE_GAMES)
                3 -> setOf(VipBenefit.DAILY_BONUS, VipBenefit.CASHBACK, VipBenefit.EXCLUSIVE_GAMES, VipBenefit.PERSONAL_MANAGER)
                else -> setOf(VipBenefit.DAILY_BONUS, VipBenefit.CASHBACK, VipBenefit.EXCLUSIVE_GAMES, VipBenefit.PERSONAL_MANAGER, VipBenefit.LUXURY_EVENTS)
            }
            _unlockedBenefits.value = newBenefits
        }
    }
    
    fun hasBenefit(benefit: VipBenefit): Boolean {
        return benefit in _unlockedBenefits.value
    }
}

enum class VipBenefit(val title: String, val description: String, val requiredLevel: Int) {
    DAILY_BONUS(
        "Ежедневный Бонус",
        "Получайте 1000 монет каждый день",
        1
    ),
    CASHBACK(
        "Кэшбэк",
        "Возврат 5% от всех ставок",
        1
    ),
    EXCLUSIVE_GAMES(
        "Эксклюзивные Игры",
        "Доступ к VIP-играм с повышенными ставками",
        2
    ),
    PERSONAL_MANAGER(
        "Персональный Менеджер",
        "Круглосуточная поддержка и особые предложения",
        3
    ),
    LUXURY_EVENTS(
        "Элитные Мероприятия",
        "Приглашения на закрытые турниры",
        4
    )
} 