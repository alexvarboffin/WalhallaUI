package org.apache;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;

import org.apache.cordova.R;

public class BackPressedUtil {

    public static void backPressedToast(@NonNull Activity activity) {
        //View view = activity.findViewById(android.R.id.content);
        View view = activity.findViewById(R.id.cLayout);
        if (view == null) {
            showToast(activity, R.string.press_again_to_exit, Toast.LENGTH_SHORT);
        } else {
            showSnackbar(activity, view, R.string.press_again_to_exit, Snackbar.LENGTH_LONG);
        }
    }

    private static void showToast(@NonNull Context context, int resId, int duration) {
        Toast.makeText(context, resId, duration).show();
    }

    private static void showSnackbar(@NonNull Activity activity, @NonNull View view, int resId, int duration) {
        Snackbar.make(view, resId, duration).setAction("Action", null).show();
    }
}
