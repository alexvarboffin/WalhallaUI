package com.walhalla.wvcompose2.game

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun FortunaWheelScreen() {
    var credits by remember { mutableStateOf(1000) }
    var bet by remember { mutableStateOf(10) }
    var isSpinning by remember { mutableStateOf(false) }
    var currentRotation by remember { mutableStateOf(0f) }
    var targetRotation by remember { mutableStateOf(0f) }

    val soundEffects = rememberGameSoundEffects()

    val sectors = remember {
        listOf(
            WheelSector(0, "x2", Color(0xFFE57373)),
            WheelSector(1, "x3", Color(0xFF81C784)),
            WheelSector(2, "x0", Color(0xFF64B5F6)),
            WheelSector(3, "x5", Color(0xFFFFB74D)),
            WheelSector(4, "x1", Color(0xFFBA68C8)),
            WheelSector(5, "x4", Color(0xFF4DB6AC)),
            WheelSector(6, "x0", Color(0xFFFFD54F)),
            WheelSector(7, "x2", Color(0xFF7986CB))
        )
    }

    val infiniteTransition = rememberInfiniteTransition()
    val rotationAnimation by animateFloatAsState(
        targetValue = targetRotation,
        animationSpec = tween(
            durationMillis = 5000,
            easing = FastOutSlowInEasing
        ),
        finishedListener = {
            isSpinning = false
            val sector = sectors[((360 - (it % 360)) / (360f / sectors.size)).toInt() % sectors.size]
            val multiplier = sector.label.substring(1).toIntOrNull() ?: 0
            val win = bet * multiplier
            credits += win
            if (win > 0) {
                soundEffects.playWinSound()
            } else {
                soundEffects.playStopSound()
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

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(250.dp)
                .padding(16.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2, size.height / 2)
                val radius = size.width.coerceAtMost(size.height) / 2

                rotate(rotationAnimation) {
                    val sectorAngle = 360f / sectors.size
                    sectors.forEachIndexed { index, sector ->
                        rotate(index * sectorAngle) {
                            drawArc(
                                color = sector.color,
                                startAngle = 0f,
                                sweepAngle = sectorAngle,
                                useCenter = true,
                                topLeft = Offset(0f, 0f),
                                size = Size(size.width, size.height)
                            )

                            // Отрисовка текста сектора
                            rotate(sectorAngle / 2) {
                                drawContext.canvas.nativeCanvas.apply {
                                    drawText(
                                        sector.label,
                                        center.x + radius * 0.7f,
                                        center.y,
                                        android.graphics.Paint().apply {
                                            color = android.graphics.Color.WHITE
                                            textSize = 30f
                                            textAlign = android.graphics.Paint.Align.CENTER
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                // Отрисовка стрелки
                drawCircle(
                    color = Color.Red,
                    radius = 10f,
                    center = Offset(center.x, center.y - radius)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

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

        Spacer(modifier = Modifier.height(5.dp))

        Button(
            onClick = {
                if (credits >= bet) {
                    credits -= bet
                    isSpinning = true
                    soundEffects.startSpinningSound()
                    val extraSpins = 5
                    targetRotation = currentRotation + (360 * extraSpins) + Random.nextFloat() * 360
                    currentRotation = targetRotation
                }
            },
            enabled = !isSpinning && credits >= bet,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("КРУТИТЬ!")
        }
    }
}

data class WheelSector(
    val id: Int,
    val label: String,
    val color: Color
)