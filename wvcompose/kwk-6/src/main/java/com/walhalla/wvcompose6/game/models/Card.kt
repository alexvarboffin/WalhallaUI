package com.walhalla.wvcompose6.game.models

enum class Suit {
    HEARTS, DIAMONDS, CLUBS, SPADES
}

enum class Rank(val value: Int) {
    ACE(11),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    JACK(10),
    QUEEN(10),
    KING(10)
}

data class Card(
    val suit: Suit,
    val rank: Rank,
    val isFaceUp: Boolean = true
) {
    fun getSymbol(): String = when (suit) {
        Suit.HEARTS -> "♥️"
        Suit.DIAMONDS -> "♦️"
        Suit.CLUBS -> "♣️"
        Suit.SPADES -> "♠️"
    }
    
    fun getDisplayValue(): String = when (rank) {
        Rank.ACE -> "A"
        Rank.JACK -> "J"
        Rank.QUEEN -> "Q"
        Rank.KING -> "K"
        else -> rank.value.toString()
    }
    
    fun getValue(): Int = rank.value
    
    fun getAlternateValue(): Int = if (rank == Rank.ACE) 1 else rank.value
} 