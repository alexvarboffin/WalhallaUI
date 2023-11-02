package com.walhalla.domain.interactors;

import android.view.View;
import android.view.ViewGroup;

import com.walhalla.boilerplate.domain.interactors.base.Interactor;

public interface AdvertInteractor extends Interactor {

    //Отстyk в главный тред
    interface Callback<T> {
        // TODO: Add interactor callback methods here
        void onMessageRetrieved(int id, T message);

        void onRetrievalFailed(String error);
    }

    //run <--base
    void selectView(ViewGroup id, Callback<View> callback);
}
