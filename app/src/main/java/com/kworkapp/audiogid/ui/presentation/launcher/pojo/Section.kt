package com.kworkapp.audiogid.ui.presentation.launcher.pojo

import androidx.annotation.Keep

@Keep
class Section {
    val timingRegionName: String = ""

    @kotlin.jvm.JvmField
    var items: List<Item> = emptyList()
    var total: Int = 0
}
