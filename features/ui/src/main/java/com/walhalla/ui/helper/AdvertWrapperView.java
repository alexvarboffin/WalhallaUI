package com.walhalla.ui.helper;

import androidx.lifecycle.LifecycleObserver;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AdvertWrapperView extends FrameLayout
        implements LifecycleObserver {

    public AdvertWrapperView(@NonNull Context context) {
        super(context);
    }

    public AdvertWrapperView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AdvertWrapperView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
