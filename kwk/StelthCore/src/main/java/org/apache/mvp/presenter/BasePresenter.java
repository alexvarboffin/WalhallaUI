package org.apache.mvp.presenter;

import android.os.Handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BasePresenter {

    protected final ExecutorService executor = Executors.newSingleThreadExecutor();
    protected final Handler handler;

    public BasePresenter(Handler handler) {
        this.handler = handler;
    }

}
