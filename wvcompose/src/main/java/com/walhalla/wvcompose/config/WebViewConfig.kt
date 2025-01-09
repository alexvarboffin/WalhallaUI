package com.walhalla.wvcompose.config

data class WebViewConfig(
    val showToolbar: Boolean = true,
    val showBottomMenu: Boolean = true,
    val loadingType: LoadingType = LoadingType.PROGRESS,
    val menuItems: List<MenuItem> = listOf(
        MenuItem("Пункт 1", "https://example.com/1"),
        MenuItem("Пункт 2", "https://example.com/2"),
        MenuItem("Пункт 3", "https://example.com/3")
    ),
    val webViewSettings: WebViewSettings = WebViewSettings()
)

data class MenuItem(
    val title: String,
    val url: String
)

data class WebViewSettings(
    val enableJavaScript: Boolean = true,
    val enableNavigation: Boolean = true,
    val showLoadingProgress: Boolean = true,
    val userAgent: String? = null,
    val allowFileAccess: Boolean = false
)

enum class LoadingType {
    PROGRESS,
    SPLASH_SCREEN
} 