package com.walhalla.wvcompose2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.walhalla.wvcompose2.config.WebViewConfig
import com.walhalla.wvcompose2.config.MenuItem
import com.walhalla.wvcompose2.ui.screens.MainScreen
import com.walhalla.wvcompose2.ui.theme.WVComposeTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val config = WebViewConfig(
            showToolbar = true,
            showBottomNav = true,
            menuItems = listOf(
                MenuItem("Google", "https://www.google.com"),
                MenuItem("Yandex", "https://www.yandex.ru"),
                MenuItem("Bing", "https://www.bing.com")
            )
        )

        setContent {
            WVComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(config)
                }
            }
        }
    }
} 