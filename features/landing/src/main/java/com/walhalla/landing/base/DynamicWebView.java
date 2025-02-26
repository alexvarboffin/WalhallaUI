package com.walhalla.landing.base;

import android.app.Activity;
import android.os.Build;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.UWView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.walhalla.landing.activity.WPresenter;
import com.walhalla.webview.ChromeView;

public class DynamicWebView {

    private final UWVlayout uwVlayout;
    private final ActivityConfig config;
    private final Activity context;

    private SwipeRefreshLayout swipe;


    public void setCallback(UWVlayout.UWVlayoutCallback callback) {
        this.callback = callback;
        this.uwVlayout.setUWVCallback(callback);
    }

    UWVlayout.UWVlayoutCallback callback;

    public DynamicWebView(Activity context, ActivityConfig config) {
        this.uwVlayout = new UWVlayout(context);
        this.context = context;
        this.config = config;
//        uwVlayout.setOnLongClickListener(v -> {
//            showPopupMenu(context, v);
//            return true;
//        });
        // Настройка WebView
        //webView.getSettings().setJavaScriptEnabled(true);
    }

//    private void showPopupMenu(Context context, View v) {
//        PopupMenu popupMenu = new PopupMenu(context, v);
//        popupMenu.inflate(R.menu.wv_menu);
//        popupMenu.setOnMenuItemClickListener(item -> {
//            int itemId = item.getItemId();
//            if (itemId == R.id.action_home) {
//                uwVlayout.loadUrl("http://www.example.com");
//                return true;
//            } else if (itemId == R.id.action_exit) {
//                //context.finish();
//                return true;
//            } else {
//                return false;
//            }
//        });
//        popupMenu.show();
//    }

    public UWVlayout getParent() {
        return uwVlayout;
    }

    public UWView getWebView() {
        return uwVlayout.getWebView();
    }

    public void generateViews(ChromeView chromeView, RelativeLayout parent, WPresenter presenter) {
        // Создайте экземпляр DynamicWebView и получите его WebView
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        uwVlayout.setLayoutParams(lp);
        if (config.isSwipeEnabled()) {
            swipe = new SwipeRefreshLayout(context);
            swipe.setLayoutParams(lp);
            parent.addView(swipe);
            swipe.addView(uwVlayout);
            swipe.setRefreshing(false);
            swipeWebViewRef(swipe, uwVlayout.getWebView());
        } else {
            parent.addView(uwVlayout);
        }

        //mWebView.setBackgroundColor(Color.BLACK);
        // register class containing methods to be exposed to JavaScript
        presenter.a123(chromeView, getWebView());
    }

    private void swipeWebViewRef(SwipeRefreshLayout swipe, UWView webView) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            webView.setOnScrollChangeListener((view, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                if (!view.canScrollVertically(-1)) {
                    swipe.setEnabled(true); // Разрешить обновление при прокрутке вверх
                } else {
                    if (swipe.isRefreshing()) {
                        swipe.setRefreshing(false);
                    } // Запретить обновление при прокрутке вниз
                    swipe.setEnabled(false);
                }

//                int scrollY = rootScrollView.getScrollY(); // For ScrollView
//                int scrollX = rootScrollView.getScrollX(); // For HorizontalScrollView
                // DO SOMETHING WITH THE SCROLL COORDINATES


//                View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);
//                int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));
//
//                // если diff равен 0 - вы достигли конца скрола
//                if (diff == 0) {
//                    // some code
//                }
            });
        }


        swipe.setOnRefreshListener(() -> {
            swipe.setRefreshing(false);
            webView.reload();
        });
    }

}
