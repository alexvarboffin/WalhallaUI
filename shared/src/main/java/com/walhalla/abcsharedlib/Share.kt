package com.walhalla.abcsharedlib

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.walhalla.shared.R
import com.walhalla.ui.DLog.handleException

import com.walhalla.ui.UConst


import java.io.File

object Share {
    private const val DEBUG = false
    const val comPinterestEXTRA_DESCRIPTION: String = "com.pinterest.EXTRA_DESCRIPTION"

    @JvmStatic
    val email: String = if (DEBUG) "alexvarboffin.abba11@blogger.com" else ""

    const val KEY_FILE_PROVIDER: String = ".fileprovider"


    //-> ПРИ ШАРЕ ФАЙЛА С СД_КАРД, ПИНТЕРЕСТ НЕ ПИШЕТ ДЕСКРИПТИОН
    fun shareFile(context: Context, packageName0: String, description: String, file: File) {
//        File file1 = new File("/storage/emulated/0/");
//        File[] aa = file1.listFiles();
//        for (File file2 : aa) {
//            DLog.d("#" + file2);
//        }
        //File file = new File(Environment.//@@@@@().getAbsolutePath(), filename);

        try {
            if (file.exists() && !file.isDirectory) {
            }
            val path = FileProvider.getUriForFile(context, packageName0 + KEY_FILE_PROVIDER, file)
            //            if (Build.VERSION.SDK_INT >= 23) {
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                Uri path = FileProvider.getUriForFile(this,getApplicationContext().getPackageName() +  ".FileProvider", new File(filePath + fileName));
//
//            } else{
//                Uri path =Uri.fromFile(new File(filePath + fileName))
//            }
            if (path != null) {
                shareFileUri(context, packageName0, description, path)
            }
        } catch (e: StringIndexOutOfBoundsException) {
            handleException(e)
        }
    }


    // not use /cache/ folder -> FileUriExposedException
    fun shareFileUri(context: Context, packageName0: String, description1: String, path: Uri) {
        //OR
        //Intent shareIntent = new Intent();
        //shareIntent.setAction(Intent.ACTION_SEND);
        //OR

        var description = description1
        val intent = Intent(Intent.ACTION_SEND)

        //--> intent.setType(MimeType.TEXT_PLAIN);
        if (description.trim { it <= ' ' }.isEmpty()) {
            description = ""
        }

        intent.putExtra(Intent.EXTRA_TEXT, description) // + " \n"


        //        intent.putExtra(Intent.EXTRA_TEXT, new Intent(Intent.ACTION_VIEW,
        //                Uri.parse(GOOGLE_PLAY_CONSTANT
        //                        + context.getPackageName()))
        //        );
        val type = context.contentResolver.getType(path)
        println("::TYPE:: $type $path")

        if (DEBUG) {
            //intent.putExtra(Intent.EXTRA_EMAIL, aa);
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name))

        //intent.setType(MIME_TYPE_JPEG);
        intent.setType("image/*")

        //intent.setData(path); //False To:field in gmail

        // temp permission for receiving app to read this file
        //intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION)

        //intent.setDataAndType(path, MIME_TYPE_JPEG); //Not application/octet-stream type
        intent.putExtra(Intent.EXTRA_STREAM, path)


        //OR
        //intent.putExtra(Intent.EXTRA_STREAM, path);
        //intent.setType(MIME_TYPE_JPEG);

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
        intent.putExtra("url", UConst.GOOGLE_PLAY_CONSTANT + packageName0)
        intent.putExtra(comPinterestEXTRA_DESCRIPTION, description)

        //E/DatabaseUtils: Writing exception to parcel
        //java.lang.SecurityException: Permission Denial: reading androidx.core.content.FileProvider uri content:
        //Android 11
        val resInfoList =
            context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        if (!resInfoList.isEmpty()) {
            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                context.grantUriPermission(
                    packageName,
                    path,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
            //Intent chooser = Intent.createChooser(intent, "Share File");
            val chooser = Intent.createChooser(intent, "Choose an app")
            chooser.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            context.startActivity(chooser)
        } else {
            Toast.makeText(context, R.string.no_app_available_to_share, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Gmail
     * Pinterest
     */
    @JvmStatic
    fun makeImageShare(content: String?): Intent {
        val intent = Intent(Intent.ACTION_SEND)
        //@@@ intent.setType("*/*");
        //@@@ intent.setType(MIME_TYPE_JPEG);
        //@@@ intent.setType(MimeType.TEXT_PLAIN);
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_TEXT, content)

        intent.putExtra("ru.ok.android.action.SEND_MESSAGE", content)
        intent.putExtra(comPinterestEXTRA_DESCRIPTION, content)
        if (DEBUG) {
            //intent.putExtra(Intent.EXTRA_EMAIL, email);
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        }
        return intent
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
    //                intent.setType(MimeType.TEXT_PLAIN);
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
    //                //intent.setType(MIME_TYPE_JPEG);
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
