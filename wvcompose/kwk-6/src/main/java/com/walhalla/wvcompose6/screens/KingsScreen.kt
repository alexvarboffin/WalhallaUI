package com.walhalla.wvcompose6.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.walhalla.wvcompose6.ui.theme.*

@Composable
fun KingsScreen() {
    val topPlayers = listOf(
        Player("James Bond", "10,000,000", "💎 Diamond Master", "42 победы подряд"),
        Player("Lady Luck", "8,500,000", "👑 Gold Elite", "Легендарный игрок"),
        Player("Lucky Devil", "7,200,000", "👑 Gold Elite", "Везунчик дня"),
        Player("Dark Knight", "6,800,000", "🌟 Silver Pro", "Ночной король"),
        Player("Red Queen", "6,500,000", "🌟 Silver Pro", "Королева блэкджека"),
        Player("Golden Boy", "6,200,000", "🌟 Silver Pro", "Счастливчик"),
        Player("Lucky Star", "5,900,000", "🌟 Silver Pro", "Восходящая звезда"),
        Player("Black Jack", "5,600,000", "🌟 Silver Pro", "Мастер карт"),
        Player("Royal Flush", "5,300,000", "🌟 Silver Pro", "Покоритель казино"),
        Player("Wild Card", "5,000,000", "🌟 Silver Pro", "Непредсказуемый")
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        DarkBackground,
                        PurpleAccent.copy(alpha = 0.1f),
                        DarkBackground
                    )
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    "ЛЕГЕНДЫ БЛЭКДЖЕКА",
                    color = GoldColor,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    "Лучшие игроки этой недели",
                    color = TextSecondary,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }

            itemsIndexed(topPlayers) { index, player ->
                PlayerCard(player, index + 1)
            }
        }
    }
}

@Composable
private fun PlayerCard(player: Player, position: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (position) {
                1 -> CardBackground.copy(alpha = 0.95f)
                2 -> CardBackground.copy(alpha = 0.9f)
                3 -> CardBackground.copy(alpha = 0.85f)
                else -> CardBackground.copy(alpha = 0.8f)
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Номер позиции
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            when (position) {
                                1 -> GoldColor
                                2 -> RubyRed
                                3 -> PurpleAccent
                                else -> WinningsGreen
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        position.toString(),
                        color = DarkBackground,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column {
                    Text(
                        player.name,
                        color = TextPrimary,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        player.status,
                        color = GoldColor,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        player.achievement,
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Выигрыш
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    "$ ${player.winnings}",
                    color = WinningsGreen,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private data class Player(
    val name: String,
    val winnings: String,
    val status: String,
    val achievement: String
) 