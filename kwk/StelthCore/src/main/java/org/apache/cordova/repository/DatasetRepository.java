package org.apache.cordova.repository;

import android.content.Context;

import org.apache.cordova.domen.UIVisibleDataset;

public interface DatasetRepository {

    interface RepoCallback {
        void successResponse(UIVisibleDataset value);

        void handleError(String message);
    }


    void getConfig(Context context); //String rawUrl
}
