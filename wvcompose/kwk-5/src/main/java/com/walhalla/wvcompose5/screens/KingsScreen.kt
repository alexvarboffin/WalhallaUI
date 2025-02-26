package com.walhalla.wvcompose5.screens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun KingsScreen() {
    val topPlayers = listOf(
        Player("Alexander Great", "10,000,000", "💎 Diamond"),
        Player("Queen Victoria", "8,500,000", "👑 Gold"),
        Player("King Arthur", "7,200,000", "👑 Gold"),
        Player("Caesar", "6,800,000", "🌟 Silver"),
        Player("Cleopatra", "6,500,000", "🌟 Silver"),
        Player("Napoleon", "6,200,000", "🌟 Silver"),
        Player("Genghis Khan", "5,900,000", "🌟 Silver"),
        Player("Elizabeth I", "5,600,000", "🌟 Silver"),
        Player("Louis XIV", "5,300,000", "🌟 Silver"),
        Player("Ivan Great", "5,000,000", "🌟 Silver")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B1B1B))
            .padding(16.dp)
    ) {
        // Заголовок
        Text(
            "КОРОЛИ РУЛЕТКИ",
            color = Color(0xFFFFD700),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
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
                1 -> Color(0xFF2C2C2C).copy(alpha = 0.9f)
                2 -> Color(0xFF2C2C2C).copy(alpha = 0.8f)
                3 -> Color(0xFF2C2C2C).copy(alpha = 0.7f)
                else -> Color(0xFF2C2C2C).copy(alpha = 0.6f)
            }
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Позиция и имя
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
                                1 -> Color(0xFFFFD700)
                                2 -> Color(0xFFC0C0C0)
                                3 -> Color(0xFFCD7F32)
                                else -> Color(0xFF4CAF50)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        position.toString(),
                        color = if (position <= 3) Color.Black else Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column {
                    Text(
                        player.name,
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        player.status,
                        color = Color(0xFFFFD700),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Выигрыш
            Text(
                "$ ${player.winnings}",
                color = Color(0xFF4CAF50),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private data class Player(
    val name: String,
    val winnings: String,
    val status: String
) 