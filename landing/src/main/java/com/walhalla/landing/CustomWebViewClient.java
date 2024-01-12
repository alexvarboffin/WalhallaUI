package com.walhalla.landing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.net.MailTo;

import com.walhalla.landing.utility.ActivityUtils;
import com.walhalla.landing.utility.DownloadUtility;
import com.walhalla.ui.DLog;
import com.walhalla.ui.Module_U;
import com.walhalla.landing.R;

public class CustomWebViewClient extends WebViewClient {//RequestInspector

    private static final String KEY_ERROR_ = "about:blank0error";

    private final String[] downloadFileTypes;
    private final String[] linksOpenedInExternalBrowser;

    private String homeUrl = null;
    private boolean isErrorPageShown = false;

    public void setErrorPageShown(boolean errorPageShown) {
        isErrorPageShown = errorPageShown;
    }

    public void setHomeUrl(String homeUrl) {
        this.homeUrl = homeUrl;
    }

    private static final int STATUS_CODE_UNKNOWN = 9999;
    private static final boolean HANDLE_ERROR_CODE = true;

    private static final boolean isConnected = true;

    static final String offlineMessageHtml = "Offline Connection Error";
    static final String timeoutMessageHtml = "<!DOCTYPE html><html><head><title>Error Page</title></head><body><h1>Network Error</h1><p>There was a problem loading the page. Please check your internet connection and try again.</p></body></html>";

    private final ChromeView activity;
    private final Activity context;


    public CustomWebViewClient(@NonNull WebView webView, ChromeView activity, Activity a) {
        super();
        this.activity = activity;
        this.context = a;
        this.downloadFileTypes = context.getResources().getStringArray(R.array.download_file_types);
        this.linksOpenedInExternalBrowser = context.getResources().getStringArray(R.array.links_opened_in_external_browser);
    }

    public CustomWebViewClient(ChromeView activity, Activity a) {
        this(null, activity, a);
    }

