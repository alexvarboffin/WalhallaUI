package com.walhalla.wvcompose1.ui.screens

import android.view.GestureDetector
import android.view.MotionEvent
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.walhalla.wvcompose1.config.LoadingType
import com.walhalla.wvcompose1.config.MenuItem
import com.walhalla.wvcompose1.config.MenuItemType
import com.walhalla.wvcompose1.config.WebViewConfig
import com.walhalla.wvcompose1.game.SlotMachineScreen
import com.walhalla.wvcompose1.ui.components.BookmarksSheet
import com.walhalla.wvcompose1.ui.components.ReaderModeControls
import com.walhalla.wvcompose1.ui.components.SearchBar
import com.walhalla.wvcompose1.ui.components.enableReaderMode

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
    
    // Новые состояния
    var showSearch by remember { mutableStateOf(false) }
    var showReaderControls by remember { mutableStateOf(false) }
    var showBookmarks by remember { mutableStateOf(false) }
    var isReaderModeEnabled by remember { mutableStateOf(false) }
    var textZoomPercent by remember { mutableStateOf(100) }
    var isDarkMode by remember { mutableStateOf(false) }
    var bookmarks by remember { mutableStateOf(listOf<MenuItem>()) }
    var showGame by remember { mutableStateOf(false) }
    
    val context = LocalContext.current

    Scaffold(
        topBar = {
            if (config.showToolbar) {
                Column {
                    TopAppBar(
                        title = { Text(if (showGame) "Игра" else "") },
                        navigationIcon = {
                            if (!showGame && config.webViewSettings.enableNavigation) {
                                IconButton(
                                    onClick = { webView?.goBack() },
                                    enabled = canGoBack
                                ) {
                                    Icon(Icons.Default.ArrowBack, "Назад")
                                }
                            }
                        },
                        actions = {
                            if (!showGame) {  // Показываем действия только для WebView
                                if (config.webViewSettings.enableNavigation) {
                                    IconButton(
                                        onClick = { webView?.goForward() },
                                        enabled = canGoForward
                                    ) {
                                        Icon(Icons.Default.ArrowForward, "Вперёд")
                                    }
                                }
                                
                                // Поиск
                                IconButton(onClick = { showSearch = true }) {
                                    Icon(Icons.Default.Search, "Поиск")
                                }
                                
                                // Режим чтения
                                if (config.webViewSettings.enableReaderMode) {
                                    IconButton(
                                        onClick = { 
                                            isReaderModeEnabled = !isReaderModeEnabled
                                            showReaderControls = isReaderModeEnabled
                                            if (isReaderModeEnabled) {
                                                webView?.enableReaderMode()
                                            } else {
                                                webView?.reload()
                                            }
                                        }
                                    ) {
                                        Icon(
                                            if (isReaderModeEnabled) Icons.Filled.MenuBook else Icons.Outlined.MenuBook,
                                            "Режим чтения"
                                        )
                                    }
                                }
                                
                                // Закладки
                                IconButton(onClick = { showBookmarks = true }) {
                                    Icon(Icons.Default.Bookmarks, "Закладки")
                                }
                                
                                IconButton(onClick = { 
                                    webView?.reload()
                                    isLoading = true 
                                }) {
                                    Icon(Icons.Default.Refresh, "Обновить")
                                }
                            }
                        }
                    )
                    if (!showGame && config.webViewSettings.showLoadingProgress && isLoading) {
                        LinearProgressIndicator(
                            progress = { webViewProgress / 100f },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    
                    // Поиск (только для WebView)
                    if (!showGame && showSearch) {
                        SearchBar(
                            webView = webView,
                            onClose = { showSearch = false }
                        )
                    }
                    
                    // Контролы режима чтения (только для WebView)
                    if (!showGame && showReaderControls) {
                        ReaderModeControls(
                            webView = webView,
                            textZoomPercent = textZoomPercent,
                            onTextZoomChange = { 
                                textZoomPercent = it
                                webView?.settings?.textZoom = it
                            },
                            isDarkMode = isDarkMode,
                            onDarkModeChange = { 
                                isDarkMode = it
                                webView?.evaluateJavascript(
                                    if (it) {
                                        "document.body.style.backgroundColor = '#121212'; document.body.style.color = '#ffffff';"
                                    } else {
                                        "document.body.style.backgroundColor = '#ffffff'; document.body.style.color = '#000000';"
                                    },
                                    null
                                )
                            },
                            onClose = { showReaderControls = false }
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
                                when (item.type) {
                                    MenuItemType.WEB -> {
                                        currentUrl = item.url ?: return@NavigationBarItem
                                        isLoading = true
                                        hasError = false
                                        showGame = false
                                    }
                                    MenuItemType.GAME -> {
                                        showGame = true
                                    }
                                }
                            },
                            icon = { 
                                when (item.type) {
                                    MenuItemType.WEB -> Text((index + 1).toString())
                                    MenuItemType.GAME -> Icon(Icons.Default.Casino, "Игра")
                                }
                            },
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
            if (showGame) {
                SlotMachineScreen()
            } else if (!hasError) {
                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            settings.apply {
                                javaScriptEnabled = config.webViewSettings.enableJavaScript
                                allowFileAccess = config.webViewSettings.allowFileAccess
                                config.webViewSettings.userAgent?.let {
                                    userAgentString = it
                                }
                                
                                // Настройки масштабирования
                                if (config.webViewSettings.enableZoom) {
                                    builtInZoomControls = true
                                    displayZoomControls = false
                                }
                                
                                textZoom = textZoomPercent
                            }
                            
                            // Настройка жестов
                            if (config.gestureSettings.enableSwipeNavigation) {
                                val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                                    override fun onFling(
                                        e1: MotionEvent?,
                                        e2: MotionEvent,
                                        velocityX: Float,
                                        velocityY: Float
                                    ): Boolean {
                                        if (e1 == null) return false
                                        
                                        val sensitivity = config.gestureSettings.swipeSensitivity
                                        val diffX = e2.x - e1.x
                                        
                                        if (Math.abs(diffX) > 100 * sensitivity) {
                                            if (diffX > 0 && canGoBack) {
                                                goBack()
                                                return true
                                            } else if (diffX < 0 && canGoForward) {
                                                goForward()
                                                return true
                                            }
                                        }
                                        return false
                                    }
                                })
                                
                                setOnTouchListener { v, event ->
                                    gestureDetector.onTouchEvent(event)
                                    false
                                }
                            }
                            
                            webViewClient = object : WebViewClient() {
                                override fun onPageFinished(view: WebView?, url: String?) {
                                    super.onPageFinished(view, url)
                                    isLoading = false
                                    canGoBack = view?.canGoBack() ?: false
                                    canGoForward = view?.canGoForward() ?: false
                                    
                                    if (isReaderModeEnabled) {
                                        enableReaderMode()
                                    }
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
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
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
    
    // Показ закладок
    if (showBookmarks) {
        BookmarksSheet(
            bookmarks = bookmarks,
            onBookmarkClick = { bookmark ->
                currentUrl = bookmark.url
                isLoading = true
                showBookmarks = false
            },
            onBookmarkDelete = { bookmark ->
                bookmarks = bookmarks.filter { it != bookmark }
            },
            onDismiss = { showBookmarks = false }
        )
    }
} 