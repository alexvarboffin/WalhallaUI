package tk.alexapp.freestuffandcoupons;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.FirebaseApp;

import android.app.Application;

import androidx.annotation.NonNull;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        MobileAds.initialize(this, initializationStatus -> {
            //getString(R.string.admobAppId)
        });
    }
}