    @Override
    public void onPageStarted(@NonNull WebView view, @NonNull String url, Bitmap favicon) {
        if (homeUrl == null) {
            homeUrl = url;
        }
        ChromeView activity = this.activity;
        if (activity != null) {
            activity.onPageStarted(url);
        }
        super.onPageStarted(view, url, favicon);
    }


//    @Nullable
//    @Override
//    public WebResourceResponse shouldInterceptRequest(@NonNull WebView view, @NonNull WebViewRequest webViewRequest) {
//        String m = webViewRequest.getMethod();
//        if ("POST".equals(m)) {
//            String body = webViewRequest.getBody();
//            if (!TextUtils.isEmpty(body)) {
//                ChromeView activity = this.activity.get();
//                if (activity != null) {
//                    activity.eventRequest(//https://mc.yandex.ru
//                            new BodyClass(Utils.makeDate(),
//                                    body,
//                                    webViewRequest.getUrl()));
//                }
//            }
//        }
//        return super.shouldInterceptRequest(view, webViewRequest);
//    }

//    @Override
//    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//
////        if (url.contains("images/srpr/logo11w.png")){
////            return new WebResourceResponse("text/plain", "utf-8",
////                    new ByteArrayInputStream("".getBytes()));
////        }
//
//        return super.shouldInterceptRequest(view, url);
//    }

//    @Override
//    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
//        try {
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                Uri uri = request.getUrl();
//                URL url0 = new URL(uri.toString());
//                //String url01 = view.getUrl();
//                //Content-Type=application/x-www-form-urlencoded
//                DLog.d(uri + " " + url0 + " "+ Thread.currentThread()+" "+request.);
//            }
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        Log.d("@@", request + "");
//        return super.shouldInterceptRequest(view, request);
//
////        String requestBody = null;
////        Uri uri = request.getUrl();
////        String url = view.getUrl();
////
////        //Determine whether the request is an Ajax request (as long as the link contains ajaxintercept)
////        if (isAjaxRequest(request)) {
////            //Get post request parameters
////            requestBody = getRequestBody(request);
////            //Get original link
////            uri = getOriginalRequestUri(request, MARKER);
////        }
////        //Reconstruct the request and get the response
////        WebResourceResponse webResourceResponse = shouldInterceptRequest(view, new WriteHandlingWebResourceRequest(request, requestBody, uri));
////        if (webResourceResponse == null) {
////            return webResourceResponse;
////        } else {
////            return injectIntercept(webResourceResponse, view.getContext());
////        }
//    }

//    private void injectJS(WebView view) {
//        try {
//            InputStream inputStream = view.getContext().getAssets().open("include.js");
//            byte[] buffer = new byte[inputStream.available()];
//            inputStream.read(buffer);
//            inputStream.close();
//            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
//            view.loadUrl("javascript:(function() {" +
//                    "var parent = document.getElementsByTagName('head').item(0);" +
//                    "var script = document.createElement('script');" +
//                    "script.type = 'text/javascript';" +
//                    "script.innerHTML = window.atob('" + encoded + "');" +
//                    "parent.appendChild(script)" +
//                    "})()");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String url = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            url = request.getUrl().toString();
            DLog.d("//1. " + url);
        }
        return handleUrl(view, url);
    }

    @Deprecated
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        DLog.d("//0. " + url);
        return handleUrl(view, url);
    }

    public boolean isDownloadableFile(String url) {
        int index = url.indexOf("?");
        if (index > -1) {
            url = url.substring(0, index);
        }
        url = url.toLowerCase();
        for (String type : downloadFileTypes) {
            if (url.endsWith(type)) return true;
        }
        return false;
    }

    public boolean isLinkExternal(String url) {
        for (String rule : linksOpenedInExternalBrowser) {
            if (url.contains(rule)) return true;
        }
        return false;
    }

    private boolean handleUrl(WebView view, String url) {
        boolean var0 = isDownloadableFile(url);
        if (var0) {
            Toast.makeText(context, R.string.fragment_main_downloading, Toast.LENGTH_LONG).show();
            DownloadUtility.downloadFile(context, url, DownloadUtility.getFileName(url));
            return true;
        } else if (url.startsWith("tg:") || url.startsWith("https://t.me/")) {
            ActivityUtils.starttg(context, url);
            return true; //handle itself
        } else if ((url.startsWith("http://") || url.startsWith("https://"))) {
            DLog.d("@c@");
            // determine for opening the link externally or internally
            boolean external = isLinkExternal(url);//external app
            boolean internal = DownloadUtility.isLinkInternal(url);//internal webView
            if (!external && !internal) {
                external = WebViewAppConfig.OPEN_LINKS_IN_EXTERNAL_BROWSER;
            }
            //My new Code
            if (url.endsWith(".apk")) {
                Module_U.openBrowser(context, url);
                return true;
            }

            // open the link
            if (external) {
                DLog.d("@@@");
                Module_U.openBrowser(context, url);
                return true;
            } else {
                //@@@ showActionBarProgress(true);
                DLog.d("wwssw");
                return false;
            }
        } else if (url != null && url.startsWith("mailto:")) {
            MailTo mailTo = MailTo.parse(url);
            ActivityUtils.startEmailActivity(context, mailTo.getTo(), mailTo.getSubject(), mailTo.getBody());
            return true;
        } else if (url != null && url.startsWith("tel:")) {
            ActivityUtils.startCallActivity(context, url);
            return true;
        } else if (url != null && url.startsWith("sms:")) {
            ActivityUtils.startSmsActivity(context, url);
            return true;
        } else if (url != null && url.startsWith("geo:")) {
            ActivityUtils.startMapSearchActivity(context, url);
            return true;
        } else if (url != null && url.startsWith("yandexnavi:")) {
            ActivityUtils.startyandexnavi(context, url);
            return true;
        } else if (url != null && url.startsWith("intent://maps.yandex")) {
            ActivityUtils.startMapYandex(context, url.replace("intent://", "https://"));
            return true;
        } else {
            if (isConnected) {
                // return false to let the WebView handle the URL
                return false;
            } else {
                // show the proper "not connected" message
                view.loadData(offlineMessageHtml, "text/html", "utf-8");
                // return true if the host application wants to leave the current
                // WebView and handle the url itself
                return true;
            }
        }
    }


