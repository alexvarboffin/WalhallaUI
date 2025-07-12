package com.walhalla.intentresolver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import java.io.File;

public class UriUtils {
    
    public static final String KEYFILEPROVIDER = ".fileprovider";
//    ".provider"

    @SuppressLint("ObsoleteSdkInt")
    public static Uri getUriFromFile(Context context, File file) throws IllegalArgumentException {
        String APPLICATION_ID = context.getPackageName();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP || file.isDirectory()) {//Not use FileProvider is Directory
            return Uri.fromFile(file);
        } else {
            return FileProvider.getUriForFile(context, APPLICATION_ID + KEYFILEPROVIDER, file);
        }
    }
}
