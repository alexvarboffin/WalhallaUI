package com.walhalla.wvcompose1.ui.components

import android.webkit.WebView
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ReaderModeControls(
    webView: WebView?,
    textZoomPercent: Int,
    onTextZoomChange: (Int) -> Unit,
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onClose: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 3.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Режим чтения", style = MaterialTheme.typography.titleMedium)
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, "Закрыть")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Размер текста:", modifier = Modifier.weight(1f))
                IconButton(
                    onClick = { onTextZoomChange((textZoomPercent - 10).coerceIn(50, 200)) }
                ) {
                    Icon(Icons.Default.Remove, "Уменьшить")
                }
                Text("$textZoomPercent%")
                IconButton(
                    onClick = { onTextZoomChange((textZoomPercent + 10).coerceIn(50, 200)) }
                ) {
                    Icon(Icons.Default.Add, "Увеличить")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Тёмная тема", modifier = Modifier.weight(1f))
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = onDarkModeChange
                )
            }
        }
    }
}

fun WebView.enableReaderMode() {
    settings.apply {
        // Удаляем ненужные элементы и стили
        loadWithOverviewMode = true
        useWideViewPort = true
        
        // Инжектируем CSS для режима чтения
        evaluateJavascript("""
            javascript:(function() {
                var style = document.createElement('style');
                style.textContent = `
                    body {
                        max-width: 800px !important;
                        margin: 0 auto !important;
                        padding: 16px !important;
                        font-family: system-ui, -apple-system, sans-serif !important;
                        line-height: 1.6 !important;
                    }
                    
                    img {
                        max-width: 100% !important;
                        height: auto !important;
                    }
                    
                    /* Удаляем ненужные элементы */
                    header, footer, nav, aside, .ad, .advertisement, .social-share {
                        display: none !important;
                    }
                `;
                document.head.appendChild(style);
            })()
        """.trimIndent(), null)
    }
} 