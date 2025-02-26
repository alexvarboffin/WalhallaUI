package com.walhalla.wvcompose2.game

import android.content.Context
import android.media.MediaPlayer
import android.media.AudioAttributes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.io.File

class GameSoundEffects(private val context: Context) {
    private var spinningSound: MediaPlayer? = null
    private var winSound: MediaPlayer? = null
    private var stopSound: MediaPlayer? = null

    init {
        // Генерируем звуки при первом создании
        SoundGenerator.generateWheelSpinningSound(context)
        SoundGenerator.generateWheelStopSound(context)
        SoundGenerator.generateWheelWinSound(context)

        // Настраиваем атрибуты аудио для игровых эффектов
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        // Инициализируем звук вращения колеса
        spinningSound = MediaPlayer().apply {
            setDataSource(File(context.filesDir, "wheel_spinning.wav").absolutePath)
            setAudioAttributes(audioAttributes)
            isLooping = false
            setVolume(1.0f, 1.0f)
            prepare()
        }

        // Инициализируем звук выигрыша
        winSound = MediaPlayer().apply {
            setDataSource(File(context.filesDir, "wheel_win.wav").absolutePath)
            setAudioAttributes(audioAttributes)
            isLooping = false
            setVolume(1.0f, 1.0f)
            prepare()
        }

        // Инициализируем звук остановки колеса
        stopSound = MediaPlayer().apply {
            setDataSource(File(context.filesDir, "wheel_stop.wav").absolutePath)
            setAudioAttributes(audioAttributes)
            isLooping = false
            setVolume(1.0f, 1.0f)
            prepare()
        }
    }

    fun startSpinningSound() {
        spinningSound?.apply {
            if (!isPlaying) {
                seekTo(0)
                start()
            }
        }
    }

    fun stopSpinningSound() {
        spinningSound?.apply {
            if (isPlaying) {
                pause()
                seekTo(0)
            }
        }
    }

    fun playStopSound() {
        stopSound?.apply {
            seekTo(0)
            start()
        }
    }

    fun playWinSound() {
        winSound?.apply {
            seekTo(0)
            start()
        }
    }

    fun release() {
        spinningSound?.release()
        winSound?.release()
        stopSound?.release()
        spinningSound = null
        winSound = null
        stopSound = null
    }
}

@Composable
fun rememberGameSoundEffects(): GameSoundEffects {
    val context = LocalContext.current
    val soundEffects = remember { GameSoundEffects(context) }

    DisposableEffect(soundEffects) {
        onDispose {
            soundEffects.release()
        }
    }

    return soundEffects
} 