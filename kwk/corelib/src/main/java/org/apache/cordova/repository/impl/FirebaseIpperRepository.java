//package org.apache.cordova.repository.impl;
//
//import static org.apache.cordova.E._IP_INFO_URL_;
//import static org.apache.cordova.constants.TableField.FIELD_UPDATE_AT;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.os.Handler;
//
//import android.text.TextUtils;
//
//import androidx.annotation.NonNull;
//import androidx.preference.PreferenceManager;
//
//import com.google.gson.Gson;
//import com.walhalla.ui.BuildConfig;
//import com.walhalla.ui.DLog;
//
//import org.apache.cordova.utils.Utils;
//
//import org.apache.cordova.E;
//import org.apache.cordova.domen.ScreenType;
//import org.apache.cordova.TPreferences;
//
//import org.apache.cordova.domen.UIVisibleDataset;
//import org.apache.cordova.http.HttpClient;
//import org.apache.cordova.model.IpApi;
//import org.apache.cordova.repository.AbstractDatasetRepository;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//import okhttp3.ResponseBody;
//
///**
// * CLONE->RemoteRepository
// */
//public class FirebaseIpperRepository extends AbstractDatasetRepository implements Callback {
//
//    private static final String COUNTRY_CODE = BuildConfig.DEBUG ? "" : "INUS";
//    private static final boolean TRACKER_ENABLED = true;
//    private final Gson gson = new Gson();
//
//    private final Handler handler;
//
//    public FirebaseIpperRepository(Handler handler, Context context) {
//        super(context);
//        this.handler = handler;
//    }
//
//    @Override
//    public void getConfig(Context context) {
//        OkHttpClient client = HttpClient.getUnsafeOkHttpClient(context);
//        //private final String _URL_ = "https://ipinfo.io/json";
//
//        Request request = new Request.Builder()
//                .url(E.d(_IP_INFO_URL_))
//                .build();
//        Call call = client.newCall(request);
//        call.enqueue(this);
//    }
//
//    @Override
//    public void onFailure(@NonNull Call call, @NonNull IOException e) {
//        DLog.handleException(e);
//        //handler.post(() -> {
//        callback.successResponse(new UIVisibleDataset(ScreenType.GAME_VIEW, null));
//        //});
//    }
//
//    String json = "";
//
//    @Override
//    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//        ResponseBody body = response.body();
//        json = "";
//        if (body != null) {
//            json = body.string();
//        }
//        //IPInfoRemote aa = IpInfoRepositoryExternal.fetch(json);
//        //DLog.d("@" + json);
//        IpApi entity = gson.fromJson(json, IpApi.class);
//
//        if (entity == null || TextUtils.isEmpty(entity.countryCode)) {//Try next time
//            DLog.d("@@Try next time @@ " + entity);
//        } else {
//            boolean blocked = (COUNTRY_CODE).contains(entity.countryCode); //BLOCK #1+"UA"
//            DLog.d("@@@@" + entity.countryCode + " " + blocked);
//            if (blocked) {//Mute next request
//                DLog.d("@@Mute next request@@");
//                TPreferences pref = TPreferences.getInstance(this.context);
//                pref.setMute("true");
////                if (callback != null) {
////                    callback.successResponse(new UIVisibleDataset(false, null, false, ""));
////                }
//
//            } else {
//                DLog.d("@@Firebase@@");
//                FirebaseRepository repository = new FirebaseRepository(context);
//                repository.setCallback(callback);
//                repository.getConfig(context);
//
////                try {
////                    UIVisibleDataset aa = new UIVisibleDataset(ScreenType.WEB_VIEW, entity.url);
////                    aa.setEnabled(true);
////                    callback.successResponse(aa);
////                } catch (Exception e) {
////                    DLog.handleException(e);
////                }
//            }
//
//
//            //@@@@
//            //"status"	"success"
//            //"country
//            //"countryCode	""
//            //"region	"01"
//            //"regionName
//            //"city	""
//            //"zip	""
//            //"lat	42
//            //"lon	3
//            //"timezone	"E
//            //"isp	"
//            //"org	"
//            //"as	"
//            //"query	"1
//
//            if (TRACKER_ENABLED && !BuildConfig.DEBUG) {
//                Map<String, Object> map = new HashMap<>();
//                map.put(FIELD_UPDATE_AT, Utils.makeDate());
//                map.put("entity", entity);
//                map.put("blocked", blocked);
//                FirebaseRepository.update(context, map);
//            }
//        }
//    }
//}
