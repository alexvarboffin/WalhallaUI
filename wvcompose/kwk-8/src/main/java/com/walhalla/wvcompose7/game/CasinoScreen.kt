package com.walhalla.wvcompose7.game

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
import com.walhalla.wvcompose7.game.models.GameState
import com.walhalla.wvcompose7.game.viewmodels.CasinoViewModel

@Composable
fun CasinoScreen(
    viewModel: CasinoViewModel,
    onNavigateToDevilsDeal: () -> Unit,
    onNavigateToAchievements: () -> Unit,
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
                text = "ВРАТА АДА",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Испытай свою удачу",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            GameCard(
                title = "Блэкджек",
                description = "Классическая игра с демоническим твистом",
                onClick = {
                    viewModel.startBlackjack()
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            GameCard(
                title = "Сделка с Дьяволом",
                description = "Рискни душой ради невероятных наград",
                onClick = onNavigateToDevilsDeal
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            GameCard(
                title = "Достижения",
                description = "Твой путь к величию",
                onClick = onNavigateToAchievements
            )
        }
    }
}

@Composable
private fun GameCard(
    title: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
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
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = description,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
} 