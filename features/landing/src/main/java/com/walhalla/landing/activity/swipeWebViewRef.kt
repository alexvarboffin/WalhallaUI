package com.walhalla.landing.activity

import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.appcompat.UWView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.walhalla.ui.BuildConfig

fun swipeWebViewRef(swipeLayout: SwipeRefreshLayout, webView: UWView) {
//        ViewTreeObserver vto = swipeLayout.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                // Calculate the trigger distance.
//                final DisplayMetrics metrics = getResources().getDisplayMetrics();
//                Float mDistanceToTriggerSync = Math.min(
//                        ((View) swipeLayout.getParent()).getHeight() * 0.6f, 120 * metrics.density);
//
//                try {
//                    // Set the internal trigger distance using reflection.
//                    Field field = SwipeRefreshLayout.class.getDeclaredField("mDistanceToTriggerSync");
//                    field.setAccessible(true);
//                    field.setFloat(swipeLayout, mDistanceToTriggerSync);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                // Only needs to be done once so remove listener.
//                ViewTreeObserver obs = swipeLayout.getViewTreeObserver();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    obs.removeOnGlobalLayoutListener(this);
//                } else {
//                    obs.removeGlobalOnLayoutListener(this);
//                }
//            }
//        });

//        ViewConfiguration viewConfiguration = ViewConfiguration.get(this);
//        GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
//            @Override
//            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//                // Если расстояние по оси Y больше чем расстояние по оси X и достаточное для прокрутки
//                if (Math.abs(distanceY) > Math.abs(distanceX) && Math.abs(distanceY) >
//                        viewConfiguration.getScaledTouchSlop()) {
//                    swipeLayout.setRefreshing(false);
//                }else {
//                    if (swipeLayout.isRefreshing()) {
//                        swipeLayout.setRefreshing(false);
//                    }
//                }
//                return super.onScroll(e1, e2, distanceX, distanceY);
//            }
//        });
//
//        swipeLayout.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));

//        int distanceToTriggerSync = 800; // расстояние в пикселях
//        swipeLayout.setDistanceToTriggerSync(distanceToTriggerSync);
//

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        webView.setOnScrollChangeListener { view: View, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (!view.canScrollVertically(-1)) {
                swipeLayout.isEnabled = true // Разрешить обновление при прокрутке вверх
            } else {
                if (swipeLayout.isRefreshing) {
                    swipeLayout.isRefreshing = false
                } // Запретить обновление при прокрутке вниз
            }
        }
    }


    swipeLayout.setOnRefreshListener {
        if (BuildConfig.DEBUG) {
            Toast.makeText(webView.context, "Swiped", Toast.LENGTH_SHORT).show()
        }
        if (swipeLayout.isRefreshing) {
            swipeLayout.isRefreshing = false
        }
        webView.reload()
    }
}