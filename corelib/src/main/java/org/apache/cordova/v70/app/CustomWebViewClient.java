package org.apache.cordova.v70.app;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.net.MailTo;

//import com.acsbendi.requestinspectorwebview.RequestInspectorWebViewClient;
//import com.acsbendi.requestinspectorwebview.WebViewRequest;

import com.walhalla.ui.DLog;
import com.walhalla.ui.Module_U;

import org.apache.cordova.BuildConfig;
import org.apache.cordova.ChromeView;
import org.apache.cordova.R;
import org.apache.cordova.v70.WebViewAppConfig;
import org.apache.cordova.v70.utility.ActivityUtils;
import org.apache.cordova.v70.utility.DownloadUtility;

public class CustomWebViewClient extends WebViewClient { //RequestInspector

    private static final int STATUS_CODE_UNKNOWN = 9999;
    private static final boolean HANDLE_ERROR_CODE = true;

    private static final boolean isConnected = true;

    static final String offlineMessageHtml = "Offline Connection Error";
    static final String timeoutMessageHtml = "Timeout Connection Error";

    private final ChromeView activity;
    private final Activity context;

    public CustomWebViewClient(@NonNull WebView webView, ChromeView chromeView, Activity a) {
        super();
        this.activity = chromeView;
        this.context = a;
    }

    public CustomWebViewClient(ChromeView activity, Activity a) {
        this.activity = activity;
        this.context = a;
    }

    @Override
    public void onPageStarted(@NonNull WebView view, @NonNull String url, Bitmap favicon) {
        ChromeView activity = this.activity;
        if (activity != null) {
            activity.onPageStarted();
        }
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        ChromeView activity = this.activity;
        if (activity != null) {
            activity.onPageFinished(view, url);
        }
        //injectJS(view);

        //String cookies = CookieManager.getInstance().getCookie(url);
        //DLog.d("All the cookies in a string:" + url + "\n" + cookies);
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
//                    activity.eventRequest(
    //https://mc.yandex.ru
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


    /* @SuppressWarnings("deprecation")*/
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {


        //DLog.d("1@@@@" + url);

//        String webview_url = context.getString(R.string.app_url);
//        String device_id = Util.phoneId(MainActivity.getAppContext().getApplicationContext());
//
//        if (url.startsWith("tel:")) {
//            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
//            startActivity(intent);
//            return true;
//        } else if (url.startsWith(webview_url)) {
//            view.loadUrl(url + "?id=" + device_id);
//            return true;
//        } else {
//            view.getContext().startActivity(
//                    new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
//            return true;
//        }


//                if (request..indexOf("host")<=0) {
//                    // the link is not for activity page on my site, so launch another Activity that handles URLs
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    startActivity(intent);
//                    return true;
//                }
//                return false;

        //@@@
        if (url.startsWith("app://{package_id}") || url.startsWith("uniwebview://accept")
                || url.startsWith("uniwebview://close")) {
            ChromeView mainActivity = activity;
            if (mainActivity != null) {
                mainActivity.mAcceptPressed(url);
            }
            return true;
        }

        ..@@
    }

//            @TargetApi(Build.VERSION_CODES.N)
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView privacy, WebResourceRequest request) {
//                final Uri uri = request.getConfig();
//                return handleUri(uri);
//            }
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            DLog.d("@@@ >= 23" + error.getErrorCode() + "\t" + error.getDescription());
            if (isThisUrl(view, request)) {
                handleErrorCode(error.getErrorCode(), error.getDescription().toString(), request.getUrl().toString());
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (isThisUrl(view, request)) {
                DLog.d("[onReceived--HttpError >= 21 ] " + error + " " + request.getUrl() + " " + view.getUrl());
                //m404();
            }
        } else {
            //REMOVED ... m404();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private boolean isThisUrl(WebView webView, WebResourceRequest request) {
        String mainUrl = (webView.getUrl() == null) ? "" : webView.getUrl();
        String requestUrl = request.getUrl().toString();
        boolean result = mainUrl.equals(requestUrl);
        DLog.d("####: " + mainUrl + " " + requestUrl + " " + result);
        return result;
    }


    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {

        final int statusCode;
        // SDK < 21 does not provide statusCode
        if (Build.VERSION.SDK_INT < 21) {
            statusCode = STATUS_CODE_UNKNOWN;
        } else {
            statusCode = errorResponse.getStatusCode();
        }

        String cUrl = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cUrl = request.getUrl().toString();
        }

        //DLog.d("Status code: " + statusCode + " " + Build.VERSION.SDK_INT + " " + view.getUrl() + " " + cUrl);
        DLog.d("[" + statusCode + "] " + cUrl + "");

        if (statusCode == 404) {
            //if (!mainUrl.equals(view.getUrl())) {
            //Data

            //view.getUrl() - main url wat we nead
            //request.getUrl() - resource
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (isThisUrl(view, request)) {
                    if (BuildConfig.DEBUG) {
                        DLog.d("[onReceivedHttpError] " + statusCode + " " + request.getUrl() + " " + view.getUrl());
                    }
                    webClientError();
                    //DLog.d("@@@@@@@@@@@@ {}{404}");
                }
            }

            //}
        } else if (statusCode == 403) {
            if (view.getUrl() != null && view.getUrl().equals(cUrl)) {
                //m404();
            }
        }
    }

}

