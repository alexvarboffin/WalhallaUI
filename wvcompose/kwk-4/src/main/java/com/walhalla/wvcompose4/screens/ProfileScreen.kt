package com.walhalla.wvcompose4.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Профиль игрока",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                ProfileItem(
                    icon = Icons.Default.Person,
                    label = "Имя",
                    value = "Игрок 1"
                )
                
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                
                ProfileItem(
                    icon = Icons.Default.Star,
                    label = "Рейтинг",
                    value = "1234"
                )
                
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                
                ProfileItem(
                    icon = Icons.Default.AttachMoney,
                    label = "Всего выиграно",
                    value = "15000"
                )
                
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                
                ProfileItem(
                    icon = Icons.Default.Casino,
                    label = "Игр сыграно",
                    value = "42"
                )
                
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                
                ProfileItem(
                    icon = Icons.Default.EmojiEvents,
                    label = "Лучший выигрыш",
                    value = "5000"
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Достижения",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Achievement(
                    title = "Новичок",
                    description = "Сыграйте первую игру",
                    completed = true
                )
                
                Achievement(
                    title = "Счастливчик",
                    description = "Выиграйте 1000 кредитов за одну игру",
                    completed = true
                )
                
                Achievement(
                    title = "Профессионал",
                    description = "Выиграйте 10000 кредитов всего",
                    completed = false
                )
            }
        }
    }
}

@Composable
private fun ProfileItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun Achievement(
    title: String,
    description: String,
    completed: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (completed) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (completed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
} 