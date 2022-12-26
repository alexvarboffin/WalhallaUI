package com.walhalla.pcleaner;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;

import com.walhalla.ui.BuildConfig;
import com.walhalla.ui.DLog;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        printHashKey(this);
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
            } catch (NoSuchAlgorithmException e) {
                DLog.handleException(e);
            } catch (Exception e) {
                DLog.handleException(e);
            }
        }
    }
}
