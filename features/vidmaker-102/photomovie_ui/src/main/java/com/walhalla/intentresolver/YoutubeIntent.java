package com.walhalla.intentresolver;


import static com.walhalla.intentresolver.YoutubeUtils.BASEAA;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import com.video.maker.BuildConfig;
import com.video.maker.activity.ShareActivity;
import com.video.maker.util.DLog;
import com.video.maker.util.ShareUtils;
import com.walhalla.TextUtilz;
import com.walhalla.sharedlib.SharedNetwork;
import com.walhalla.sharedlib.SharedObj;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class YoutubeIntent extends DefaultIntent {
//[@@@@@com.google.android.youtube]com.google.android.apps.youtube.app.application.Shell_UploadActivity@@]
//[@@@@@com.google.android.youtube]com.google.android.apps.youtube.app.extensions.upload.UploadActivity@@]

//[@@@@@com.google.android.youtube]com.google.android.youtube.api.StandalonePlayerActivity@@] █
//[@@@@@com.google.android.youtube]com.google.android.youtube.app.honeycomb.Shell$HomeActivity@@] █
//[@@@@@com.google.android.youtube]com.google.android.apps.youtube.app.application.Shell$ResultsActivity@@] █
//[@@@@@com.google.android.youtube]com.google.android.apps.youtube.app.application.Shell_MediaSearchActivity@@] █
//[@@@@@com.google.android.youtube]com.google.android.apps.youtube.app.application.Shell_LiveCreationActivity@@] █
//[@@@@@com.google.android.youtube]com.google.android.youtube.ManageNetworkUsageActivity@@] █
//[@@@@@com.google.android.youtube]com.google.android.apps.youtube.app.settings.datasaving.DataSavingSettingsActivity@@] █
//[@@@@@com.google.android.youtube]com.google.android.apps.youtube.app.settings.videoquality.VideoQualitySettingsActivity@@] █
//[@@@@@com.google.android.youtube]com.google.android.apps.youtube.app.watchwhile.WatchWhileActivity@@] █

    //    "android.intent.extra.TITLE"
//            "android.intent.extra.SUBJECT"
//            "android.intent.extra.TEXT"

    private String Shell_UploadActivity = "com.google.android.apps.youtube.app.application.Shell_UploadActivity";
    //not exist?? private String Shell_MultipleUploadsActivity = "com.google.android.apps.youtube.app.application.Shell_MultipleUploadsActivity";


    ///mnt/shared/Pictures/VideoMaker/Story_maker_2024_06_29_17_42_57.mp4
    //content://media/external/video/media/2237


//    public void shareMp4Selector(ShareActivity context, File file, Uri uri) {
//
//        sendMultiple(context, file.getAbsolutePath(), tags, title, description, uri);
//    }

    public void shareMp4Selector(ShareActivity context, File file, Uri uri, SharedObj sharedObj) {

        if (sharedObj == null || TextUtils.isEmpty(sharedObj.title)) {
            //titles
            List<String> titles0 = TextUtilz.readAllLines(context, "titles.txt");
            Random random = new Random();
            int index = random.nextInt(titles0.size());


            //resolveMp4ActivitiesForPackage(context, uri, Utils.dec0(tube));

            List<String> tags = TextUtilz.readAllLines(context, "tags.txt");
            //String title = extractTextBetween(file.getName()).replace("__", "/");
            String title = youtubetitle(titles0.get(index));

            //String simpleTitle = generateTitle(file).split("\\.")[0];
            String simpleTitle = titles0.get(index) + BASEAA;
            String description = YoutubeUtils.generateDescriptionFromTemplate(context, tags, simpleTitle);


            sendMultiple(context, file.getAbsolutePath(), tags, title, description, uri);
        } else {
            DLog.d("@@@" + uri + "@@@" + sharedObj.title + "@@" + sharedObj.description + "@@" + sharedObj.tags);
            sendMultiple(context, file.getAbsolutePath(),
                    sharedObj.tags,
                    sharedObj.title,
                    sharedObj.description,
                    uri);
        }
    }


    private void sendMultiple(Context context, String videoPath, List<String> tags, String title, String description, Uri contentUri) {
        try {
            String csYoutubePackage = ShareUtils.YOUTU;
            Intent intent1 = new Intent(Intent.ACTION_SEND_MULTIPLE);
            intent1.setPackage(csYoutubePackage);


//  No more work (          ComponentName componentName = new ComponentName(csYoutubePackage, Shell_MultipleUploadsActivity);
//  No more work (          //DLog.d("COMPONENT NAME==> " + componentName);
//  No more work (          intent1.setComponent(componentName);



            if (intent1 != null && !videoPath.isEmpty()) {

//                Uri contentUri;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    contentUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName()
//                            + KEYFILEPROVIDER, new File(videoPath));
//                } else {
//                    contentUri = Uri.fromFile(new File(videoPath));
//                }

                ArrayList<Uri> uris = new ArrayList<>();

                uris.add(contentUri);
//                uris.add(contentUri);
//                uris.add(contentUri);
//                uris.add(contentUri);
//                uris.add(contentUri);


                intent1.setType("video/*");
                intent1.putExtra(Intent.EXTRA_STREAM, uris);


                //intent1.putExtra(Intent.EXTRA_TIME, descriptions);

                //Описание
                intent1.putExtra(Intent.EXTRA_SUBJECT, description);


                //Meta Tags
                if (tags != null && !tags.isEmpty()) {
                    String eTags = String.join(",", tags);
                    intent1.putExtra(Intent.EXTRA_TEXT, eTags);
                }

                //Название
                intent1.putExtra("com.google.android.libraries.youtube.upload.extra_upload_activity_preset_title", title);

                intent1.putExtra(Intent.EXTRA_TITLE, title);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.grantUriPermission(csYoutubePackage, contentUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

                List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent1, PackageManager.MATCH_DEFAULT_ONLY);
                if (!resInfoList.isEmpty()) {
                    for (ResolveInfo resolveInfo : resInfoList) {
                        String packageName = resolveInfo.activityInfo.packageName;
                        String activityName = resolveInfo.activityInfo.name;
                        if (BuildConfig.DEBUG) {
                            DLog.d("[@@@@@" + packageName + "]" + activityName + "@@]");
                        }
                        context.grantUriPermission(packageName, contentUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                } else {
                    DLog.d("Not found activity...");
                }

                //context.startActivityForResult(intent1, COMPONENT_REQUEST_CODE);
                context.startActivity(intent1);
            } else {
                Toast.makeText(context, "Youtube app not installed!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
