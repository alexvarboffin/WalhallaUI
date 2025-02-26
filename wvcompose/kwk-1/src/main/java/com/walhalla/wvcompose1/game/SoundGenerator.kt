package com.walhalla.wvcompose1.game

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import kotlin.math.sin

class SoundGenerator {
    companion object {
        private const val SAMPLE_RATE = 44100
        private const val SPINNING_DURATION_MS = 1000 // 1 секунда для зацикленного звука
        private const val STOP_DURATION_MS = 100 // 0.1 секунда для звука остановки
        private const val WIN_DURATION_MS = 2000 // 2 секунды для звука победы

        fun generateSpinningSound(context: Context) {
            val numSamples = (SAMPLE_RATE * SPINNING_DURATION_MS / 1000)
            val samples = FloatArray(numSamples)
            
            // Создаем механический звук вращения (смесь частот)
            for (i in 0 until numSamples) {
                val t = i.toFloat() / SAMPLE_RATE
                samples[i] = (
                    sin(2.0 * Math.PI * 440.0 * t).toFloat() * 0.3f + // Основной тон
                    sin(2.0 * Math.PI * 880.0 * t).toFloat() * 0.2f + // Октава выше
                    sin(2.0 * Math.PI * 220.0 * t).toFloat() * 0.2f + // Октава ниже
                    (Math.random() * 0.1 - 0.05).toFloat() // Немного шума
                ).coerceIn(-1f, 1f)
            }
            
            writeWavFile(context, "slot_spinning.wav", samples)
        }

        fun generateStopSound(context: Context) {
            val numSamples = (SAMPLE_RATE * STOP_DURATION_MS / 1000)
            val samples = FloatArray(numSamples)
            
            // Создаем короткий щелчок
            for (i in 0 until numSamples) {
                val t = i.toFloat() / SAMPLE_RATE
                val envelope = 1.0f - (i.toFloat() / numSamples) // Затухание
                samples[i] = (
                    sin(2.0 * Math.PI * 2000.0 * t).toFloat() * envelope * 0.8f +
                    sin(2.0 * Math.PI * 1000.0 * t).toFloat() * envelope * 0.2f
                ).coerceIn(-1f, 1f)
            }
            
            writeWavFile(context, "slot_stop.wav", samples)
        }

        fun generateWinSound(context: Context) {
            val numSamples = (SAMPLE_RATE * WIN_DURATION_MS / 1000)
            val samples = FloatArray(numSamples)
            
            // Создаем победную мелодию
            val frequencies = listOf(523.25, 659.25, 783.99, 1046.50) // До-Ми-Соль-До
            val noteDuration = WIN_DURATION_MS / frequencies.size
            
            for (i in 0 until numSamples) {
                val t = i.toFloat() / SAMPLE_RATE
                val noteIndex = (i * frequencies.size / numSamples).coerceIn(0, frequencies.size - 1)
                val freq = frequencies[noteIndex]
                val envelope = 1.0f - (i % (noteDuration * SAMPLE_RATE / 1000)).toFloat() / (noteDuration * SAMPLE_RATE / 1000)
                samples[i] = (sin(2.0 * Math.PI * freq * t).toFloat() * envelope).coerceIn(-1f, 1f)
            }
            
            writeWavFile(context, "slot_win.wav", samples)
        }

        private fun writeWavFile(context: Context, filename: String, samples: FloatArray) {
            val file = File(context.filesDir, filename)
            FileOutputStream(file).use { fos ->
                // WAV header
                fos.write("RIFF".toByteArray())
                val dataSize = samples.size * 2 // 16-bit samples
                fos.write(intToByteArray(36 + dataSize)) // File size
                fos.write("WAVE".toByteArray())
                fos.write("fmt ".toByteArray())
                fos.write(intToByteArray(16)) // Subchunk1Size
                fos.write(shortToByteArray(1)) // AudioFormat (PCM)
                fos.write(shortToByteArray(1)) // NumChannels (Mono)
                fos.write(intToByteArray(SAMPLE_RATE)) // SampleRate
                fos.write(intToByteArray(SAMPLE_RATE * 2)) // ByteRate
                fos.write(shortToByteArray(2)) // BlockAlign
                fos.write(shortToByteArray(16)) // BitsPerSample
                fos.write("data".toByteArray())
                fos.write(intToByteArray(dataSize))

                // Convert and write samples
                samples.forEach { sample ->
                    val intSample = (sample * 32767).toInt().coerceIn(-32768, 32767)
                    fos.write(shortToByteArray(intSample.toShort()))
                }
            }
        }

        private fun intToByteArray(value: Int): ByteArray {
            return byteArrayOf(
                value.toByte(),
                (value shr 8).toByte(),
                (value shr 16).toByte(),
                (value shr 24).toByte()
            )
        }

        private fun shortToByteArray(value: Short): ByteArray {
            return byteArrayOf(
                value.toByte(),
                (value.toInt() shr 8).toByte()
            )
        }
    }
} 