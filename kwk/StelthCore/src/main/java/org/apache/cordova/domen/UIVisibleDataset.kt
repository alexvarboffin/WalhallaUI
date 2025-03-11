package org.apache.cordova.domen


// Класс для пользовательских интерфейсов
class UIVisibleDataset {
    private val dataset: Dataset

    constructor(dataset: Dataset) {
        this.dataset = dataset
    }

    constructor() {
        this.dataset = Dataset()
    }

    constructor(type: ScreenType?, url: String?) {
        this.dataset = Dataset()
        dataset.screenType = type
        dataset.url = url
    }

    constructor(isEnabled: Boolean?, successUrl: String?, isDemo: Boolean, whitelist: String?) {
        this.dataset = Dataset()
        dataset.enabled = isEnabled
        dataset.url = successUrl
        dataset.isDemo = isDemo
        dataset.whitelist = whitelist
    }


    var screenType: ScreenType?
        // Остальные методы для работы с UI...
        get() = dataset.screenType
        set(screenType) {
            dataset.screenType = screenType
        }

    var enabled: Boolean?
        get() = dataset.enabled
        set(enabled) {
            dataset.enabled = enabled
        }

    var orientation: Int?
        get() = dataset.orientation
        set(orientation) {
            dataset.orientation = orientation
        }

    var isWebview_external: Boolean
        get() = dataset.webview_external
        set(webview_external) {
            dataset.webview_external = webview_external
        }

    var viewType: String?
        get() = dataset.viewType
        set(viewType) {
            dataset.viewType = viewType
        }

    var isDemo: Boolean
        get() = dataset.isDemo
        set(demo) {
            dataset.isDemo = demo
        }

    val whitelist: String
        get() = dataset.whitelist

    var url: String?
        //    public void setWhitelist(String whitelist) {
        get() = dataset.url
        set(url) {
            dataset.url = url
        }

    fun isEnabled(): Boolean {
        return dataset.isEnabled
    } // Дополнительные методы, если необходимо
}