package com.walhalla.wvcompose4.game

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun SlotMachineScreen() {
    var credits by remember { mutableStateOf(1000) }
    var bet by remember { mutableStateOf(10) }
    var isSpinning by remember { mutableStateOf(false) }
    var symbols by remember { mutableStateOf(List(3) { 0 }) }
    
    // Символы для слот-машины: 🍒 🍊 🍋 🍇 💎 7️⃣
    val slotSymbols = listOf("🍒", "🍊", "🍋", "🍇", "💎", "7️⃣")
    
    // Анимация вращения
    val spinAnimation = rememberInfiniteTransition()
    val spinPosition = spinAnimation.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Кредиты: $credits",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Дисплей слот-машины
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            symbols.forEach { symbol ->
                Text(
                    text = if (isSpinning) 
                        slotSymbols[(spinPosition.value * slotSymbols.size).toInt() % slotSymbols.size]
                    else 
                        slotSymbols[symbol],
                    style = MaterialTheme.typography.displayLarge
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Управление ставкой
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { if (bet > 10) bet -= 10 },
                enabled = !isSpinning
            ) {
                Text("-10")
            }
            
            Text(
                text = "Ставка: $bet",
                style = MaterialTheme.typography.titleLarge
            )
            
            Button(
                onClick = { if (credits >= bet + 10) bet += 10 },
                enabled = !isSpinning
            ) {
                Text("+10")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Кнопка SPIN
        Button(
            onClick = {
                if (credits >= bet) {
                    credits -= bet
                    isSpinning = true
                    // Запускаем вращение на 2 секунды

                    kotlinx.coroutines.GlobalScope.launch {
                        kotlinx.coroutines.delay(2000)
                        symbols = List(3) { Random.nextInt(slotSymbols.size) }
                        isSpinning = false
                        
                        // Проверяем выигрыш
                        val win = when {
                            symbols.all { it == symbols[0] } -> bet * 10 // Три одинаковых символа
                            symbols.count { it == 5 } >= 2 -> bet * 5    // Два или более 7️⃣
                            symbols.count { it == 4 } >= 2 -> bet * 3    // Два или более 💎
                            else -> 0
                        }
                        credits += win
                    }
                }
            },
            enabled = !isSpinning && credits >= bet,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(if (isSpinning) "ВРАЩАЕТСЯ..." else "КРУТИТЬ!")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Таблица выигрышей
        Text(
            text = """
                Выигрыши:
                Три одинаковых символа - ставка x10
                Два или более 7️⃣ - ставка x5
                Два или более 💎 - ставка x3
            """.trimIndent(),
            style = MaterialTheme.typography.bodyMedium
        )
    }
} 