package com.walhalla.abcsharedlib;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.walhalla.core.UConst;

import com.walhalla.ui.DLog;
import com.walhalla.ui.R;

import java.io.File;
import java.util.List;

public class Share {

    public static final String KEY_FILE_PROVIDER = ".fileprovider";

    private static final boolean DEBUG = false;
    public static final String email = DEBUG ? "alexvarboffin.abba11@blogger.com" : "";

    //@@@@@@@@@@ ПРИ ШАРЕ ФАЙЛА С СД_КАРД, ПИНТЕРЕСТ НЕ ПИШЕТ ДЕСКРИПТИОН

    public static void shareFile(Context context, String packageName0, String description, File file0) {

//        File file1 = new File("/storage/emulated/0/");
//        File[] aa = file1.listFiles();
//        for (File file2 : aa) {
//            DLog.d("#" + file2);
//        }
        //File file = new File(Environment.//@@@@@().getAbsolutePath(), filename);
        try {
            if (file0.exists() && !file0.isDirectory()) {
            }
            Uri path = FileProvider.getUriForFile(context, packageName0 + KEY_FILE_PROVIDER, file0);
//            if (Build.VERSION.SDK_INT >= 23) {
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                Uri path = FileProvider.getUriForFile(this,getApplicationContext().getPackageName() +  ".FileProvider", new File(filePath + fileName));
//
//            } else{
//                Uri path =Uri.fromFile(new File(filePath + fileName))
//            }
            if (path != null) {
                shareFileUri(context, packageName0, description, path);
            }
        } catch (StringIndexOutOfBoundsException e) {
            DLog.handleException(e);
        }
    }



    // not use /cache/ folder -> FileUriExposedException
    public static void shareFileUri(Context context, String packageName0, String description, @NonNull Uri path) {
        //OR
        //Intent shareIntent = new Intent();
        //shareIntent.setAction(Intent.ACTION_SEND);
        //OR

        Intent intent = new Intent(Intent.ACTION_SEND);
        //--> intent.setType("text/plain");

        if (description == null || description.trim().isEmpty()) {
            description = "";
        }

        intent.putExtra(Intent.EXTRA_TEXT, description);// + " \n"
        //        intent.putExtra(Intent.EXTRA_TEXT, new Intent(Intent.ACTION_VIEW,
        //                Uri.parse(GOOGLE_PLAY_CONSTANT
        //                        + context.getPackageName()))
        //        );


        String type = context.getContentResolver().getType(path);
        DLog.d("::TYPE:: " + type+" "+path);

        if (DEBUG) {
            //intent.putExtra(Intent.EXTRA_EMAIL, aa);
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));

        //intent.setType("image/jpeg");
        intent.setType("image/*");

        //intent.setData(path); //False To:field in gmail

        // temp permission for receiving app to read this file
        //intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);

        //intent.setDataAndType(path, "image/jpeg"); //Not application/octet-stream type
        intent.putExtra(Intent.EXTRA_STREAM, path);

        //OR
        //intent.putExtra(Intent.EXTRA_STREAM, path);
        //intent.setType("image/jpeg");

//                if (DEBUG) {
//                    DLog.d( "shareFile: " + intent.toString());
//                }
        //if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
//                List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

//                for (ResolveInfo resolveInfo : resInfoList) {
//                    String packageName = resolveInfo.activityInfo.packageName;
//                    context.grantUriPermission(packageName, path, Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    //}
//                }


        //More Options
        intent.putExtra("url", UConst.GOOGLE_PLAY_CONSTANT + packageName0);
        intent.putExtra("com.pinterest.EXTRA_DESCRIPTION", description);

        //Intent chooser = Intent.createChooser(intent, "Share File");
        Intent chooser = Intent.createChooser(intent, "Choose an app");


        //E/DatabaseUtils: Writing exception to parcel
        //java.lang.SecurityException: Permission Denial: reading androidx.core.content.FileProvider uri content:
        //Android 11
        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, path, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        chooser.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        context.startActivity(chooser);
    }

    /**
     * Gmail
     * Pinterest
     */
    public static Intent makeImageShare(String content) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        //@@@ intent.setType("*/*");
        //@@@ intent.setType("image/jpeg");
        //@@@ intent.setType("text/plain");
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.putExtra("com.pinterest.EXTRA_DESCRIPTION", content);
        if (DEBUG) {
            //intent.putExtra(Intent.EXTRA_EMAIL, email);
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        }
        return intent;
    }

//    private static void shareFile(Context context, String message, File file) {
//
////        File file1 = new File("/storage/emulated/0/");
////        File[] aa = file1.listFiles();
////        for (File file2 : aa) {
////            DLog.d("#" + file2);
////        }
//        try {
//            file.createNewFile();
//        } catch (IOException e) {
//            DLog.handleException(e);
//        }
//        try {
//
//            Uri contentUri = FileProvider.getUriForFile(context,
//                    BuildConfig.APPLICATION_ID + mmmm, file);
//
//            if (contentUri != null) {
//
//                //OR
//                //Intent shareIntent = new Intent();
//                //shareIntent.setAction(Intent.ACTION_SEND);
//                //OR
//
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("text/plain");
//
//                if (message == null || message.trim().isEmpty()) {
//                    message = "";
//                }
//                intent.putExtra(Intent.EXTRA_TEXT, message + " \n");
//                //        intent.putExtra(Intent.EXTRA_TEXT, new Intent(Intent.ACTION_VIEW,
//                //                Uri.parse(GOOGLE_PLAY_CONSTANT
//                //                        + context.getPackageName()))
//                //        );
//
//
//                // temp permission for receiving app to read this file
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//                String type = context.getContentResolver().getType(contentUri);
//                DLog.d("@@@@@@@@@: " + type);
//                intent.setDataAndType(contentUri, type);
//                intent.putExtra(Intent.EXTRA_STREAM, contentUri);
//
//                //OR
//                //intent.putExtra(Intent.EXTRA_STREAM, contentUri);
//                //intent.setType("image/jpeg");
//
////                if (DEBUG) {
////                    DLog.d( "shareFile: " + intent.toString());
////                }
//                context.startActivity(Intent.createChooser(intent, "Choose an app"));
//            }
//        } catch (StringIndexOutOfBoundsException e) {
//            DLog.handleException(e);
//        }
//    }
}
