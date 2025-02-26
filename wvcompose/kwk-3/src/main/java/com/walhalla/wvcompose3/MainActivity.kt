package com.walhalla.wvcompose3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.walhalla.wvcompose3.config.*
import com.walhalla.wvcompose3.ui.screens.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val config = WebViewConfig(
            showToolbar = true,
            showBottomMenu = true,
            loadingType = LoadingType.PROGRESS,
            menuItems = listOf(
                MenuItem("Google", "https://www.google.com"),
                MenuItem("Yandex", "https://www.yandex.ru"),
                MenuItem("Bing", "https://www.bing.com"),
                MenuItem("Игра", "", MenuItemType.GAME)
            ),
            webViewSettings = WebViewSettings(
                enableJavaScript = true,
                enableNavigation = true,
                showLoadingProgress = true,
                allowFileAccess = false,
                userAgent = null
            )
        )

        setContent {
            MaterialTheme {
                MainScreen(config)
            }
        }
    }
}