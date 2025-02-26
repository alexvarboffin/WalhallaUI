package com.walhalla.wvcompose6.game.models

class Game {
    private val deck = mutableListOf<Card>()
    private var currentIndex = 0
    
    init {
        resetDeck()
    }
    
    private fun resetDeck() {
        deck.clear()
        for (suit in Suit.values()) {
            for (rank in Rank.values()) {
                deck.add(Card(suit, rank))
            }
        }
        deck.shuffle()
        currentIndex = 0
    }
    
    fun drawCard(faceUp: Boolean = true): Card {
        if (currentIndex >= deck.size) {
            resetDeck()
        }
        return deck[currentIndex++].copy(isFaceUp = faceUp)
    }
    
    fun calculateHandValue(cards: List<Card>): Int {
        var sum = 0
        var numAces = 0
        
        // Сначала считаем все карты кроме тузов
        for (card in cards) {
            if (card.rank == Rank.ACE) {
                numAces++
            } else {
                sum += card.getValue()
            }
        }
        
        // Теперь добавляем тузы
        for (i in 0 until numAces) {
            sum += if (sum + 11 <= 21) 11 else 1
        }
        
        return sum
    }
    
    fun dealInitialCards(): Pair<List<Card>, List<Card>> {
        val playerCards = listOf(drawCard(), drawCard())
        val dealerCards = listOf(drawCard(), drawCard(faceUp = false))
        return Pair(dealerCards, playerCards)
    }
    
    fun playDealer(dealerCards: MutableList<Card>, playerValue: Int) {
        // Открываем вторую карту дилера
        dealerCards[1] = dealerCards[1].copy(isFaceUp = true)
        
        // Дилер берет карты, пока у него меньше 17
        while (calculateHandValue(dealerCards) < 17) {
            dealerCards.add(drawCard())
        }
    }
    
    fun determineWinner(dealerCards: List<Card>, playerCards: List<Card>): GameResult {
        val dealerValue = calculateHandValue(dealerCards)
        val playerValue = calculateHandValue(playerCards)
        
        return when {
            playerValue > 21 -> GameResult.DEALER_WINS
            dealerValue > 21 -> GameResult.PLAYER_WINS
            playerValue > dealerValue -> GameResult.PLAYER_WINS
            dealerValue > playerValue -> GameResult.DEALER_WINS
            else -> GameResult.PUSH
        }
    }
}

enum class GameResult {
    PLAYER_WINS,
    DEALER_WINS,
    PUSH // Ничья
} 