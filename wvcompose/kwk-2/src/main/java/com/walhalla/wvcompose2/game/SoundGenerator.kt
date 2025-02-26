package com.walhalla.wvcompose2.game

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import kotlin.math.sin

class SoundGenerator {
    companion object {
        private const val SAMPLE_RATE = 44100
        private const val SPINNING_DURATION_MS = 1000 // 1 секунда для зацикленного звука
        private const val STOP_DURATION_MS = 200 // 0.2 секунды для звука остановки
        private const val WIN_DURATION_MS = 3000 // 3 секунды для звука победы

        fun generateWheelSpinningSound(context: Context) {
            val numSamples = (SAMPLE_RATE * SPINNING_DURATION_MS / 1000)
            val samples = FloatArray(numSamples)
            
            // Создаем звук вращения колеса (щелчки с затуханием)
            for (i in 0 until numSamples) {
                val t = i.toFloat() / SAMPLE_RATE
                val clickFreq = 10 // Частота щелчков
                val envelope = 1.0f - (i % (SAMPLE_RATE / clickFreq)).toFloat() / (SAMPLE_RATE / clickFreq)
                samples[i] = (
                    sin(2.0 * Math.PI * 1000.0 * t).toFloat() * envelope * 0.5f +
                    sin(2.0 * Math.PI * 2000.0 * t).toFloat() * envelope * 0.3f +
                    (Math.random() * 0.2 - 0.1).toFloat()
                ).coerceIn(-1f, 1f)
            }
            
            writeWavFile(context, "wheel_spinning.wav", samples)
        }

        fun generateWheelStopSound(context: Context) {
            val numSamples = (SAMPLE_RATE * STOP_DURATION_MS / 1000)
            val samples = FloatArray(numSamples)
            
            // Создаем звук остановки колеса (глухой удар)
            for (i in 0 until numSamples) {
                val t = i.toFloat() / SAMPLE_RATE
                val envelope = 1.0f - (i.toFloat() / numSamples)
                samples[i] = (
                    sin(2.0 * Math.PI * 200.0 * t).toFloat() * envelope * 0.8f +
                    sin(2.0 * Math.PI * 400.0 * t).toFloat() * envelope * 0.2f
                ).coerceIn(-1f, 1f)
            }
            
            writeWavFile(context, "wheel_stop.wav", samples)
        }

        fun generateWheelWinSound(context: Context) {
            val numSamples = (SAMPLE_RATE * WIN_DURATION_MS / 1000)
            val samples = FloatArray(numSamples)
            
            // Создаем фанфары победы
            val frequencies = listOf(
                523.25, // До
                659.25, // Ми
                783.99, // Соль
                1046.50, // До
                1318.51, // Ми
                1567.98 // Соль
            )
            
            for (i in 0 until numSamples) {
                val t = i.toFloat() / SAMPLE_RATE
                val section = (i * frequencies.size / numSamples).coerceIn(0, frequencies.size - 1)
                val freq = frequencies[section]
                val envelope = when {
                    i < numSamples * 0.1 -> i.toFloat() / (numSamples * 0.1f) // Нарастание
                    i > numSamples * 0.8 -> 1.0f - (i - numSamples * 0.8f) / (numSamples * 0.2f) // Затухание
                    else -> 1.0f
                }
                samples[i] = (
                    sin(2.0 * Math.PI * freq * t).toFloat() * envelope * 0.5f +
                    sin(2.0 * Math.PI * freq * 2 * t).toFloat() * envelope * 0.3f +
                    sin(2.0 * Math.PI * freq * 3 * t).toFloat() * envelope * 0.2f
                ).coerceIn(-1f, 1f)
            }
            
            writeWavFile(context, "wheel_win.wav", samples)
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