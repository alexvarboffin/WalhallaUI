package com.walhalla.landing.config

class Cfg private constructor(builder: Builder) {
    val isToolbarEnabled: Boolean
    val counterTimeMs: Long
    val isSplashScreenEnabled: Boolean

    var isCheckConnection: Boolean
    val isSwipeEnabled: Boolean
    val isProgressEnabled: Boolean

    init {
        this.isToolbarEnabled = builder.toolbarEnabled
        this.counterTimeMs = builder.counterTimeMs
        this.isSplashScreenEnabled = builder.splashScreenEnabled
        this.isCheckConnection = builder.checkConnection
        this.isSwipeEnabled = builder.swipeEnabled
        this.isProgressEnabled = builder.progressEnabled
    }

    // Установка значений по умолчанию
    class Builder {
        var toolbarEnabled = false
        var counterTimeMs: Long = 2400
        var splashScreenEnabled = true
        var checkConnection = true
        var swipeEnabled = false
        var progressEnabled = true

        fun setToolbarEnabled(toolbarEnabled: Boolean): Builder {
            this.toolbarEnabled = toolbarEnabled
            return this
        }

        fun setCounterTimeMs(counterTimeMs: Long): Builder {
            this.counterTimeMs = counterTimeMs
            return this
        }

        fun setSplashScreenEnabled(splashScreenEnabled: Boolean): Builder {
            this.splashScreenEnabled = splashScreenEnabled
            return this
        }

        fun setCheckConnection(checkConnection: Boolean): Builder {
            this.checkConnection = checkConnection
            return this
        }

        fun setSwipeEnabled(swipeEnabled: Boolean): Builder {
            this.swipeEnabled = swipeEnabled
            return this
        }

        fun setProgressEnabled(progressEnabled: Boolean): Builder {
            this.progressEnabled = progressEnabled
            return this
        }

        fun build(): Cfg {
            return Cfg(this)
        }
    }
}

