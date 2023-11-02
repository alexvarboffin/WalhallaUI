package tk.alexapp.freestuffandcoupons.log;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

public class FirebaseCrashlytics0 {

    public static void log(String systemLanguage) {
        String message = "Language changed: " + systemLanguage;
        FirebaseCrashlytics.getInstance().log(message);
    }
}
