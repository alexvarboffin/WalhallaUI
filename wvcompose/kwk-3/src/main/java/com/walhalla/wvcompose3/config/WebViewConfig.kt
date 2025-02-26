package com.walhalla.wvcompose3.config

data class WebViewConfig(
    val showToolbar: Boolean = true,
    val showBottomMenu: Boolean = true,
    val loadingType: LoadingType = LoadingType.PROGRESS,
    val menuItems: List<MenuItem> = listOf(
        MenuItem("Пункт 1", "https://google.com/1"),
        MenuItem("Пункт 2", "https://google.com/2"),
        MenuItem("Пункт 3", "https://google.com/3"),
        MenuItem("Игра", "", MenuItemType.GAME)
    ),
    val webViewSettings: WebViewSettings = WebViewSettings(),
    val theme: WebViewTheme = WebViewTheme.SYSTEM,
    val gestureSettings: GestureSettings = GestureSettings()
)

data class MenuItem(
    val title: String,
    val url: String,
    val type: MenuItemType = MenuItemType.WEB,
    val isBookmarked: Boolean = false
)

enum class MenuItemType {
    WEB,
    GAME
}

data class WebViewSettings(
    val enableJavaScript: Boolean = true,
    val enableNavigation: Boolean = true,
    val showLoadingProgress: Boolean = true,
    val userAgent: String? = null,
    val allowFileAccess: Boolean = false,
    val enableZoom: Boolean = true,
    val textZoomPercent: Int = 100,
    val enableReaderMode: Boolean = true,
    val forceDarkMode: Boolean = false
)

data class GestureSettings(
    val enableSwipeNavigation: Boolean = true,
    val enableSwipeRefresh: Boolean = true,
    val enablePinchZoom: Boolean = true,
    val swipeSensitivity: Float = 1.0f
)

enum class WebViewTheme {
    LIGHT,
    DARK,
    SYSTEM
}

enum class LoadingType {
    PROGRESS,
    SPLASH_SCREEN
} 