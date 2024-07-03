//package com.walhalla.landing.activity;
//
//import android.annotation.SuppressLint;
//import android.app.Dialog;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.webkit.WebResourceRequest;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
//import androidx.fragment.app.DialogFragment;
//
//import com.walhalla.landing.R;
//import com.walhalla.ui.BuildConfig;
//
//public class WebViewDialogFragment extends DialogFragment {
//
//    private WebView newWebView;
//
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
//
//        LayoutInflater inflater = requireActivity().getLayoutInflater();
//        View view = inflater.inflate(R.layout.dialog_webview, null);
//        builder.setView(view);
//
//        newWebView = view.findViewById(R.id.webview);
//        setupWebView(newWebView);
//
//        builder.setPositiveButton("Закрыть", (dialog, which) -> dialog.dismiss());
//
//        return builder.create();
//    }
//
//    @SuppressLint("SetJavaScriptEnabled")
//    private void setupWebView(WebView webView) {
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setBackgroundColor(BuildConfig.DEBUG ? Color.YELLOW : Color.TRANSPARENT);
//        webView.setWebViewClient(new WebViewClient() {
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                String newUrl = request.getUrl().toString();
//                view.loadUrl(newUrl);
//                if (newUrl.contains("oauth.telegram.org/auth/push?")) {
//                    dismiss();
//                }
//                return true;
//            }
//        });
//    }
//
//
////    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
////        WebView newWebView = new WebView(view.getContext());
////        setupWebView(newWebView);
////
////        WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
////        transport.setWebView(newWebView);
////        resultMsg.sendToTarget();
////
////        show(getParentFragmentManager(), "WebViewDialogFragment");
////
////        return true;
////    }
//}