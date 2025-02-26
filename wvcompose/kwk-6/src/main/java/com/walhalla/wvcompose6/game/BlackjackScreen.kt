package com.walhalla.wvcompose6.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.walhalla.wvcompose6.game.models.*
import com.walhalla.wvcompose6.ui.theme.*

@Composable
fun BlackjackScreen() {
    var credits by remember { mutableStateOf(5000) }
    var currentBet by remember { mutableStateOf(100) }
    var dealerCards by remember { mutableStateOf(listOf<Card>()) }
    var playerCards by remember { mutableStateOf(listOf<Card>()) }
    var gameState by remember { mutableStateOf(GameState.BETTING) }
    var deck by remember { mutableStateOf(createShuffledDeck()) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // VIP статус и баланс
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CardBackground)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "VIP STATUS",
                        color = GoldColor,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        "GOLD MEMBER",
                        color = GoldColor,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "БАЛАНС",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        "$ $credits",
                        color = WinningsGreen,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Игровой стол
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(TableGreen, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Карты дилера
                CardRow(
                    cards = dealerCards,
                    title = "ДИЛЕР"
                )
                
                // Карты игрока
                CardRow(
                    cards = playerCards,
                    title = "ИГРОК"
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Ставки и кнопки управления
        if (gameState == GameState.BETTING) {
            BettingControls(
                currentBet = currentBet,
                onBetChange = { newBet ->
                    if (newBet >= 100 && newBet <= credits) {
                        currentBet = newBet
                    }
                },
                onDeal = {
                    credits -= currentBet
                    gameState = GameState.PLAYING
                    deck = createShuffledDeck()
                    dealerCards = listOf(
                        deck.removeAt(0).copy(isFaceUp = true),
                        deck.removeAt(0).copy(isFaceUp = false)
                    )
                    playerCards = listOf(
                        deck.removeAt(0).copy(isFaceUp = true),
                        deck.removeAt(0).copy(isFaceUp = true)
                    )
                },
                maxBet = credits
            )
        } else if (gameState == GameState.GAME_OVER) {
            Button(
                onClick = { 
                    gameState = GameState.BETTING
                    dealerCards = listOf()
                    playerCards = listOf()
                    currentBet = 100.coerceAtMost(credits)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldColor,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    "НОВАЯ ИГРА",
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            GameControls(
                onHit = {
                    playerCards = playerCards + deck.removeAt(0).copy(isFaceUp = true)
                    if (calculateHandValue(playerCards) > 21) {
                        gameState = GameState.GAME_OVER
                    }
                },
                onStand = {
                    // Открываем вторую карту дилера
                    dealerCards = dealerCards.mapIndexed { index, card ->
                        if (index == 1) card.copy(isFaceUp = true) else card
                    }
                    
                    // Дилер берет карты, пока сумма меньше 17
                    var dealerValue = calculateHandValue(dealerCards)
                    while (dealerValue < 17) {
                        dealerCards = dealerCards + deck.removeAt(0).copy(isFaceUp = true)
                        dealerValue = calculateHandValue(dealerCards)
                    }
                    
                    // Определяем победителя
                    val playerValue = calculateHandValue(playerCards)
                    when {
                        dealerValue > 21 -> {
                            // Дилер перебрал - игрок выиграл
                            credits += currentBet * 2
                        }
                        playerValue > dealerValue -> {
                            // Игрок выиграл
                            credits += currentBet * 2
                        }
                        playerValue == dealerValue -> {
                            // Ничья
                            credits += currentBet
                        }
                        // Иначе дилер выиграл - деньги уже списаны
                    }
                    
                    gameState = GameState.GAME_OVER
                },
                onDouble = {
                    if (credits >= currentBet) {
                        credits -= currentBet
                        currentBet *= 2
                        // Добавляем одну карту и завершаем ход
                        playerCards = playerCards + deck.removeAt(0).copy(isFaceUp = true)
                        
                        // Сразу переходим к ходу дилера
                        dealerCards = dealerCards.mapIndexed { index, card ->
                            if (index == 1) card.copy(isFaceUp = true) else card
                        }
                        
                        var dealerValue = calculateHandValue(dealerCards)
                        while (dealerValue < 17) {
                            dealerCards = dealerCards + deck.removeAt(0).copy(isFaceUp = true)
                            dealerValue = calculateHandValue(dealerCards)
                        }
                        
                        // Определяем победителя
                        val playerValue = calculateHandValue(playerCards)
                        when {
                            playerValue > 21 -> {
                                // Игрок перебрал - деньги уже списаны
                            }
                            dealerValue > 21 -> {
                                // Дилер перебрал - игрок выиграл
                                credits += currentBet * 2
                            }
                            playerValue > dealerValue -> {
                                // Игрок выиграл
                                credits += currentBet * 2
                            }
                            playerValue == dealerValue -> {
                                // Ничья
                                credits += currentBet
                            }
                            // Иначе дилер выиграл - деньги уже списаны
                        }
                        
                        gameState = GameState.GAME_OVER
                    }
                },
                enabled = gameState == GameState.PLAYING,
                canDouble = credits >= currentBet && playerCards.size == 2
            )
        }
    }
}

@Composable
private fun CardRow(
    cards: List<Card>,
    title: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            title,
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            cards.forEach { card ->
                PlayingCard(card = card)
            }
        }
    }
}

@Composable
private fun PlayingCard(
    card: Card,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .size(width = 60.dp, height = 90.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        if (card.isFaceUp) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = card.getDisplayValue(),
                    color = if (card.suit in listOf(Suit.HEARTS, Suit.DIAMONDS)) 
                        BetRed else Color.Black,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = card.getSymbol(),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(TableGreen)
            )
        }
    }
}

