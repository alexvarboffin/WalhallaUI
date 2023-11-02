package com.walhalla.domain.interactors.impl;

import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdView;
import com.walhalla.boilerplate.domain.executor.Executor;
import com.walhalla.boilerplate.domain.executor.MainThread;
import com.walhalla.boilerplate.domain.interactors.base.AbstractInteractor;

import com.walhalla.domain.interactors.AdvertInteractor;
import com.walhalla.domain.repository.AdvertRepository;
import com.walhalla.library.AdMobCase;

/**
 * AbstractInteractor
 * запустит все в фоновом потоке...
 */

public class AdvertInteractorImpl extends AbstractInteractor
        implements AdvertInteractor {

    private AdvertRepository mAdvertRepository;

    public AdvertInteractorImpl(
            Executor threadExecutor,
            MainThread mainThread,
            AdvertRepository repository) {
        super(threadExecutor, mainThread);
        mAdvertRepository = repository;
    }

    //Отдаем результат
    public void notifyError() {
        mMainThread.post(() -> {
            //mCallback.onRetrievalFailed("Nothing to welcome you with :(");
        });
    }

    public void postMessage(final String msg) {
        mMainThread.post(() -> {
            //mCallback.onMessageRetrieved(msg);
        });
    }

    @Override
    public void selectView(ViewGroup viewGroup, final Callback<View> callback) {
        Runnable runnable = () -> {
//            try {
//                String name = Thread.currentThread().getName();
//                Log.i(TAG, "Foo " + name);
//                TimeUnit.SECONDS.sleep(1);
//                Log.i(TAG, "Bar " + name);

                final int id = viewGroup.getId();
                final AdView adView = mAdvertRepository.getNewAdsBanner(viewGroup);

                if (adView == null) {
                    callback.onRetrievalFailed("View not found");
                    return;
                }
                mMainThread.post(() -> {
                    callback.onMessageRetrieved(id, adView);
                    AdMobCase.interstitialBannerRequest(adView);
                });
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        };
        mThreadExecutor.submit(runnable);
    }

    @Override
    public void run() {}
}
