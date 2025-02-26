package com.walhalla.wvcompose7.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.walhalla.wvcompose7.game.achievements.models.Achievement

@Composable
fun AchievementsScreen(
    modifier: Modifier = Modifier
) {
    val gradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        )
    )
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(gradient)
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "ДОСТИЖЕНИЯ",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Путь к величию",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            AchievementsList()
        }
    }
}

@Composable
private fun AchievementsList() {
    Column {
        AchievementCard(
            title = "Первая Победа",
            description = "Выиграйте свою первую игру",
            points = 100,
            isUnlocked = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        AchievementCard(
            title = "Счастливчик",
            description = "Выиграйте 5 раз подряд",
            points = 500,
            isUnlocked = false
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        AchievementCard(
            title = "Высокий Роллер",
            description = "Сделайте ставку в 10,000 монет",
            points = 1000,
            isUnlocked = false
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        AchievementCard(
            title = "Мастер Блэкджека",
            description = "Получите 10 блэкджеков",
            points = 2000,
            isUnlocked = false
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        AchievementCard(
            title = "Демонический Пакт",
            description = "Заключите сделку с демоном",
            points = 5000,
            isUnlocked = false
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AchievementCard(
    title: String,
    description: String,
    points: Int,
    isUnlocked: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { },
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = if (isUnlocked) "Разблокировано" else "Заблокировано",
                fontSize = 16.sp,
                color = if (isUnlocked) 
                    MaterialTheme.colorScheme.primary
                else 
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = description,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "$points очков",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
} 