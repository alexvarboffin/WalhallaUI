package com.walhalla.wvcompose7.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.walhalla.wvcompose7.game.theme.*

@Composable
fun SinsScreen() {
    val sins = listOf(
        Sin(
            title = "🔥 ГОРДЫНЯ",
            description = "Стань лучшим игроком",
            reward = "x666 к выигрышу"
        ),
        Sin(
            title = "💰 АЛЧНОСТЬ",
            description = "Собери максимальный банк",
            reward = "Золотой статус"
        ),
        Sin(
            title = "😈 ПОХОТЬ",
            description = "Испытай все удовольствия",
            reward = "VIP доступ"
        ),
        Sin(
            title = "⚔️ ГНЕВ",
            description = "Покори всех противников",
            reward = "Особые привилегии"
        ),
        Sin(
            title = "🍷 ЧРЕВОУГОДИЕ",
            description = "Насладись всеми играми",
            reward = "Бонус на депозит"
        ),
        Sin(
            title = "💀 ЗАВИСТЬ",
            description = "Превзойди других игроков",
            reward = "Элитный статус"
        ),
        Sin(
            title = "😴 ЛЕНЬ",
            description = "Получай пассивный доход",
            reward = "Автоматические ставки"
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        DarkAbyss,
                        BloodRed.copy(alpha = 0.1f),
                        DarkAbyss
                    )
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "СМЕРТНЫЕ ГРЕХИ",
                    color = HellFire,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Text(
                    "Поддайся искушению",
                    color = TextLight,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }

            items(sins) { sin ->
                SinCard(sin)
            }
        }
    }
}

@Composable
private fun SinCard(sin: Sin) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkSurface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                sin.title,
                color = CursedGold,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                sin.description,
                color = TextLight,
                style = MaterialTheme.typography.bodyLarge
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Награда:",
                    color = TextLight,
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Text(
                    sin.reward,
                    color = HellFire,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private data class Sin(
    val title: String,
    val description: String,
    val reward: String
) 