package org.apache.cordova.repository

import android.content.Context
import org.apache.cordova.domen.UIVisibleDataset

interface DatasetRepository {
    interface RepoCallback {
        fun successResponse(value: UIVisibleDataset)

        fun handleError(message: String?)
    }


    fun getConfig(context: Context?) //String rawUrl
}
