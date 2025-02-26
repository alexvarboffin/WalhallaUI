package com.walhalla.wvcompose2.config

data class WebViewConfig(
    val showToolbar: Boolean = true,
    val showBottomNav: Boolean = true,
    val loadingType: LoadingType = LoadingType.PROGRESS,
    val settings: WebViewSettings = WebViewSettings(),
    val theme: WebViewTheme = WebViewTheme.SYSTEM,
    val menuItems: List<MenuItem> = defaultMenuItems,
    val gestureSettings: GestureSettings = GestureSettings()
)

enum class LoadingType {
    PROGRESS,
    SPLASH
}

enum class WebViewTheme {
    LIGHT,
    DARK,
    SYSTEM
}

data class WebViewSettings(
    val enableJavaScript: Boolean = true,
    val enableZoom: Boolean = true,
    val textZoomPercent: Int = 100,
    val enableReaderMode: Boolean = false,
    val forceDarkMode: Boolean = false,
    val userAgent: String? = null,
    val allowFileAccess: Boolean = false,
    val enableNavigation: Boolean = true,
    val showLoadingProgress: Boolean = true,
    val cacheMode: Int = 2, // LOAD_DEFAULT
    val textEncoding: String = "UTF-8"
)

data class GestureSettings(
    val enableSwipeNavigation: Boolean = true,
    val enableSwipeRefresh: Boolean = true,
    val enablePinchZoom: Boolean = true,
    val enableDoubleTapZoom: Boolean = true,
    val enableLongPressSelection: Boolean = true
)

data class MenuItem(
    val title: String,
    val url: String,
    val icon: Int? = null
)

val defaultMenuItems = listOf(
    MenuItem("Google", "https://www.google.com"),
    MenuItem("Yandex", "https://www.yandex.ru"),
    MenuItem("Bing", "https://www.bing.com")
) 