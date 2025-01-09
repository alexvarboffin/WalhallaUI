package com.walhalla.wvcompose.ui.screens

import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.walhalla.wvcompose.config.LoadingType
import com.walhalla.wvcompose.config.WebViewConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(config: WebViewConfig) {
    var isLoading by remember { mutableStateOf(true) }
    var currentUrl by remember { mutableStateOf(config.menuItems.first().url) }
    var selectedItemIndex by remember { mutableStateOf(0) }
    var webViewProgress by remember { mutableStateOf(0) }
    var canGoBack by remember { mutableStateOf(false) }
    var canGoForward by remember { mutableStateOf(false) }
    var webView by remember { mutableStateOf<WebView?>(null) }
    var hasError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            if (config.showToolbar) {
                Column {
                    TopAppBar(
                        title = { Text("WebView") },
                        navigationIcon = {
                            if (config.webViewSettings.enableNavigation) {
                                IconButton(
                                    onClick = { webView?.goBack() },
                                    enabled = canGoBack
                                ) {
                                    Icon(Icons.Default.ArrowBack, "Назад")
                                }
                            }
                        },
                        actions = {
                            if (config.webViewSettings.enableNavigation) {
                                IconButton(
                                    onClick = { webView?.goForward() },
                                    enabled = canGoForward
                                ) {
                                    Icon(Icons.Default.ArrowForward, "Вперёд")
                                }
                            }
                            IconButton(onClick = { 
                                webView?.reload()
                                isLoading = true 
                            }) {
                                Icon(Icons.Default.Refresh, "Обновить")
                            }
                        }
                    )
                    if (config.webViewSettings.showLoadingProgress && isLoading) {
                        LinearProgressIndicator(
                            progress = { webViewProgress / 100f },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        },
        bottomBar = {
            if (config.showBottomMenu) {
                NavigationBar {
                    config.menuItems.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selectedItemIndex == index,
                            onClick = { 
                                selectedItemIndex = index
                                currentUrl = item.url
                                isLoading = true
                                hasError = false
                            },
                            icon = { Text((index + 1).toString()) },
                            label = { Text(item.title) }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (!hasError) {
                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            settings.apply {
                                javaScriptEnabled = config.webViewSettings.enableJavaScript
                                allowFileAccess = config.webViewSettings.allowFileAccess
                                config.webViewSettings.userAgent?.let {
                                    userAgentString = it
                                }
                            }
                            webViewClient = object : WebViewClient() {
                                override fun onPageFinished(view: WebView?, url: String?) {
                                    super.onPageFinished(view, url)
                                    isLoading = false
                                    canGoBack = view?.canGoBack() ?: false
                                    canGoForward = view?.canGoForward() ?: false
                                }

                                override fun onReceivedError(
                                    view: WebView?,
                                    errorCode: Int,
                                    description: String?,
                                    failingUrl: String?
                                ) {
                                    super.onReceivedError(view, errorCode, description, failingUrl)
                                    hasError = true
                                    errorMessage = description ?: "Ошибка загрузки страницы"
                                }
                            }
                            webChromeClient = object : WebChromeClient() {
                                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                    super.onProgressChanged(view, newProgress)
                                    webViewProgress = newProgress
                                }
                            }
                            loadUrl(currentUrl)
                            webView = this
                        }
                    },
                    update = { view ->
                        if (view.url != currentUrl) {
                            view.loadUrl(currentUrl)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                // Экран ошибки
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = "Ошибка",
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(errorMessage)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        hasError = false
                        webView?.reload()
                    }) {
                        Text("Повторить")
                    }
                }
            }

            if (isLoading && !hasError) {
                when (config.loadingType) {
                    LoadingType.PROGRESS -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(androidx.compose.ui.Alignment.Center)
                        )
                    }
                    LoadingType.SPLASH_SCREEN -> {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            Text(
                                text = "Loading...",
                                modifier = Modifier.align(androidx.compose.ui.Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }
} 