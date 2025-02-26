package com.walhalla.wvcompose7.game.screens

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
import com.walhalla.wvcompose7.game.viewmodels.RouletteViewModel
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RouletteScreen(
    viewModel: RouletteViewModel,
    modifier: Modifier = Modifier
) {
    var isSpinning by remember { mutableStateOf(false) }
    var targetRotation by remember { mutableStateOf(0f) }
    
    val rotation by animateFloatAsState(
        targetValue = targetRotation,
        animationSpec = tween(
            durationMillis = if (isSpinning) 5000 else 0,
            easing = FastOutSlowInEasing
        ),
        finishedListener = {
            isSpinning = false
            viewModel.onSpinComplete(calculateResult(it))
        }
    )
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Рулетка
        Box(
            modifier = Modifier
                .size(300.dp)
                .padding(16.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2, size.height / 2)
                val radius = size.width / 2
                
                // Рисуем сектора рулетки
                rotate(rotation) {
                    val sectorAngle = 360f / ROULETTE_NUMBERS.size
                    ROULETTE_NUMBERS.forEachIndexed { index, number ->
                        val startAngle = index * sectorAngle
                        var color = when {
                            number == 0 -> Color.Green
                            index % 2 == 0 -> Color.Red
                            else -> Color.Black
                        }
                        
                        drawArc(
                            color = color,
                            startAngle = startAngle,
                            sweepAngle = sectorAngle,
                            useCenter = true,
                            size = Size(radius * 2, radius * 2),
                            topLeft = Offset(0f, 0f)
                        )
                        
                        // Рисуем числа
                        val textAngle = (startAngle + sectorAngle / 2) * PI / 180
                        val textRadius = radius * 0.7f
                        val x = center.x + cos(textAngle).toFloat() * textRadius
                        val y = center.y + sin(textAngle).toFloat() * textRadius
                        
                        drawContext.canvas.nativeCanvas.drawText(
                            number.toString(),
                            x,
                            y,
                            android.graphics.Paint().apply {
                                color = Color.White
                                textSize = 24f
                                textAlign = android.graphics.Paint.Align.CENTER
                            }
                        )
                    }
                }
                
                // Рисуем указатель
                drawCircle(
                    color = Color.White,
                    radius = 10f,
                    center = Offset(center.x, center.y - radius)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Кнопка запуска
        Button(
            onClick = {
                if (!isSpinning) {
                    isSpinning = true
                    targetRotation += 360f * 10 + (0..360).random().toFloat()
                }
            },
            enabled = !isSpinning
        ) {
            Text(if (isSpinning) "Крутится..." else "Крутить")
        }
    }
}

private fun calculateResult(rotation: Float): Int {
    val normalizedRotation = (360 - (rotation % 360)) % 360
    val sectorAngle = 360f / ROULETTE_NUMBERS.size
    val sector = (normalizedRotation / sectorAngle).toInt()
    return ROULETTE_NUMBERS[sector]
}

private val ROULETTE_NUMBERS = listOf(
    0, 32, 15, 19, 4, 21, 2, 25, 17, 34, 6, 27, 13, 36, 11, 30, 8, 23, 10,
    5, 24, 16, 33, 1, 20, 14, 31, 9, 22, 18, 29, 7, 28, 12, 35, 3, 26
) 