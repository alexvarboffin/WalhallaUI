package org.apache.cordova.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import org.apache.cordova.R;
import org.imaginativeworld.oopsnointernet.dialogs.pendulum.DialogPropertiesPendulum;
import org.imaginativeworld.oopsnointernet.dialogs.pendulum.NoInternetDialogPendulum;
import org.imaginativeworld.oopsnointernet.dialogs.signal.DialogPropertiesSignal;
import org.imaginativeworld.oopsnointernet.dialogs.signal.NoInternetDialogSignal;
import org.imaginativeworld.oopsnointernet.snackbars.fire.NoInternetSnackbarFire;
import org.imaginativeworld.oopsnointernet.snackbars.fire.SnackbarPropertiesFire;

public class NetUtils {

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static void isOnlineFire(AppCompatActivity activity, ViewGroup mainContainer) {
        // No Internet Snackbar: Fire
        NoInternetSnackbarFire.Builder builder = new NoInternetSnackbarFire.Builder(
                mainContainer,
                activity.getLifecycle()
        );

        SnackbarPropertiesFire properties = builder.getSnackbarProperties();

        // Optional
        properties.setConnectionCallback(hasActiveConnection -> {
            // ...
        });
        properties.setDuration(Snackbar.LENGTH_INDEFINITE); // Optional
        properties.setNoInternetConnectionMessage("No active Internet connection!"); // Optional
        properties.setOnAirplaneModeMessage("You have turned on the airplane mode!"); // Optional
        properties.setSnackbarActionText(activity.getString(R.string.action_settings)); // Optional
        properties.setShowActionToDismiss(false); // Optional
        properties.setSnackbarDismissActionText(activity.getString(android.R.string.ok)); // Optional

        builder.build();
    }

    public static void isOnlineSignal(AppCompatActivity activity) {
        // No Internet Dialog: Signal
        NoInternetDialogSignal.Builder builder = new NoInternetDialogSignal.Builder(activity, activity.getLifecycle());
        DialogPropertiesSignal properties = builder.getDialogProperties();
        // Optional
        properties.setConnectionCallback(hasActiveConnection -> {
            // ...
        });

        properties.setCancelable(false); // Optional
        properties.setNoInternetConnectionTitle(activity.getString(R.string.no_internet_title)); // Optional
        properties.setNoInternetConnectionMessage(activity.getString(R.string.no_internet_message)); // Optional
        properties.setShowInternetOnButtons(true); // Optional
        properties.setPleaseTurnOnText(activity.getString(R.string.please_turn_on)); // Optional
        properties.setWifiOnButtonText(activity.getString(R.string.wifi_button)); // Optional
        properties.setMobileDataOnButtonText("Mobile data"); // Optional

        properties.setOnAirplaneModeTitle(activity.getString(R.string.on_airplane_mode_title)); // Optional
        properties.setOnAirplaneModeMessage(activity.getString(R.string.on_airplane_mode_message)); // Optional
        properties.setPleaseTurnOffText(activity.getString(R.string.please_turn_off)); // Optional
        properties.setAirplaneModeOffButtonText("Airplane mode"); // Optional
        properties.setShowAirplaneModeOffButtons(true); // Optional

        builder.build();
    }

    public static void isOnlinePendulum(AppCompatActivity activity) {
        // No Internet Dialog: Pendulum
        NoInternetDialogPendulum.Builder builder = new NoInternetDialogPendulum.Builder(activity, activity.getLifecycle());
        DialogPropertiesPendulum properties = builder.getDialogProperties();
        // Optional
        properties.setConnectionCallback(hasActiveConnection -> {
            // ...
        });
        properties.setCancelable(false); // Optional
        properties.setNoInternetConnectionTitle(activity.getString(R.string.no_internet)); // Optional
        properties.setNoInternetConnectionMessage(activity.getString(R.string.no_internet_message)); // Optional
        properties.setShowInternetOnButtons(true); // Optional
        properties.setPleaseTurnOnText(activity.getString(R.string.please_turn_on)); // Optional
        properties.setWifiOnButtonText(activity.getString(R.string.wifi_button)); // Optional
        properties.setMobileDataOnButtonText("Mobile data"); // Optional

        properties.setOnAirplaneModeTitle(activity.getString(R.string.no_internet)); // Optional
        properties.setOnAirplaneModeMessage(activity.getString(R.string.on_airplane_mode_message)); // Optional
        properties.setPleaseTurnOffText(activity.getString(R.string.please_turn_off)); // Optional
        properties.setAirplaneModeOffButtonText("Airplane mode"); // Optional
        properties.setShowAirplaneModeOffButtons(true); // Optional
        builder.build();
    }
}