@Composable
private fun BettingControls(
    currentBet: Int,
    onBetChange: (Int) -> Unit,
    onDeal: () -> Unit,
    maxBet: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { onBetChange(currentBet - 100) },
                enabled = currentBet > 100,
                colors = ButtonDefaults.buttonColors(
                    containerColor = BetRed
                )
            ) {
                Text("-100")
            }
            
            Text(
                text = "Ставка: $currentBet",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )
            
            Button(
                onClick = { onBetChange(currentBet + 100) },
                enabled = currentBet + 100 <= maxBet,
                colors = ButtonDefaults.buttonColors(
                    containerColor = WinningsGreen
                )
            ) {
                Text("+100")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = onDeal,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = GoldColor,
                contentColor = Color.Black
            )
        ) {
            Text(
                "СДЕЛАТЬ СТАВКУ",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun GameControls(
    onHit: () -> Unit,
    onStand: () -> Unit,
    onDouble: () -> Unit,
    enabled: Boolean,
    canDouble: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = onHit,
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = WinningsGreen
            )
        ) {
            Text("ЕЩЁ")
        }
        
        Button(
            onClick = onStand,
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = BetRed
            )
        ) {
            Text("ХВАТИТ")
        }
        
        Button(
            onClick = onDouble,
            enabled = enabled && canDouble,
            colors = ButtonDefaults.buttonColors(
                containerColor = GoldColor,
                contentColor = Color.Black
            )
        ) {
            Text("УДВОИТЬ")
        }
    }
}

enum class GameState {
    BETTING, PLAYING, DEALER_TURN, GAME_OVER
}

private fun createShuffledDeck(): MutableList<Card> {
    val deck = mutableListOf<Card>()
    for (suit in Suit.values()) {
        for (rank in Rank.values()) {
            deck.add(Card(suit = suit, rank = rank))
        }
    }
    deck.shuffle()
    return deck
}

private fun calculateHandValue(cards: List<Card>): Int {
    var sum = 0
    var numAces = 0
    
    // Сначала подсчитаем все карты, считая тузы за 11
    for (card in cards) {
        when (card.rank) {
            Rank.ACE -> {
                sum += 11
                numAces++
            }
            Rank.KING, Rank.QUEEN, Rank.JACK -> sum += 10
            else -> sum += card.rank.ordinal + 2 // от 2 до 10
        }
    }
    
    // Уменьшаем значение тузов с 11 до 1, пока сумма больше 21
    while (sum > 21 && numAces > 0) {
        sum -= 10
        numAces--
    }
    
    return sum
} 