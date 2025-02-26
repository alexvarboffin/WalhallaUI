package com.walhalla.domain.interactors

import android.view.View
import android.view.ViewGroup
import com.walhalla.boilerplate.domain.interactors.base.Interactor

interface AdvertInteractor : Interactor {
    //Отстyk в главный тред
    interface Callback<T> {
        // TODO: Add interactor callback methods here
        fun onMessageRetrieved(id: Int, message: T)

        fun onRetrievalFailed(error: String)
    }

    //run <--base
    fun selectView(id: ViewGroup, callback: Callback<View>)
}
