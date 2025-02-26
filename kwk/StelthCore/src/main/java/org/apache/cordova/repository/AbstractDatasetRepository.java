package org.apache.cordova.repository;

import android.content.Context;

public abstract class AbstractDatasetRepository implements DatasetRepository {

//    protected final RepoCallback callback;
//    protected final Context context;
//
//    public AbstractDatasetRepository(Context context, RepoCallback callback) {
//        this.callback = callback;
//        this.context = context;
//    }


    protected RepoCallback callback;
    protected final Context context;

    public AbstractDatasetRepository(Context context) {
        this.context = context;
    }

    public void setCallback(RepoCallback callback) {
        this.callback = callback;
    }


}
