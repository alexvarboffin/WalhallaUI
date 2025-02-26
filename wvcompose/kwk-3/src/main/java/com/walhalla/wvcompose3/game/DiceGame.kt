package com.walhalla.wvcompose3.game

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun DiceGameScreen() {
    var credits by remember { mutableStateOf(1000) }
    var bet by remember { mutableStateOf(10) }
    var isRolling by remember { mutableStateOf(false) }
    var dice1Value by remember { mutableStateOf(1) }
    var dice2Value by remember { mutableStateOf(1) }
    var rotationAngle1 by remember { mutableStateOf(0f) }
    var rotationAngle2 by remember { mutableStateOf(0f) }
    
    val soundEffects = rememberGameSoundEffects()
    
    // Анимация вращения костей
    val rotation1 by animateFloatAsState(
        targetValue = if (isRolling) rotationAngle1 + 360f * 5 else rotationAngle1,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        finishedListener = {
            if (isRolling) {
                dice1Value = Random.nextInt(1, 7)
                soundEffects.playStopSound()
            }
        }
    )
    
    val rotation2 by animateFloatAsState(
        targetValue = if (isRolling) rotationAngle2 + 360f * 5 else rotationAngle2,
        animationSpec = tween(
            durationMillis = 1200,
            easing = FastOutSlowInEasing
        ),
        finishedListener = {
            if (isRolling) {
                dice2Value = Random.nextInt(1, 7)
                isRolling = false
                val total = dice1Value + dice2Value
                val win = when {
                    total == 7 || total == 11 -> bet * 4
                    total == 2 || total == 12 -> bet * 6
                    total % 2 == 0 -> bet * 2
                    else -> 0
                }
                credits += win
                if (win > 0) {
                    soundEffects.playWinSound()
                }
            }
        }
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
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Первая кость
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    rotate(rotation1) {
                        // Рисуем кость
                        drawRect(
                            color = Color.White,
                            topLeft = Offset.Zero,
                            size = size
                        )
                        
                        // Рисуем точки на кости
                        val dotRadius = size.minDimension / 10
                        when (dice1Value) {
                            1 -> {
                                drawCircle(
                                    color = Color.Black,
                                    radius = dotRadius,
                                    center = Offset(size.width / 2, size.height / 2)
                                )
                            }
                            2 -> {
                                drawCircle(
                                    color = Color.Black,
                                    radius = dotRadius,
                                    center = Offset(size.width * 0.25f, size.height * 0.25f)
                                )
                                drawCircle(
                                    color = Color.Black,
                                    radius = dotRadius,
                                    center = Offset(size.width * 0.75f, size.height * 0.75f)
                                )
                            }
                            // Добавить остальные варианты точек для значений 3-6
                        }
                    }
                }
            }
            
            // Вторая кость
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    rotate(rotation2) {
                        drawRect(
                            color = Color.White,
                            topLeft = Offset.Zero,
                            size = size
                        )
                        
                        val dotRadius = size.minDimension / 10
                        when (dice2Value) {
                            1 -> {
                                drawCircle(
                                    color = Color.Black,
                                    radius = dotRadius,
                                    center = Offset(size.width / 2, size.height / 2)
                                )
                            }
                            2 -> {
                                drawCircle(
                                    color = Color.Black,
                                    radius = dotRadius,
                                    center = Offset(size.width * 0.25f, size.height * 0.25f)
                                )
                                drawCircle(
                                    color = Color.Black,
                                    radius = dotRadius,
                                    center = Offset(size.width * 0.75f, size.height * 0.75f)
                                )
                            }
                            // Добавить остальные варианты точек для значений 3-6
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "Сумма: ${dice1Value + dice2Value}",
            style = MaterialTheme.typography.titleLarge
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { if (bet > 10) bet -= 10 },
                enabled = !isRolling
            ) {
                Text("-10")
            }
            
            Text(
                text = "Ставка: $bet",
                style = MaterialTheme.typography.titleLarge
            )
            
            Button(
                onClick = { if (credits >= bet + 10) bet += 10 },
                enabled = !isRolling
            ) {
                Text("+10")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = {
                if (credits >= bet) {
                    credits -= bet
                    isRolling = true
                    soundEffects.startSpinningSound()
                    rotationAngle1 = rotation1
                    rotationAngle2 = rotation2
                }
            },
            enabled = !isRolling && credits >= bet,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("БРОСИТЬ КОСТИ!")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = """
                Выигрыши:
                7 или 11 - ставка x4
                2 или 12 - ставка x6
                Чётная сумма - ставка x2
            """.trimIndent(),
            style = MaterialTheme.typography.bodyMedium
        )
    }
} 