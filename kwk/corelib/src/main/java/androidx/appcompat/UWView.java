package androidx.appcompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

public class UWView extends android.webkit.WebView {

    private static final String TAG = "@@@";

    public UWView(Context context) {
        super(context);
    }

    public UWView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UWView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("NewApi")
    public UWView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public UWView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
    }

    @Override
    public void reload() {
        super.reload();
        Log.d(TAG, "reload: " + getUrl());
    }
}
