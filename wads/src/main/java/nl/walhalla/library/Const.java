package nl.walhalla.library;

import com.google.android.gms.ads.AdRequest;
import com.walhalla.library.BuildConfig;

public class Const {


    private static final boolean DEBUG = BuildConfig.DEBUG;
    //ca-app-pub-3940256099942544~3347511713 test app id
    public static String TEST_BANNER_ADS = "ca-app-pub-3940256099942544/6300978111";
    public static String TEST_INTERSTITIAL_ID = "ca-app-pub-3940256099942544/1033173712";
    public static String TEST_REWARDED_VIDEO_ID = "ca-app-pub-3940256099942544/5224354917";

    public static String[] testDevices() {

        if (DEBUG) {
            return new String[]{
                    "330FD453402153D99F79DD7C25317FEC",
                    "4D01657EE12797A512F48F4177D1D7B1",
                    "0D2A5ED7525433C1F516A9C73E0BC6B8",
                    AdRequest.DEVICE_ID_EMULATOR,
                    "849CAA47E0F3319B7F8B8030397EBD18",
                    "7D2FA7A23F13AE4D002EA0B0366F8E43",
                    "C3D380C19B875F807C597DC0E0229963",
                    "73652235E6BB7DCDF74F6DF97E6F53E4",
                    "E61BEECC9132E6A2C3EF1421BE6E1CF1",
                    "A514FA02FAD622C7C5CB24E98C384574",
                    "4B0192C0BE1FECA5FA018578AFD762C6",
                    "F6183A7D5F1195AEC9302D29DADBF450",
                    "D97101CD912C42082D0C57DF7EC70E26",
                    "420B3C23A53A68C1F994B6E2043964AA",
                    "BAA04F48EC6D5FE611A1C720812E3428",
                    "1FFCD512BD8AE45F7647127CB80345E8",
                    "D835BCD2872E5FA7FB21AB05AB396F5C"
            };
        }
        return null;
    }
}
