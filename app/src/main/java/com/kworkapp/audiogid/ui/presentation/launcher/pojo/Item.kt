package com.kworkapp.audiogid.ui.presentation.launcher.pojo

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Item(var name: String?, var description: String?, var link: String?,
                var timingName: String? = null,
                var descriptionName: String? = null,
                var start: String? = null,
                var end: String? = null) : Parcelable
