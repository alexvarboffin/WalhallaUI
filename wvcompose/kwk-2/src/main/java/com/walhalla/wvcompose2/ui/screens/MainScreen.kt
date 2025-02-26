package com.walhalla.wvcompose2.ui.screens

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.walhalla.wvcompose2.config.WebViewConfig
import com.walhalla.wvcompose2.game.FortunaWheelScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(config: WebViewConfig) {
    var currentUrl by remember { mutableStateOf(config.menuItems.first().url) }
    var isLoading by remember { mutableStateOf(true) }
    var showGame by remember { mutableStateOf(false) }
    var selectedItemIndex by remember { mutableStateOf(0) }
    var canGoBack by remember { mutableStateOf(false) }
    var canGoForward by remember { mutableStateOf(false) }
    var webView by remember { mutableStateOf<WebView?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (showGame) "Колесо Фортуны" else "") },
                navigationIcon = {
                    if (showGame) {
                        IconButton(onClick = { showGame = false }) {
                            Icon(Icons.Default.ArrowBack, "Назад")
                        }
                    }
                },
                actions = {
                    if (!showGame && config.showToolbar) {
                        IconButton(
                            onClick = { webView?.goBack() },
                            enabled = canGoBack
                        ) {
                            Icon(Icons.Default.ArrowBack, "Назад")
                        }
                        
                        IconButton(
                            onClick = { webView?.goForward() },
                            enabled = canGoForward
                        ) {
                            Icon(Icons.Default.ArrowForward, "Вперед")
                        }
                        
                        IconButton(onClick = { webView?.reload() }) {
                            Icon(Icons.Default.Refresh, "Обновить")
                        }
                        
                        IconButton(
                            onClick = { /* Включить/выключить режим чтения */ }
                        ) {
                            Icon(
                                if (config.settings.enableReaderMode) Icons.Filled.MenuBook
                                else Icons.Outlined.MenuBook,
                                "Режим чтения"
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (config.showBottomNav) {
                NavigationBar {
                    config.menuItems.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Language, item.title) },
                            label = { Text(item.title) },
                            selected = selectedItemIndex == index && !showGame,
                            onClick = {
                                if (!showGame) {
                                    selectedItemIndex = index
                                    currentUrl = item.url
                                    isLoading = true
                                }
                            }
                        )
                    }
                    
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Casino, "Игра") },
                        label = { Text("Игра") },
                        selected = showGame,
                        onClick = { showGame = true }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (showGame) {
                FortunaWheelScreen()
            } else {
                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            webViewClient = object : WebViewClient() {
                                override fun onPageFinished(view: WebView?, url: String?) {
                                    super.onPageFinished(view, url)
                                    isLoading = false
                                    canGoBack = view?.canGoBack() ?: false
                                    canGoForward = view?.canGoForward() ?: false
                                }
                            }
                            settings.apply {
                                javaScriptEnabled = config.settings.enableJavaScript
                                builtInZoomControls = config.settings.enableZoom
                                displayZoomControls = false
                                textZoom = config.settings.textZoomPercent
                                userAgentString = config.settings.userAgent ?: userAgentString
                                allowFileAccess = config.settings.allowFileAccess
                                cacheMode = config.settings.cacheMode
                                defaultTextEncodingName = config.settings.textEncoding
                            }
                            loadUrl(currentUrl)
                            webView = this
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                    update = { view ->
                        if (view.url != currentUrl) {
                            view.loadUrl(currentUrl)
                        }
                    }
                )

                if (isLoading && config.settings.showLoadingProgress) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
} 