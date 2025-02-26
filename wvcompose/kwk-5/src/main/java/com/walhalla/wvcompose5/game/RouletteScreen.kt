package com.walhalla.wvcompose5.game

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun RouletteScreen() {
    var credits by remember { mutableStateOf(5000) }
    var currentBet by remember { mutableStateOf(100) }
    var isSpinning by remember { mutableStateOf(false) }
    var selectedNumber by remember { mutableStateOf<Int?>(null) }
    var lastWinningNumber by remember { mutableStateOf<Int?>(null) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B1B1B))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // VIP статус и баланс
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2C))
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
                        color = Color(0xFFFFD700),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        "GOLD MEMBER",
                        color = Color(0xFFFFD700),
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
                        color = Color(0xFF4CAF50),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Рулетка
        Box(
            modifier = Modifier
                .size(220.dp)
                .clip(RoundedCornerShape(140.dp))
                .background(Color(0xFF4CAF50))
                .padding(8.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // TODO: Добавить анимацию рулетки
            }
            
            if (lastWinningNumber != null) {
                Text(
                    text = lastWinningNumber.toString(),
                    color = Color.White,
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Ставки
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { if (currentBet > 100) currentBet -= 100 },
                enabled = !isSpinning,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD32F2F)
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
                onClick = { if (credits >= currentBet + 100) currentBet += 100 },
                enabled = !isSpinning,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)
                )
            ) {
                Text("+100")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Кнопка SPIN
        Button(
            onClick = {
                if (credits >= currentBet && selectedNumber != null) {
                    credits -= currentBet
                    isSpinning = true
                    // Имитация вращения


                    kotlinx.coroutines.GlobalScope.launch {
                        kotlinx.coroutines.delay(3000)
                        lastWinningNumber = Random.nextInt(0, 37)
                        isSpinning = false
                        
                        // Проверка выигрыша
                        if (lastWinningNumber == selectedNumber) {
                            credits += currentBet * 35 // Выигрыш 35 к 1
                        }
                    }
                }
            },
            enabled = !isSpinning && credits >= currentBet && selectedNumber != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFD700),
                contentColor = Color.Black
            )
        ) {
            Text(
                if (isSpinning) "ВРАЩАЕТСЯ..." else "СДЕЛАТЬ СТАВКУ!",
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Выбор числа
        Text(
            "Выберите число (0-36):",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Сетка чисел 3x12
        for (row in 0..2) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (col in 0..11) {
                    val number = row + col * 3 + 1
                    if (number <= 36) {
                        NumberButton(
                            number = number,
                            selected = selectedNumber == number,
                            onSelect = { selectedNumber = it }
                        )
                    }
                }
            }
        }
        
        // Зеро
        NumberButton(
            number = 0,
            selected = selectedNumber == 0,
            onSelect = { selectedNumber = it },
            isZero = true
        )
    }
}

@Composable
private fun NumberButton(
    number: Int,
    selected: Boolean,
    onSelect: (Int) -> Unit,
    isZero: Boolean = false
) {
    Button(
        onClick = { onSelect(number) },
        modifier = Modifier.size(40.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = when {
                selected -> Color(0xFFFFD700)
                isZero -> Color(0xFF4CAF50)
                number % 2 == 0 -> Color(0xFFD32F2F)
                else -> Color.Black
            }
        )
    ) {
        Text(
            text = number.toString(),
            color = if (selected) Color.Black else Color.White
        )
    }
} 