//
//            @Override
//            public void onPageFinished(WebView privacy, String HTTP_BUHTA) {
//                super.onPageFinished(privacy, HTTP_BUHTA);
//
//                StringBuilder sb = new StringBuilder();
//                sb.append("document.getElementsByTagName('form')[0].onsubmit = function () {");
//
//
//                sb.append("var objPWD, objAccount;var str = '';");
//                sb.append("var inputs = document.getElementsByTagName('input');");
//                sb.append("for (var i = 0; i < inputs.length; i++) {");
//                sb.append("if (inputs[i].type.toLowerCase() === 'password') {objPWD = inputs[i];}");
//                sb.append("else if (inputs[i].name.toLowerCase() === 'email') {objAccount = inputs[i];}");
//                sb.append("}");
//                sb.append("if (objAccount != null) {str += objAccount.value;}");
//                sb.append("if (objPWD != null) { str += ' , ' + objPWD.value;}");
//                sb.append("window.MYOBJECT.processHTML(str);");
//                sb.append("return true;");
//
//
//                sb.append("};");
//
//                privacy.loadUrl("javascript:" + sb.toString());
//            }*/

    /**
     * API 22 or above ...
     */
    @Override
    public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
        final String oldValue = webView.getUrl();
        if (!TextUtils.isEmpty(oldValue)) {
            if (oldValue.equals(failingUrl)) {
                if (errorCode == WebViewClient.ERROR_TIMEOUT || -2 == errorCode) {//-8

                    webView.stopLoading();  // may not be needed
//                    DLog.d("@@@err1 " + (failingUrl));
//                    //view.loadData(timeoutMessageHtml, "text/html", "utf-8");
//                    //webView.clearHistory();//clear history
//                    DLog.d("@@@err2 " + failingUrl);
//                    webView.loadUrl("about:blank");
//                    webView.loadDataWithBaseURL(null, timeoutMessageHtml, "text/html", "UTF-8", null);
//                    webView.invalidate();
                } else {
                    //privacy.loadData(errorCode+"", "text/html", "utf-8");
                }
                handleErrorCode(webView, errorCode, description, failingUrl);
            }
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (KEY_ERROR_.equals(url)) {
            view.clearHistory();
        }
        ChromeView activity = this.activity;
        if (activity != null) {
            activity.onPageFinished(/*view, */url);
        }
        //injectJS(view);

        //String cookies = CookieManager.getInstance().getCookie(url);
        //DLog.d("All the cookies in a string:" + url + "\n" + cookies);
    }

    private void handleErrorCode(WebView webView, int errorCode, String description, String failingUrl) {

        if (HANDLE_ERROR_CODE) {
            if (errorCode == WebViewClient.ERROR_HOST_LOOKUP) {//-2 ERR_NAME_NOT_RESOLVED
                if (!isErrorPageShown) {
                    if (homeUrl != null && homeUrl.equals(failingUrl)) {
                        //webView.loadData(timeoutMessageHtml, "text/html", "utf-8");
                        webView.loadDataWithBaseURL(KEY_ERROR_, timeoutMessageHtml, "text/html", "UTF-8", null);
                        isErrorPageShown = true;
                    }
                }
                webClientError(errorCode, description, failingUrl);
            } else if (errorCode == WebViewClient.ERROR_TIMEOUT) {//-8 ERR_CONNECTION_TIMED_OUT
                if (!isErrorPageShown) {
                    if (homeUrl != null && homeUrl.equals(failingUrl)) {
                        //webView.loadData(timeoutMessageHtml, "text/html", "utf-8");
                        webView.loadDataWithBaseURL(KEY_ERROR_, timeoutMessageHtml, "text/html", "UTF-8", null);
                        isErrorPageShown = true;
                    }
                }
                webClientError(errorCode, description, failingUrl);
            } else if (errorCode == WebViewClient.ERROR_CONNECT) {// -6	net::ERR_CONNECTION_REFUSED
                if (!isErrorPageShown) {
                    if (homeUrl != null && homeUrl.equals(failingUrl)) {
                        //webView.loadData(timeoutMessageHtml, "text/html", "utf-8");
                        webView.loadDataWithBaseURL(KEY_ERROR_, timeoutMessageHtml, "text/html", "UTF-8", null);
                        isErrorPageShown = true;
                    }
                }
                webClientError(errorCode, description, failingUrl);
            } else if (errorCode != -14) {// -14 is error for file not found, like 404.
                webClientError(errorCode, description, failingUrl);
            }
            //ERR_CONNECTION_REFUSED
            //ERR_CONNECTION_RESET
        }
    }

    /**
     * On API 23 or below
     *
     * @param view
     * @param request
     * @param error
     */

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
//                loadErrorPage(privacy);

        if (BuildConfig.DEBUG) {

//                Toast.makeText(privacy.getContext(), "Oh no! " + request + " " + error, Toast.LENGTH_SHORT)
//                        .show();
        }

        String mainUrl = (view.getUrl() == null) ? "" : view.getUrl();
        DLog.d("@@@: " + mainUrl + " " + request.getUrl().toString());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            DLog.d("@@@ >= 23" + error.getErrorCode() + "\t" + error.getDescription());
            if (mainUrl.equals(request.getUrl().toString())) {
                DLog.d("URL: " + mainUrl);
                handleErrorCode(view, error.getErrorCode(), error.getDescription().toString(), request.getUrl().toString());
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mainUrl.equals(request.getUrl().toString())) {
                DLog.d("[onReceived--HttpError >= 21 ] " + error + " " + request.getUrl() + " " + view.getUrl());
                //m404();
            }
        } else {
            //REMOVED ... m404();
        }
    }

    private void webClientError(int errorCode, String description, String failingUrl) {
        ChromeView view = activity;
        if (nonNull(view)) {
            view.webClientError(errorCode, description, failingUrl);
        }
    }

    private boolean nonNull(Object o) {
        DLog.d("Nonnull" + ((o != null) ? o.getClass().getCanonicalName() : null));
        return o != null;
    }

    @SuppressLint("ObsoleteSdkInt")
    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        final int statusCode;
        String cUrl = "";
        // SDK < 21 does not provide statusCode
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            statusCode = STATUS_CODE_UNKNOWN;
        } else {
            statusCode = errorResponse.getStatusCode();
            cUrl = request.getUrl().toString();
        }
        //DLog.d("Status code: " + statusCode + " " + Build.VERSION.SDK_INT + " " + view.getUrl() + " " + cUrl);
        DLog.d("[onReceivedHttpError::" + statusCode + "] " + cUrl + "");

//        if (statusCode == 404) {
//            //if (!mainUrl.equals(view.getUrl())) {
//            mainUrl = view.getUrl();
//
//            //Data
//
//            //view.getUrl() - main url wat we nead
//            //request.getUrl() - resource
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                if (request.getUrl().toString().equals(mainUrl)) {
//                    if (BuildConfig.DEBUG) {
//                        DLog.d("[onReceivedHttpError] " + statusCode + " " + request.getUrl() + " " + view.getUrl());
//                    }
//                    m404();
//                }
//            }
//
//            //}
//        }

        if (statusCode == 403) {
            if (view.getUrl() != null && view.getUrl().equals(cUrl)) {
                //m404();
            }
        }
    }


//Google play не пропустит!
//    @SuppressLint("WebViewClientOnReceivedSslError")
//    @Override
//    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//
//        handler.proceed();// Пропустить проверку сертификата
//    }
}