package com.walhalla.landing.base;

import android.content.Context;
import android.view.View;

import androidx.appcompat.UWView;
import androidx.appcompat.widget.PopupMenu;

import com.walhalla.landing.R;


public class DynamicWebViewOld {

    private final UWView webView;

    public DynamicWebViewOld(Context context) {
        webView = new UWView(context);
        webView.setOnLongClickListener(v -> {
            showPopupMenu(context, v);
            return true;
        });
        // Настройка WebView
        //webView.getSettings().setJavaScriptEnabled(true);
    }

    private void showPopupMenu(Context context, View v) {
        PopupMenu popupMenu = new PopupMenu(context, v);
        popupMenu.inflate(R.menu.wv_menu);
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_home) {
                webView.loadUrl("http://www.example.com");
                return true;
            } else if (itemId == R.id.action_exit) {
                //context.finish();
                return true;
            } else {
                return false;
            }
        });
        popupMenu.show();
    }

    public UWView getWebView() {
        return webView;
    }

    // Дополнительные методы для настройки и использования WebView
}
