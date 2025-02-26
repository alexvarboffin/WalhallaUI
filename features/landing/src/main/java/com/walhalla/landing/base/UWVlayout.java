package com.walhalla.landing.base;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.UWView;
import androidx.appcompat.widget.PopupMenu;

import com.walhalla.landing.BuildConfig;
import com.walhalla.landing.R;
import com.walhalla.landing.databinding.CustomWebviewLayoutBinding;

public class UWVlayout extends RelativeLayout
//ChromeView
{

    public void setUWVCallback(UWVlayoutCallback callback) {
        this.callback = callback;
    }

    UWVlayoutCallback callback;

    public interface UWVlayoutCallback {

        void closeApplication();

        void copyToClipboard(String url);
    }

    public ImageView getButtonMenu() {
        return customWebviewLayoutBinding.buttonMenu;
    }

    private CustomWebviewLayoutBinding customWebviewLayoutBinding;

    public UWVlayout(Context context) {
        super(context);
        init(context);
    }

    public UWVlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public UWVlayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        customWebviewLayoutBinding = CustomWebviewLayoutBinding.inflate(inflater, this, true);
        customWebviewLayoutBinding.buttonMenu.setOnClickListener(v -> showPopupMenu(context, v));

        if(BuildConfig.DEBUG){
            setBackgroundColor(Color.parseColor("#80770000"));
        }
    }

    private void showPopupMenu(Context context, View v) {
        PopupMenu popupMenu = new PopupMenu(context, v);
        popupMenu.inflate(R.menu.wv_menu);
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_home) {
                String homeUrl = getWebView().getOriginalUrl();
                getWebView().loadUrl(homeUrl);
                return true;
            } else if (itemId == R.id.action_exit) {
                if (callback != null) {
                    callback.closeApplication();
                }
                return true;
            } else if (itemId == R.id.action_url_copy) {
                if (callback != null) {
                    String url = getWebView().getUrl();
                    callback.copyToClipboard(url);
                }
                return true;
            } else {
                return false;
            }
        });
        popupMenu.show();
    }

    public UWView getWebView() {
        return customWebviewLayoutBinding.inner00;
    }
}
