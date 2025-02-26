package org.apache.cordova.utility;

import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import org.apache.cordova.CordovaActivity;
import org.apache.cordova.R;
import org.apache.cordova.domen.ScreenType;
import org.apache.cordova.domen.UIVisibleDataset;
import org.apache.web.CustomTabs;
import org.apache.web.TwaLauncherUtils;

public class DemoUtils {

    private static String defUrl = "https://2ip.ru";

    public static void makeDemoToolbar(CordovaActivity context) {
        // Create a LinearLayout with horizontal orientation for RadioButtons
        LinearLayout radioLayout = new LinearLayout(context);
        radioLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        radioLayout.setOrientation(LinearLayout.HORIZONTAL);
        radioLayout.setGravity(Gravity.CENTER);
        radioLayout.setBackgroundColor(Color.RED);
        Button button = new Button(context);
        button.setText("1: GAME");
        button.setGravity(Gravity.CENTER);
        Button btnWebView = new Button(context);
        btnWebView.setText("WebView");
        btnWebView.setGravity(Gravity.CENTER);


        Button buttonTWA = new Button(context);
        buttonTWA.setText("TWA");
        buttonTWA.setGravity(Gravity.CENTER);

        Button buttonCts = new Button(context);
        buttonCts.setText("CustomTabs");
        buttonCts.setGravity(Gravity.CENTER);


        // Create a RadioGroup for switching WebView visibility
        LinearLayout layout = new LinearLayout(context);
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        layout.addView(button);
        layout.addView(btnWebView);
        layout.addView(buttonTWA);
        layout.addView(buttonCts);

//        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
//
//        });
        button.setOnClickListener(v -> {
            context.makeScreen(new UIVisibleDataset(ScreenType.GAME_VIEW, null));
        });
        btnWebView.setOnClickListener(v -> {
            //presenter.wrapContentRequest();
            context.makeScreen(new UIVisibleDataset(ScreenType.WEB_VIEW, defUrl));
        });
        buttonTWA.setOnClickListener(v -> {
            new TwaLauncherUtils(context).launchUrl("https://wsms.ru/app/");
        });
        buttonCts.setOnClickListener(v -> {
            new CustomTabs(context).launchUrl("https://wsms.ru/app/");
        });
        // Add RadioGroup to LinearLayout
        radioLayout.addView(layout);

        // Add LinearLayout to the main layout container
        context.rootContainer(radioLayout);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) radioLayout.getLayoutParams();
        layoutParams.gravity = Gravity.BOTTOM;
        radioLayout.setLayoutParams(layoutParams);
    }
}
