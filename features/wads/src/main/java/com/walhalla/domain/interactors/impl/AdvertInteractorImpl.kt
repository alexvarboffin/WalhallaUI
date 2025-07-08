package com.walhalla.domain.interactors.impl

import android.view.View
import android.view.ViewGroup
//import com.walhalla.boilerplate.domain.executor.Executor
//import com.walhalla.boilerplate.domain.executor.MainThread
//import com.walhalla.boilerplate.domain.interactors.base.AbstractInteractor
import com.walhalla.domain.interactors.AdvertInteractor
import com.walhalla.domain.repository.AdvertRepository
import com.walhalla.library.AdMobCase.interstitialBannerRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * AbstractInteractor
 * запустит все в фоновом потоке...
 */
class AdvertInteractorImpl(
    //threadExecutor: Executor, mainThread: MainThread,

    var threadExecutor: CoroutineScope = CoroutineScope(Dispatchers.IO),
    var mainThread: CoroutineScope = MainScope()//CoroutineScope(Dispatchers.Main)

    , var mAdvertRepository: AdvertRepository
) :
/*AbstractInteractor(threadExecutor, mainThread),*/ AdvertInteractor {

//    //Отдаем результат
//    fun notifyError() {
//        mMainThread.post {}
//    }
//
//    fun postMessage(msg: String) {
//        mMainThread.post {}
//    }

    override fun selectView(viewGroup: ViewGroup, callback: AdvertInteractor.Callback<View>) {
        threadExecutor.launch {
            val adView = mAdvertRepository.getNewAdsBanner(viewGroup)

            if (adView == null) {
                mainThread.launch {
                    callback.onRetrievalFailed("View not found")
                }
                return@launch
            }

            mainThread.launch {
                callback.onMessageRetrieved(viewGroup.id, adView)
                interstitialBannerRequest(adView)
            }
        }
    }

    //override fun run() {}
}
