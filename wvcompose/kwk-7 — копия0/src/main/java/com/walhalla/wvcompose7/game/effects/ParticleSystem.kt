package com.walhalla.wvcompose7.game.effects

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.random.Random

data class Particle(
    val id: Int,
    var position: Offset,
    var velocity: Offset,
    var alpha: Float,
    var scale: Float,
    var rotation: Float,
    val color: Color,
    var lifetime: Float
)

@Composable
fun ParticleEffect(
    isActive: Boolean,
    particleColor: Color,
    modifier: Modifier = Modifier
) {
    var particles by remember { mutableStateOf(listOf<Particle>()) }
    val random = remember { Random(System.currentTimeMillis()) }
    
    LaunchedEffect(isActive) {
        if (isActive) {
            // Создаем новые частицы
            val newParticles = (0..50).map { id ->
                Particle(
                    id = id,
                    position = Offset(
                        x = random.nextFloat() * 1000,
                        y = random.nextFloat() * 2000
                    ),
                    velocity = Offset(
                        x = (random.nextFloat() - 0.5f) * 10,
                        y = -random.nextFloat() * 15
                    ),
                    alpha = random.nextFloat() * 0.5f + 0.5f,
                    scale = random.nextFloat() * 0.5f + 0.5f,
                    rotation = random.nextFloat() * 360,
                    color = particleColor,
                    lifetime = random.nextFloat() * 2 + 1
                )
            }
            particles = newParticles
        }
    }
    
    val infiniteTransition = rememberInfiniteTransition()
    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    
    Canvas(modifier = modifier.fillMaxSize()) {
        particles.forEach { particle ->
            drawParticle(particle, animationProgress)
        }
    }
}

private fun DrawScope.drawParticle(particle: Particle, progress: Float) {
    val alpha = (particle.alpha * (1 - progress)).coerceIn(0f, 1f)
    val scale = particle.scale * (1 + progress)
    
    drawCircle(
        color = particle.color.copy(alpha = alpha),
        radius = 10f * scale,
        center = particle.position + particle.velocity * progress * 60F
    )
}

@Composable
fun DemonicEffect(
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    var particles by remember { mutableStateOf(listOf<Particle>()) }
    val random = remember { Random(System.currentTimeMillis()) }
    
    LaunchedEffect(isActive) {
        if (isActive) {
            // Создаем демонические частицы
            val newParticles = (0..100).map { id ->
                Particle(
                    id = id,
                    position = Offset(
                        x = random.nextFloat() * 1000,
                        y = 2000f
                    ),
                    velocity = Offset(
                        x = (random.nextFloat() - 0.5f) * 20,
                        y = -random.nextFloat() * 25
                    ),
                    alpha = random.nextFloat() * 0.7f + 0.3f,
                    scale = random.nextFloat() * 0.8f + 0.2f,
                    rotation = random.nextFloat() * 360,
                    color = when (random.nextInt(3)) {
                        0 -> Color(0xFFFF0000) // Красный
                        1 -> Color(0xFFFF4500) // Оранжево-красный
                        else -> Color(0xFF8B0000) // Темно-красный
                    },
                    lifetime = random.nextFloat() * 3 + 2
                )
            }
            particles = newParticles
        }
    }
    
    val infiniteTransition = rememberInfiniteTransition()
    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    
    Canvas(modifier = modifier.fillMaxSize()) {
        particles.forEach { particle ->
            // Рисуем демонические частицы с дополнительными эффектами
            drawParticle(particle, animationProgress)
            // Добавляем "хвост" частицы
            drawCircle(
                color = particle.color.copy(alpha = particle.alpha * 0.3f),
                radius = 5f * particle.scale,
                center = particle.position + particle.velocity * animationProgress * 30F
            )
        }
    }
} 