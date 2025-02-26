package com.walhalla.wvcompose7.game.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walhalla.wvcompose7.game.models.*
import com.walhalla.wvcompose7.game.mechanics.LuckManager
import com.walhalla.wvcompose7.game.bonus.DevilsDealManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class CasinoViewModel : ViewModel() {
    private val _gameState = MutableStateFlow<GameState>(GameState.INTRO)
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()
    
    private val _playerCards = MutableStateFlow<List<Card>>(emptyList())
    val playerCards: StateFlow<List<Card>> = _playerCards.asStateFlow()
    
    private val _splitCards = MutableStateFlow<List<Card>>(emptyList())
    val splitCards: StateFlow<List<Card>> = _splitCards.asStateFlow()
    
    private val _dealerCards = MutableStateFlow<List<Card>>(emptyList())
    val dealerCards: StateFlow<List<Card>> = _dealerCards.asStateFlow()
    
    private val _playerBalance = MutableStateFlow(1000)
    val playerBalance: StateFlow<Int> = _playerBalance.asStateFlow()
    
    private val _currentBet = MutableStateFlow(0)
    val currentBet: StateFlow<Int> = _currentBet.asStateFlow()
    
    private val _isPlayingSplitHand = MutableStateFlow(false)
    val isPlayingSplitHand: StateFlow<Boolean> = _isPlayingSplitHand.asStateFlow()
    
    private val _splitBet = MutableStateFlow(0)
    val splitBet: StateFlow<Int> = _splitBet.asStateFlow()
    
    private val luckManager = LuckManager()
    private val devilsDealManager = DevilsDealManager()
    private val deck = mutableListOf<Card>()
    
    init {
        initializeDeck()
    }
    
    private fun initializeDeck() {
        deck.clear()
        for (suit in Suit.values()) {
            for (rank in Rank.values()) {
                deck.add(Card(suit, rank))
            }
        }
        deck.shuffle()
    }
    
    fun placeBet(amount: Int) {
        if (_playerBalance.value >= amount && _gameState.value == GameState.BETTING) {
            _currentBet.value = amount
            _playerBalance.value -= amount
            _gameState.value = GameState.PLAYING
            dealInitialCards()
        }
    }
    
    private fun dealInitialCards() {
        viewModelScope.launch {
            // Раздаем карты игроку и дилеру
            val playerCard1 = drawCard()
            val dealerCard1 = drawCard(isFaceUp = true)
            val playerCard2 = drawCard()
            val dealerCard2 = drawCard(isFaceUp = false)
            
            _playerCards.value = listOf(playerCard1, playerCard2)
            _dealerCards.value = listOf(dealerCard1, dealerCard2)
            
            // Проверяем блэкджек у игрока
            if (calculateHandValue(_playerCards.value) == 21) {
                handleBlackjack()
            }
        }
    }
    
    private fun drawCard(isFaceUp: Boolean = true): Card {
        if (deck.isEmpty()) {
            initializeDeck()
        }
        return deck.removeFirst().copy(isFaceUp = isFaceUp)
    }
    
    fun hit() {
        if (_gameState.value == GameState.PLAYING) {
            val newCard = drawCard()
            _playerCards.value = _playerCards.value + newCard
            
            val handValue = calculateHandValue(_playerCards.value)
            when {
                handValue > 21 -> handleBust()
                handValue == 21 -> stand()
            }
        }
    }
    
    fun stand() {
        if (_gameState.value == GameState.PLAYING) {
            _gameState.value = GameState.DEALER
            playDealerTurn()
        }
    }
    
    private fun playDealerTurn() {
        viewModelScope.launch {
            // Открываем вторую карту дилера
            _dealerCards.value = _dealerCards.value.map { it.copy(isFaceUp = true) }
            
            var dealerValue = calculateHandValue(_dealerCards.value)
            while (dealerValue < 17) {
                val newCard = drawCard()
                _dealerCards.value = _dealerCards.value + newCard
                dealerValue = calculateHandValue(_dealerCards.value)
            }
            
            determineWinner()
        }
    }
    
    private fun calculateHandValue(cards: List<Card>): Int {
        var sum = 0
        var aces = 0
        
        for (card in cards) {
            if (!card.isFaceUp) continue
            
            when (card.rank) {
                Rank.ACE -> {
                    aces++
                    sum += 11
                }
                else -> sum += card.getValue()
            }
        }
        
        while (sum > 21 && aces > 0) {
            sum -= 10
            aces--
        }
        
        return sum
    }
    
    private fun handleBlackjack() {
        val winAmount = (_currentBet.value * 2.5).toInt()
        _playerBalance.value += winAmount
        luckManager.onGameResult(true, _currentBet.value, winAmount)
        _gameState.value = GameState.GAME_OVER
    }
    
    private fun handleBust() {
        luckManager.onGameResult(false, _currentBet.value, 0)
        _gameState.value = GameState.GAME_OVER
    }
    
    fun split() {
        if (_gameState.value == GameState.PLAYING && canSplit()) {
            // Списываем ставку для второй руки
            _playerBalance.value -= _currentBet.value
            _splitBet.value = _currentBet.value
            
            // Разделяем карты
            val firstCard = _playerCards.value[0]
            val secondCard = _playerCards.value[1]
            
            // Добавляем по одной карте к каждой руке
            _playerCards.value = listOf(firstCard, drawCard())
            _splitCards.value = listOf(secondCard, drawCard())
            
            _gameState.value = GameState.SPLIT
            _isPlayingSplitHand.value = false
        }
    }
    
    fun hitSplit() {
        if (_gameState.value == GameState.SPLIT) {
            val newCard = drawCard()
            if (_isPlayingSplitHand.value) {
                _splitCards.value = _splitCards.value + newCard
                val handValue = calculateHandValue(_splitCards.value)
                when {
                    handValue > 21 -> handleSplitBust()
                    handValue == 21 -> standSplit()
                }
            } else {
                _playerCards.value = _playerCards.value + newCard
                val handValue = calculateHandValue(_playerCards.value)
                when {
                    handValue > 21 -> handleFirstHandBust()
                    handValue == 21 -> standSplit()
                }
            }
        }
    }
    
    fun standSplit() {
        if (_gameState.value == GameState.SPLIT) {
            if (!_isPlayingSplitHand.value) {
                // Переключаемся на вторую руку
                _isPlayingSplitHand.value = true
            } else {
                // Обе руки сыграны, ход дилера
                _gameState.value = GameState.DEALER
                playDealerTurn()
            }
        }
    }
    
    private fun handleFirstHandBust() {
        // Первая рука перебор, переходим ко второй
        _isPlayingSplitHand.value = true
    }
    
    private fun handleSplitBust() {
        // Вторая рука перебор, переходим к дилеру
        _gameState.value = GameState.DEALER
        playDealerTurn()
    }
    
    private fun determineWinner() {
        val playerValue = calculateHandValue(_playerCards.value)
        val dealerValue = calculateHandValue(_dealerCards.value)
        
        if (_gameState.value == GameState.SPLIT) {
            // Определяем результат для обеих рук
            val splitValue = calculateHandValue(_splitCards.value)
            
            // Первая рука
            val firstHandWinAmount = calculateWinAmount(playerValue, dealerValue)
            // Вторая рука
            val secondHandWinAmount = calculateWinAmount(splitValue, dealerValue)
            
            // Обновляем баланс и статистику
            _playerBalance.value += firstHandWinAmount + secondHandWinAmount
            
            // Обновляем статистику удачи
            if (firstHandWinAmount > 0) {
                luckManager.onGameResult(true, _currentBet.value, firstHandWinAmount)
            } else {
                luckManager.onGameResult(false, _currentBet.value, 0)
            }
            
            if (secondHandWinAmount > 0) {
                luckManager.onGameResult(true, _splitBet.value, secondHandWinAmount)
            } else {
                luckManager.onGameResult(false, _splitBet.value, 0)
            }
        } else {
            // Обычная игра без сплита
            val winAmount = calculateWinAmount(playerValue, dealerValue)
            _playerBalance.value += winAmount
            
            if (winAmount > 0) {
                luckManager.onGameResult(true, _currentBet.value, winAmount)
            } else {
                luckManager.onGameResult(false, _currentBet.value, 0)
            }
        }
        
        _gameState.value = GameState.GAME_OVER
    }
    
    private fun calculateWinAmount(playerValue: Int, dealerValue: Int): Int {
        return when {
            playerValue > 21 -> 0
            dealerValue > 21 -> _currentBet.value * 2
            playerValue > dealerValue -> _currentBet.value * 2
            playerValue == dealerValue -> _currentBet.value
            else -> 0
        }
    }
    
    fun insurance() {
        if (_gameState.value == GameState.PLAYING && canInsurance()) {
            val insuranceCost = _currentBet.value / 2
            if (_playerBalance.value >= insuranceCost) {
                _playerBalance.value -= insuranceCost
                _gameState.value = GameState.INSURANCE
                
                // Проверяем есть ли у дилера блэкджек
                val dealerHasBlackjack = _dealerCards.value[1].rank == Rank.ACE || 
                                       _dealerCards.value[1].getValue() == 10
                
                if (dealerHasBlackjack) {
                    // Выплачиваем страховку 2:1
                    _playerBalance.value += insuranceCost * 3
                }
            }
        }
    }
    
    private fun canSplit(): Boolean {
        return _playerCards.value.size == 2 && 
               _playerCards.value[0].rank == _playerCards.value[1].rank &&
               _playerBalance.value >= _currentBet.value
    }
    
    private fun canInsurance(): Boolean {
        return _dealerCards.value.firstOrNull()?.rank == Rank.ACE &&
               _playerBalance.value >= _currentBet.value / 2
    }
    
    fun makeDemonPact() {
        if (!luckManager.getStats().isDemonPactActive) {
            luckManager.makeDemonPact()
            // Применяем эффекты демонического пакта
            _currentBet.value = (_currentBet.value * 1.5f).toInt()
        }
    }
    
    fun resetGame() {
        _gameState.value = GameState.BETTING
        _playerCards.value = emptyList()
        _splitCards.value = emptyList()
        _dealerCards.value = emptyList()
        _currentBet.value = 0
        _splitBet.value = 0
        _isPlayingSplitHand.value = false
        initializeDeck()
    }
    
    fun startBlackjack() {
        _gameState.value = GameState.BETTING
        initializeDeck()
    }
} 