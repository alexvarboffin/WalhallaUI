package org.apache.cordova;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.UWView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public abstract class BrowserActivity extends androidx.appcompat.app.AppCompatActivity {

    protected SwipeRefreshLayout swipeRefreshLayout;
    protected UWView __mView;

    protected void onViewCreated(AppCompatActivity view, Context context) {
        FrameLayout container = view.findViewById(R.id.viewer);
        onViewCreated(container,this);
    }

    protected void onViewCreated(ViewGroup view, Context context) {
        //mView = view.findViewById(R.id.web_view);
        //swipeRefreshLayout = view.findViewById(R.id.refresh);
        swipeRefreshLayout = new SwipeRefreshLayout(context);
        __mView = new UWView(new ContextThemeWrapper(context, R.style.AppTheme));
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        );
        swipeRefreshLayout.setLayoutParams(lp);
        __mView.setLayoutParams(lp);
        view.addView(swipeRefreshLayout);
        swipeRefreshLayout.addView(__mView);
    }

}
