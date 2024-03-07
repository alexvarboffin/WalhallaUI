package tk.alexapp.freestuffandcoupons;

import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeSettingsChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (Intent.ACTION_TIME_CHANGED.equals(action) || Intent.ACTION_TIMEZONE_CHANGED.equals(action)) {
            // Обработка смены часового пояса
            // Вы можете выполнить необходимые действия
        } else if (Intent.ACTION_LOCALE_CHANGED.equals(action)) {
            // Обработка смены языка
            updateLocale(context);
        }

        dlog(context, intent);
    }

    private void dlog(Context context, Intent intent) {
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("@@")
                .setMessage(intent.toString())
                .setPositiveButton("Yes", (dialog, which) -> {
                    //finish();
                })
                .setNegativeButton("No", null)
                .show();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void updateLocale(Context context) {

        // Обработка смены языка и отправка события
        String currentLanguage = Locale.getDefault().getLanguage();
        String deviceName = Build.MODEL;
        String systemLanguage = Locale.getDefault().getDisplayLanguage();
        String currentTime = getCurrentTime();

        Log.d("TimeSettingsChange", "Device: " + deviceName);
        Log.d("TimeSettingsChange", "System Language: " + systemLanguage);
        Log.d("TimeSettingsChange", "Current Language: " + currentLanguage);
        Log.d("TimeSettingsChange", "Current Time: " + currentTime);


        Locale locale = new Locale("your_language_code"); // Например, "en" для английского
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;

        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        // Здесь вы можете выполнить дополнительные действия после смены языка
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
