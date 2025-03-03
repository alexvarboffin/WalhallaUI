package com.walhalla.landing.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

//ThresholdSwipeRefreshLayout
//HalfScreenSwipeRefreshLayout if THRESHOLD = 0.5F

public class ThresholdSwipeRefreshLayout extends SwipeRefreshLayout {

    private static final float THRESHOLD = 0.4F;

    private float startY;
    private float screenHeight;

    public ThresholdSwipeRefreshLayout(Context context) {
        super(context);
        init(context);
    }

    public ThresholdSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float currentY = ev.getY();
                float distanceY = currentY - startY;

                // Проверяем, если свайп вверх больше 40% высоты экрана
                if (distanceY > (screenHeight * THRESHOLD)) {
                    return super.onInterceptTouchEvent(ev);
                }
                return false; // Игнорируем свайп, если он меньше 50%
        }
        return super.onInterceptTouchEvent(ev);
    }
}