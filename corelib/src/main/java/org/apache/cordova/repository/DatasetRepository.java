package org.apache.cordova.repository;

import android.content.Context;

import org.apache.cordova.domen.Dataset;

public interface DatasetRepository {

    interface RepoCallback {
        void successResponse(Dataset value);

        void handleError(String message);
    }


    void getConfig(Context context); //String rawUrl
}
