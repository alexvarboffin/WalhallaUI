package com.kworkapp.audiogid.ui.presentation.launcher.pojo

import androidx.annotation.Keep

@Keep
class DescriptionItem {
    @JvmField
    var name: String? = null

    var description: String? = null
        private set

    var link: String? = null
        private set

    constructor()

    constructor(name: String?, description: String?, link: String?) {
        this.name = name
        this.description = description
        this.link = link
    }
}
