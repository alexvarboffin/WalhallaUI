package com.walhalla.wvcompose5.screens

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

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B1B1B))
            .padding(16.dp)
    ) {
        // Заголовок профиля
        Text(
            "VIP ПРОФИЛЬ",
            color = Color(0xFFFFD700),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Основная информация
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2C))
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                ProfileField("Статус", "💎 БРИЛЛИАНТОВЫЙ VIP", Color(0xFFFFD700))
                ProfileField("Имя", "Alexander Great", Color.White)
                ProfileField("ID", "#VIP7777777", Color(0xFF4CAF50))
                ProfileField("Баланс", "$ 1,234,567", Color(0xFF4CAF50))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Статистика
        Text(
            "СТАТИСТИКА",
            color = Color(0xFFFFD700),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2C))
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                StatisticField("Всего игр", "1,234")
                StatisticField("Выигрышей", "789")
                StatisticField("Максимальный выигрыш", "$ 100,000")
                StatisticField("Текущая серия", "5 побед")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Достижения
        Text(
            "ДОСТИЖЕНИЯ",
            color = Color(0xFFFFD700),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2C))
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                AchievementField("👑 Король рулетки", "Выиграйте 1,000,000 за одну игру")
                AchievementField("🎯 Счастливое число", "Выиграйте 10 раз подряд на одном числе")
                AchievementField("💎 VIP игрок", "Достигните бриллиантового статуса")
                AchievementField("🌟 Легенда казино", "Сыграйте 10,000 игр")
            }
        }
    }
}

@Composable
private fun ProfileField(label: String, value: String, valueColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            color = Color.Gray,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            value,
            color = valueColor,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun StatisticField(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            color = Color.Gray,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            value,
            color = Color(0xFF4CAF50),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun AchievementField(title: String, description: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            title,
            color = Color(0xFFFFD700),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            description,
            color = Color.Gray,
            style = MaterialTheme.typography.bodyMedium
        )
    }
} 