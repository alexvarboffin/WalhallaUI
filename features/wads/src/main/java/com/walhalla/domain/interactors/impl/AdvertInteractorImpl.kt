package com.walhalla.domain.interactors.impl

import android.view.View
import android.view.ViewGroup
import com.walhalla.boilerplate.domain.executor.Executor
import com.walhalla.boilerplate.domain.executor.MainThread
import com.walhalla.boilerplate.domain.interactors.base.AbstractInteractor
import com.walhalla.domain.interactors.AdvertInteractor
import com.walhalla.domain.repository.AdvertRepository
import com.walhalla.library.AdMobCase.interstitialBannerRequest

/**
 * AbstractInteractor
 * запустит все в фоновом потоке...
 */
class AdvertInteractorImpl(threadExecutor: Executor, mainThread: MainThread,
    private val mAdvertRepository: AdvertRepository
) : AbstractInteractor(threadExecutor, mainThread), AdvertInteractor {
    //Отдаем результат
    fun notifyError() {
        mMainThread.post {}
    }

    fun postMessage(msg: String) {
        mMainThread.post {}
    }

    override fun selectView(viewGroup: ViewGroup, callback: AdvertInteractor.Callback<View>) {
        val runnable = Runnable {
//            try {
//                String name = Thread.currentThread().getName();
//                Log.i(TAG, "Foo " + name);
//                TimeUnit.SECONDS.sleep(1);
//                Log.i(TAG, "Bar " + name);
            val id = viewGroup.id
            val adView = mAdvertRepository.getNewAdsBanner(viewGroup)

            if (adView == null) {
                callback.onRetrievalFailed("View not found")
                return@Runnable
            }
            mMainThread.post {
                callback.onMessageRetrieved(id, adView)
                interstitialBannerRequest(adView)
            }
        }
        mThreadExecutor.submit(runnable)
    }

    override fun run() {}
}
