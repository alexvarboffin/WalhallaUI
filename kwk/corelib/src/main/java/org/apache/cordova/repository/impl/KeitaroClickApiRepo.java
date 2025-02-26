//package org.apache.cordova.repository.impl;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.os.Handler;
//import android.widget.Toast;
//
//import androidx.preference.PreferenceManager;
//
//import com.appsflyer.AppsFlyerLib;
//import com.google.gson.Gson;
//import com.onesignal.OSDeviceState;
//import com.onesignal.OneSignal;
//import com.walhalla.ui.DLog;
//
//import org.apache.cordova.utils.Utils;
//import org.apache.cordova.Cst;
//import org.apache.cordova.domen.ScreenType;
//import org.apache.cordova.TPreferences;
//
//import org.apache.cordova.domen.UIVisibleDataset;
//import org.apache.cordova.http.HttpClient;
//import org.apache.cordova.model.click_api.ClickApiResponse;
//import org.apache.cordova.repository.AbstractDatasetRepository;
//import org.apache.mvp.presenter.MainPresenter;
//import org.jetbrains.annotations.NotNull;
//import org.json.JSONObject;
//
//import java.io.IOException;
//
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.MediaType;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//import okhttp3.ResponseBody;
//
///**
// * Get URL From Server, with delay, with posttracker
// *     //Double Call
// *     //Request timeout
// */
//public class KeitaroClickApiRepo extends AbstractDatasetRepository
//        implements Callback {
//
//    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//    private static final String __U__ = "https://app.njatrack.tech/session/v2.0/{APPKEY}";
//    String privacyUrl = "https://wheel-slot.ru/lander/policy/index.html#";
//
//    private final String _URL_;
//    private final Handler handler;
//
//    public KeitaroClickApiRepo(Handler handler, String _URL_, Context context) {
//        super(context);
//        this._URL_ = _URL_;
//        this.handler = handler;
//    }
//
//    @Override
//    public void getConfig(Context context) {
//        //Delayed Request
//        Handler handler1 = new Handler();
//        handler1.postDelayed(() -> {
//            if (Utils.isNetworkAvailable(context.getApplicationContext())) {
//                tracker();
//            } else {
//                Toast.makeText(context,
//                        "No connection, pleas try again!", Toast.LENGTH_SHORT).show();
//            }
//        }, 1_000);
//    }
//
//    private void tracker() {
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
//        String url = MainPresenter.makeUrl(_URL_, preferences, context);
//        //DLog.d("@@" + url);
//        OkHttpClient client = HttpClient.getUnsafeOkHttpClient0(context);
//        //OkHttpClient client = new OkHttpClient();
//
//
//        //KEY_AF_ID + "=" +
//
//        Request request = new Request.Builder()
//                .url(url)
//                .post(makeBody(context))
//                .build();
//        Call call = client.newCall(request);
//        call.enqueue(this);
//    }
//
//    private static RequestBody makeBody(Context context) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
//            String ref = preferences.getString(CH_REFERRER, null);
//            jsonObject.put("appsflyer", "" + AppsFlyerLib.getInstance().getAppsFlyerUID(context));
//            jsonObject.put("facebook", "" + ref);
//
//        } catch (Exception e) {
//            DLog.handleException(e);
//        }
//
//        //return RequestBody.create(jsonObject.toString(), JSON);
//        return RequestBody.create(JSON, jsonObject.toString());
//    }
//
//    @Override
//    public void onFailure(@NotNull Call call, @NotNull IOException e) {
//        DLog.handleException(e);
//        handler.post(() -> {
//            callback.successResponse(new UIVisibleDataset(ScreenType.GAME_VIEW, null));
//            callback.handleError(e.getMessage());
//        });
//    }
//
//
//    String json = "";
//
//    @Override
//    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//
//        ResponseBody body = response.body();
//        json = "";
//        if (body != null) {
//            json = body.string();
//        }
//        handler.post(() -> {
////            if (Config.TAG_NOBOT.equals(var0)) {
////                makeScreen(new UIVisibleDataset(WEB_VIEW, "https://google.com"));
////            } else if (Cst.TAG_BOT.equals(var0)) {
////                makeScreen(new UIVisibleDataset(ScreenType.GAME_VIEW, null));
////            } else {
////                makeScreen(new UIVisibleDataset(ScreenType.GAME_VIEW, null));
////            }
//            if (json.isEmpty()) {
//                callback.successResponse(new UIVisibleDataset(ScreenType.GAME_VIEW, null));
//            } else {
//                try {
//                    Gson gson = new Gson();
//                    ClickApiResponse entity = gson.fromJson(json, ClickApiResponse.class);
//                    Boolean is_bot = entity.info.isBot;
//                    DLog.d("@bot@"+is_bot+" "+entity.info.url);
//
//                    //Before
////                    UIVisibleDataset aa = new UIVisibleDataset(ScreenType.WEB_RAW, entity.body);
////                    aa.setEnabled(true);
////                    callback.successResponse(aa);
//
//                    UIVisibleDataset aa = new UIVisibleDataset(ScreenType.WEB_VIEW, privacyUrl);
//                    aa.setEnabled(true);
//                    callback.successResponse(aa);
//
//                } catch (Exception e) {
//                    DLog.handleException(e);
//                }
//            }
//        });
//    }
//
//
//    public static void partNumTwo(Context context, String client_id) {
//        String firebase_push_token = TPreferences.getFirebaseToken(context);
//
//
//        OSDeviceState mm = OneSignal.getDeviceState();
//        String onesignal_player_id = "";//onesignal_player_id
//        if (mm != null && mm.getUserId() != null) {
//            onesignal_player_id = mm.getUserId();
//        }
//
//        DLog.d("[OS = 2]" + onesignal_player_id);
////                        DLog.d("@@@@@@@@@@[ID 2]@@@" + client_id);
////                        DLog.d("@@@@@@@@@@[ID 3]@@@" + firebase_push_token);//empty
//
////        RequestBody formBody = new FormBody.Builder()
////                .add("onesignal_player_id", "" + onesignal_player_id)
////                .add("client_id", "" + client_id)
////                .add("firebase_push_token", "" + firebase_push_token)
////                .build();
//
//        OkHttpClient client = HttpClient.getUnsafeOkHttpClient0(context);
////        String url = __U__
////                + client_id
////                + "?gaid=&apps_flyer_id=&client_id="
////                + "&onesignal_player_id=" + onesignal_player_id
////                + "&firebase_push_token=" + firebase_push_token;
//
//
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
//        String url = MainPresenter.makeUrl(__U__, preferences, context);
//        DLog.d("@[2]@" + url);
//
//        Request request = new Request.Builder()
//                .url(url)
//                .post(makeBody(context))
//                .build();
//
//        DLog.d("[URL_2]: " + url);
//
//        Call call0 = client.newCall(request);
//        call0.enqueue(new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                DLog.handleException(e);
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//
//                ResponseBody body = response.body();
//                String json0 = "";
//                if (body != null) {
//                    json0 = body.string();
//                    DLog.d("{@@@}" + json0);
//                }
//            }
//        });
//    }
//}
