package com.walhalla.wvcompose4.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun RulesScreen() {
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(
            text = "Правила игры",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Основные правила:",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. Выберите размер ставки")
                Text("2. Нажмите кнопку КРУТИТЬ")
                Text("3. Дождитесь остановки барабанов")
                Text("4. Получите выигрыш при совпадении символов")
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Выигрышные комбинации:",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("• Три одинаковых символа - ставка x10")
                Text("• Два или более 7️⃣ - ставка x5")
                Text("• Два или более 💎 - ставка x3")
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Символы:",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("🍒 - Вишня")
                Text("🍊 - Апельсин")
                Text("🍋 - Лимон")
                Text("🍇 - Виноград")
                Text("💎 - Алмаз")
                Text("7️⃣ - Семёрка")
            }
        }
    }
} 