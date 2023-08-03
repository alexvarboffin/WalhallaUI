package com.walhalla.phonenumber;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.walhalla.phonenumber.dashboard.AppModel;
import com.walhalla.phonenumber.dashboard.WalhallaAppListFragment;
import com.walhalla.phonenumber.dashboard.WordModel;
import com.walhalla.ui.DLog;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    
    public static ArrayList<WordModel> oBuffers(Context context) {
        Gson gson = new Gson();
        Type listUserType = new TypeToken<List<AppModel>>() {
        }.getType();
        ArrayList<WordModel> oBuffers = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = context.getAssets().open("walhalla.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
//                AppModel obj = new AppModel();
//                obj.q = str;
//                oBuffers.add(obj);
            }
            br.close();
            //oBuffers = gson.fromJson(jsonFileString, listUserType);
            oBuffers.addAll(gson.fromJson(sb.toString(), listUserType));

        } catch (Exception e) {
            DLog.d(e.getLocalizedMessage());
        }
        return oBuffers;
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
