package com.walhalla.wvcompose6.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.walhalla.wvcompose6.ui.theme.*

@Composable
fun ProfileScreen() {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        DarkBackground,
                        RubyRed.copy(alpha = 0.1f),
                        DarkBackground
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // VIP статус
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "💎 DIAMOND VIP",
                        color = GoldColor,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Максимальный уровень привилегий",
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        VipStat("Статус", "Diamond Master")
                        VipStat("ID", "#VIP777")
                        VipStat("Уровень", "50")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Статистика
            Text(
                "СТАТИСТИКА",
                color = GoldColor,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    StatRow("Общий выигрыш", "$ 1,234,567")
                    StatRow("Максимальный выигрыш", "$ 100,000")
                    StatRow("Серия побед", "7 игр")
                    StatRow("Любимая игра", "Блэкджек")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Достижения
            Text(
                "ДОСТИЖЕНИЯ",
                color = GoldColor,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Achievement("🎯 Счастливчик", "10 побед подряд", true)
                    Achievement("💎 VIP Игрок", "Достигнут Diamond статус", true)
                    Achievement("👑 Король казино", "Выигрыш более $1,000,000", true)
                    Achievement("🌟 Легенда", "100 выигрышных игр", false)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Кнопка депозита
            Button(
                onClick = { /* TODO */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldColor,
                    contentColor = DarkBackground
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "ПОПОЛНИТЬ СЧЕТ",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun VipStat(
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            label,
            color = TextSecondary,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            value,
            color = GoldColor,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun StatRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            color = TextSecondary,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            value,
            color = WinningsGreen,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun Achievement(
    title: String,
    description: String,
    achieved: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                title,
                color = if (achieved) GoldColor else TextSecondary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                description,
                color = TextSecondary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        if (achieved) {
            Text(
                "✓",
                color = WinningsGreen,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
} 