package com.walhalla.wvcompose7.slots

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.walhalla.wvcompose7.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun BaseSlot(
    title: String,
    symbols: List<String>,
    primaryColor: Color,
    secondaryColor: Color,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var balance by remember { mutableStateOf(1000) }
    var bet by remember { mutableStateOf(10) }
    var isSpinning by remember { mutableStateOf(false) }
    var winAmount by remember { mutableStateOf(0) }
    var shouldSpin by remember { mutableStateOf(false) }
    var multiplier by remember { mutableStateOf(1) }
    var streak by remember { mutableStateOf(0) }
    
    // Состояния для анимации
    val reels = List(3) { remember { mutableStateOf(0) } }
    val spinAnimations = reels.map { remember { Animatable(0f) } }
    val spinningSymbols = List(3) { remember { mutableStateOf(0) } }
    val scaleAnimations = reels.map { remember { Animatable(1f) } }
    
    // Звуковые эффекты
    val spinSound = remember { MediaPlayer.create(context, R.raw.spin_sound) }
    val winSound = remember { MediaPlayer.create(context, R.raw.win_sound) }
    
    val gradient = Brush.verticalGradient(
        colors = listOf(primaryColor, secondaryColor)
    )
    
    DisposableEffect(Unit) {
        onDispose {
            spinSound.release()
            winSound.release()
        }
    }
    
    // Обработка анимации в корутине
    LaunchedEffect(shouldSpin) {
        if (shouldSpin) {
            spinSound.start()
            
            // Запускаем быструю анимацию символов с разной скоростью для каждого барабана
            launch {
                repeat(40) { iteration ->
                    spinningSymbols.forEachIndexed { index, symbol ->
                        if (iteration < 30 + index * 5) {
                            delay(50L + index * 20)
                            symbol.value = Random.nextInt(symbols.size)
                        }
                    }
                }
            }
            
            // Запускаем анимацию вращения для каждого барабана
            spinAnimations.forEachIndexed { index, animation ->
                launch {
                    animation.animateTo(
                        targetValue = animation.value + 360f * (5 + index),
                        animationSpec = tween(
                            durationMillis = 2000 + index * 500,
                            easing = FastOutSlowInEasing
                        )
                    )
                }
            }
            
            // Анимация масштабирования для эффекта "дрожания"
            launch {
                repeat(20) {
                    scaleAnimations.forEach { scale ->
                        scale.animateTo(0.95f, tween(100))
                        scale.animateTo(1.05f, tween(100))
                    }
                }
                scaleAnimations.forEach { it.animateTo(1f) }
            }
            
            // Ждем окончания анимации
            delay(3500)
            
            // Определяем результат
            reels.forEach { reel ->
                reel.value = Random.nextInt(symbols.size)
            }
            
            // Копируем финальные символы
            reels.forEachIndexed { index, reel ->
                spinningSymbols[index].value = reel.value
            }
            
            // Проверяем выигрыш
            if (reels.all { it.value == reels[0].value }) {
                streak++
                multiplier = 1 + (streak / 2)
                winAmount = bet * 10 * multiplier
                balance += winAmount
                
                // Анимация выигрышной комбинации
                launch {
                    repeat(3) {
                        scaleAnimations.forEach { scale ->
                            scale.animateTo(1.2f, tween(200))
                            scale.animateTo(1f, tween(200))
                        }
                    }
                }
                
                winSound.start()
            } else {
                winAmount = 0
                streak = 0
                multiplier = 1
            }
            
            isSpinning = false
            shouldSpin = false
        }
    }
    
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
                text = title,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.shadow(4.dp)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Статистика
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "Баланс: $balance",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (streak > 0) {
                        Text(
                            text = "Серия: $streak",
                            fontSize = 14.sp,
                            color = Color(0xFFFFD700)
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Ставка: $bet",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (multiplier > 1) {
                        Text(
                            text = "Множитель: ${multiplier}x",
                            fontSize = 14.sp,
                            color = Color(0xFFFFD700)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Слот-машина
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .shadow(8.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(Color(0xFF1A1A1A))
                    .border(2.dp, Color(0xFF2A2A2A), MaterialTheme.shapes.medium)
                    .padding(12.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    spinAnimations.forEachIndexed { index, animation ->
                        ReelView(
                            symbols = symbols,
                            currentIndex = spinningSymbols[index].value,
                            rotationY = animation.value,
                            scale = scaleAnimations[index].value,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Выигрыш
            AnimatedVisibility(
                visible = winAmount > 0,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Text(
                    text = "Выигрыш: $winAmount",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700),
                    modifier = Modifier.shadow(4.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Кнопки управления
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { if (bet > 10) bet -= 10 },
                    enabled = !isSpinning,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4A4A4A)
                    ),
                    modifier = Modifier.shadow(4.dp)
                ) {
                    Text("-10")
                }
                
                Button(
                    onClick = {
                        if (balance >= bet && !isSpinning) {
                            isSpinning = true
                            balance -= bet
                            shouldSpin = true
                        }
                    },
                    enabled = !isSpinning && balance >= bet,
                    modifier = Modifier
                        .size(width = 120.dp, height = 48.dp)
                        .shadow(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFD700)
                    )
                ) {
                    Text(
                        if (isSpinning) "Крутится..." else "Крутить",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Button(
                    onClick = { if (balance >= 10) bet += 10 },
                    enabled = !isSpinning,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4A4A4A)
                    ),
                    modifier = Modifier.shadow(4.dp)
                ) {
                    Text("+10")
                }
            }
        }
    }
}

@Composable
private fun ReelView(
    symbols: List<String>,
    currentIndex: Int,
    rotationY: Float,
    scale: Float,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(4.dp)
            .height(180.dp)
            .shadow(4.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(Color(0xFF2A2A2A))
            .border(1.dp, Color(0xFF3A3A3A), MaterialTheme.shapes.medium)
            .graphicsLayer {
                this.rotationY = rotationY
                this.scaleX = scale
                this.scaleY = scale
            }
    ) {
        Text(
            text = symbols[currentIndex],
            fontSize = 48.sp,
            modifier = Modifier.graphicsLayer {
                this.rotationY = -rotationY
            }
        )
    }
} 