package com.walhalla.intentresolver;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;


import com.video.maker.util.DLog;
import com.walhalla.TextUtilz;
import com.walhalla.sharedlib.SharedNetwork;

import java.io.File;
import java.util.List;
import java.util.Random;

public class TiktokIntent extends DefaultIntent {

    @Override
    public void shareMp4Selector(Context context, File file, Uri uri) {
        //titles
        List<String> titles0 = TextUtilz.readAllLines(context, "titles.txt");
        Random random = new Random();
        int index = random.nextInt(titles0.size());

        //resolveMp4ActivitiesForPackage(context, uri, Utils.dec0(tube));

        List<String> tags = TextUtilz.readAllLines(context, "tags.txt");
        //String title = extractTextBetween(file.getName()).replace("__", "/");
        String title = youtubetitle(titles0.get(index));

        //String simpleTitle = generateTitle(file).split("\\.")[0];
        String simpleTitle = titles0.get(index) + YoutubeUtils.BASEAA;
        String description = YoutubeUtils.generateDescriptionFromTemplate(context, tags, simpleTitle);
        DLog.d("@@@" + uri);

        //sendMultiple(context, file.getAbsolutePath(), tags, title, description, uri);
        resolveMp4ActivitiesForPackage0(context, file, tags, title, description, uri);
    }

    private void resolveMp4ActivitiesForPackage0(Context context, File file, List<String> tags, String title, String description, Uri uri) {
        if (file.exists()) {
            try {
                //String title = "Sharing Video" + ": " + file.getAbsolutePath();
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
                    shareIntent.putExtra("from_deeplink", true);
                    shareIntent.putExtra("EXTRA_EXCULUDE", description);
                    //intent1.putExtra(Intent.EXTRA_TIME, descriptions);

                    //Описание
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, description);


                    //Meta Tags
                    String eTags = String.join(",", tags);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, eTags);


                    shareIntent.putExtra(Intent.EXTRA_TITLE, "title 1 aaaaaaaaaaa aaaaaaaaaaaa");
                    shareIntent.putExtra("key_video_caption", "title 1 aaaaaaaaaaa aaaaaaaaaaaa");
                    shareIntent.putExtra("title_path", "title 1 aaaaaaaaaaa aaaaaaaaaaaa");

                    //@@@
                    //shareIntent.putExtra(Intent.EXTRA_SUBJECT, this.getString(R.string.SharingVideoBody));

                    shareIntent.addFlags(/*Intent.FLAG_ACTIVITY_NEW_TASK |*/ Intent.FLAG_GRANT_READ_URI_PERMISSION);

//                    if (packageName == null) {
//                        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(shareIntent, PackageManager.MATCH_DEFAULT_ONLY);
//                        if (!resInfoList.isEmpty()) {
//                            for (ResolveInfo resolveInfo : resInfoList) {
//                                String name = resolveInfo.activityInfo.packageName;
//                                String activityName = resolveInfo.activityInfo.name;
//                                if (com.video.maker.BuildConfig.DEBUG) {
//                                    com.video.maker.util.DLog.d("[@@@@@" + name + "]" + activityName + "@@]");
//                                }
//                                context.grantUriPermission(name, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                            }
//                        } else {
//                            DLog.d("Not found activity...");
//                        }
//                        Intent chozzer = Intent.createChooser(shareIntent, title);
//                        context.startActivity(chozzer);
//                    } else {
                    context.grantUriPermission(SharedNetwork.Package.TIKTOK_M_PACKAGE, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    shareIntent.setPackage(SharedNetwork.Package.TIKTOK_M_PACKAGE);
                    context.startActivity(shareIntent);
//                    }
                } catch (IllegalArgumentException r) {
                    DLog.handleException(r);
                    //return Uri.parse(localVideo.path);
                    //return Uri.fromFile(file);
                }
            } catch (ActivityNotFoundException e) {
                //iUtils.ShowToast(context, "Something went wrong while sharing video! Please try again ");
                //@@@    makeToaster(R.string.abc_err_something_wrong_sharing);
                Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
