package com.walhalla.intentresolver;


import static com.walhalla.sharedlib.SharedNetwork.comPinterestEXTRA_DESCRIPTION;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.video.maker.BuildConfig;
import com.video.maker.util.DLog;
import com.walhalla.sharedlib.Share;

import java.io.File;
import java.util.List;

public class DefaultIntent implements UIntent {


    @Override
    public void shareMp4Selector(Context context, File file, Uri uri) {
        //resolveMp4ActivitiesForPackage(context, file, null);
    }

    @Override
    public boolean isClientPackage(String packageName) {
        return false;
    }

    @Override
    public void videoShare(Context context, String path) {

    }

    protected String youtubetitle(String s) {
        String title = YoutubeUtils.makeSimpleTitle(s);
        if (title.length() > 100) {
            title = title.replace("video.", "video");
        }
        if (title.length() > 100) {
            title = title.replace("#motivational", "#quotes");
        }
        if (title.length() > 100) {
            title = title.replace("#quotes", "");
        }
        if (title.length() > 100) {
            title = title.replace("#viral", "");
        }
        if (title.length() > 100) {
            title = title.replace("#shorts", "");
        }
        if (title.length() > 100) {
            title = title.replace("Motivational status video", "");
        }

        if (title.length() > 100) {
            title = title.replace(" / ", ": ").trim();
        }
        return title;
    }

    protected void resolveMp4ActivitiesForPackage(Context context, File file, String packageName, Uri uri) {
        if (file.exists()) {
            try {
                String title = "Sharing Video" + ": " + file.getAbsolutePath();
//                intent.setType("video/mp4");
//
//                //Uri uri = getUriFromFile(file);
//                //intent.setDataAndType(uri, "video/mp4");
//
//                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(localVideo.path));
//                intent.putExtra(Intent.EXTRA_SUBJECT, this.getString(R.string.SharingVideoSubject));
//                intent.putExtra(Intent.EXTRA_TEXT, this.getString(R.string.SharingVideoBody) + "\n");
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                this.startActivityForResult(Intent.createChooser(intent, title), REQUEST_SHARE);


                try {
                    //Tiktok doc
                    //Uri uri = UriUtils.getUriFromFile(context, file);
                    Intent shareIntent = makeVideoShare("Sharing Video File");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

                    //@@@
                    //shareIntent.putExtra(Intent.EXTRA_SUBJECT, this.getString(R.string.SharingVideoBody));

                    shareIntent.addFlags(/*Intent.FLAG_ACTIVITY_NEW_TASK |*/ Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    if (packageName == null) {
                        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(shareIntent, PackageManager.MATCH_DEFAULT_ONLY);
                        if (!resInfoList.isEmpty()) {
                            for (ResolveInfo resolveInfo : resInfoList) {
                                String name = resolveInfo.activityInfo.packageName;
                                String activityName = resolveInfo.activityInfo.name;
                                if (com.video.maker.BuildConfig.DEBUG) {
                                    com.video.maker.util.DLog.d("[@@@@@" + name + "]" + activityName + "@@]");
                                }
                                context.grantUriPermission(name, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            }
                        } else {
                            DLog.d("Not found activity...");
                        }
                        Intent chozzer = Intent.createChooser(shareIntent, title);
                        context.startActivity(chozzer);
                    } else {
                        context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        shareIntent.setPackage(packageName);
                        context.startActivity(shareIntent);
                    }
                } catch (IllegalArgumentException r) {
                    DLog.handleException(r);
                    //return Uri.parse(localVideo.path);
                    //return Uri.fromFile(file);
                }
            } catch (ActivityNotFoundException e) {
                //iUtils.ShowToast(context, "Something went wrong while sharing video! Please try again ");
                //@@@    makeToaster(R.string.abc_err_something_wrong_sharing);
                DLog.handleException(e);
            }
        }
    }

    public static Intent makeVideoShare(String content) {
        return makeVideoShare(content, "video/*");
    }

    public static Intent makeVideoShare(String content, String type) {

        //Intent intent = new Intent(Intent.ACTION_SEND);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);

        //@@@ intent.setType("*/*");
        //@@@ intent.setType(MIME_TYPE_JPEG);
        //@@@ intent.setType("text/plain");
        //intent.setType("image/*");//<================
        //==>intent.setType("*/*");//<================

        intent.setType(type);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.putExtra(comPinterestEXTRA_DESCRIPTION, content);
        if (BuildConfig.DEBUG) {
            //intent.putExtra(Intent.EXTRA_EMAIL, email);
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{Share.email});
        }
        return intent;
    }
}
