package com.walhalla.wvcompose3.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun SlotMachineScreen() {
    val scrollState = rememberScrollState()
    val soundEffects = rememberGameSoundEffects()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var credits by remember { mutableStateOf(100) }
        var isSpinning by remember { mutableStateOf(false) }
        var slot1Value by remember { mutableStateOf(0) }
        var slot2Value by remember { mutableStateOf(0) }
        var slot3Value by remember { mutableStateOf(0) }
        val scope = rememberCoroutineScope()
        
        val symbols = listOf("🍒", "🍋", "🍊", "🍇", "💎", "7️⃣")

        // Заголовок и кредиты
        Text(
            "Однорукий бандит",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        Text(
            "Кредиты: $credits",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Слоты
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            symbols.getOrNull(slot1Value)?.let { symbol ->
                SlotItem(symbol)
            }
            symbols.getOrNull(slot2Value)?.let { symbol ->
                SlotItem(symbol)
            }
            symbols.getOrNull(slot3Value)?.let { symbol ->
                SlotItem(symbol)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Кнопка запуска
        Button(
            onClick = {
                if (credits >= 10 && !isSpinning) {
                    credits -= 10
                    isSpinning = true
                    scope.launch {
                        // Начинаем звук вращения
                        soundEffects.startSpinningSound()
                        
                        // Анимация вращения
                        repeat(20) {
                            delay(50)
                            slot1Value = Random.nextInt(symbols.size)
                            slot2Value = Random.nextInt(symbols.size)
                            slot3Value = Random.nextInt(symbols.size)
                        }
                        
                        // Финальные значения с звуками остановки
                        slot1Value = Random.nextInt(symbols.size)
                        soundEffects.playStopSound()
                        delay(200)
                        slot2Value = Random.nextInt(symbols.size)
                        soundEffects.playStopSound()
                        delay(200)
                        slot3Value = Random.nextInt(symbols.size)
                        soundEffects.playStopSound()
                        
                        // Останавливаем звук вращения
                        soundEffects.stopSpinningSound()
                        
                        // Проверка выигрыша
                        var won = false
                        when {
                            slot1Value == slot2Value && slot2Value == slot3Value -> {
                                // Джекпот
                                won = true
                                credits += when(slot1Value) {
                                    5 -> 500  // 7️⃣
                                    4 -> 300  // 💎
                                    else -> 100
                                }
                            }
                            slot1Value == slot2Value || slot2Value == slot3Value || slot1Value == slot3Value -> {
                                // Две одинаковые
                                won = true
                                credits += 20
                            }
                        }
                        
                        // Проигрываем звук выигрыша если выиграли
                        if (won) {
                            soundEffects.playWinSound()
                        }
                        
                        isSpinning = false
                    }
                }
            },
            enabled = credits >= 10 && !isSpinning,
            modifier = Modifier.size(width = 200.dp, height = 60.dp)
        ) {
            if (isSpinning) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(
                    "КРУТИТЬ (10 кр.)",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Правила
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "Правила игры:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text("• Три одинаковых 7️⃣ = 500 кредитов")
                Text("• Три одинаковых 💎 = 300 кредитов")
                Text("• Три любых одинаковых = 100 кредитов")
                Text("• Две одинаковых = 20 кредитов")
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    "Комбинации:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text("🍒 - Вишня")
                Text("🍋 - Лимон")
                Text("🍊 - Апельсин")
                Text("🍇 - Виноград")
                Text("💎 - Алмаз")
                Text("7️⃣ - Семёрка")
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    "Советы:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text("• Начните с малых ставок")
                Text("• Следите за балансом")
                Text("• Играйте ответственно")
                Text("• Это просто игра для развлечения")
            }
        }
        
        // Дополнительное пространство внизу для удобства прокрутки
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun SlotItem(symbol: String) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = symbol,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
    }
} 