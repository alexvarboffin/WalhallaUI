package nl.walhalla.library;

import com.google.android.gms.ads.AdRequest;
import com.walhalla.library.BuildConfig;

public class Const {

    //ca-app-pub-3940256099942544~3347511713 test app id
    public static String TEST_BANNER_ADS = "ca-app-pub-3940256099942544/6300978111";
    public static String TEST_INTERSTITIAL_ID = "ca-app-pub-3940256099942544/1033173712";
    public static String TEST_REWARDED_VIDEO_ID = "ca-app-pub-3940256099942544/5224354917";


    public static String[] testDevices() {
        return new String[]{
                AdRequest.DEVICE_ID_EMULATOR,
        };
    }
}
