package com.walhalla.wvcompose4.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun LeaderboardScreen() {
    val leaders = remember {
        listOf(
            LeaderboardItem("Игрок 1", 15000),
            LeaderboardItem("Игрок 2", 12500),
            LeaderboardItem("Игрок 3", 10000),
            LeaderboardItem("Игрок 4", 7500),
            LeaderboardItem("Игрок 5", 5000),
            LeaderboardItem("Игрок 6", 4000),
            LeaderboardItem("Игрок 7", 3000),
            LeaderboardItem("Игрок 8", 2000),
            LeaderboardItem("Игрок 9", 1500),
            LeaderboardItem("Игрок 10", 1000)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Таблица лидеров",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            LazyColumn(
                modifier = Modifier.padding(16.dp)
            ) {
                itemsIndexed(leaders) { index, leader ->
                    LeaderboardRow(
                        position = index + 1,
                        name = leader.name,
                        score = leader.score
                    )
                    if (index < leaders.lastIndex) {
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun LeaderboardRow(
    position: Int,
    name: String,
    score: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$position.",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(40.dp)
            )
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Text(
            text = score.toString(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

private data class LeaderboardItem(
    val name: String,
    val score: Int
) 