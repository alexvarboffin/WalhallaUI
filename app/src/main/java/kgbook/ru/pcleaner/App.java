package kgbook.ru.pcleaner;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;

import androidx.multidex.MultiDexApplication;

import com.walhalla.ui.BuildConfig;
import com.walhalla.ui.DLog;

import java.security.MessageDigest;

public class App extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        //printHashKey(this);
    }

    public void printHashKey(Context pContext) {
        if (BuildConfig.DEBUG) {
            try {
                PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    String hashKey = new String(Base64.encode(md.digest(), 0));
                    DLog.d("printHashKey() Hash Key: " + hashKey);
                }
            } catch (Exception e) {
                DLog.handleException(e);
            }
        }
    }
}
