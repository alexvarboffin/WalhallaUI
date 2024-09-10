package org.apache.mvp.presenter;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.webkit.ValueCallback;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.walhalla.ui.DLog;

import java.util.Arrays;

public abstract class BaseZPresenter {

    public static final int FILECHOOSER_RESULTCODE = 1;
    protected final AppCompatActivity activity;

    protected ValueCallback<Uri> mUploadMessage;
    protected ValueCallback<Uri[]> mUploadMessages;
    protected Uri mCapturedImageURI = null;

    public BaseZPresenter(AppCompatActivity compatActivity) {
        this.activity = compatActivity;

        DLog.d("@@@ CREATE @@@" + this.getClass().getSimpleName());
    }

    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {

        if (requestCode == FILECHOOSER_RESULTCODE) {
            DLog.d("@_file_selected_@" + data + " " + mUploadMessage + " " + mUploadMessages);
            if (null == mUploadMessage && null == mUploadMessages) {
                return;
            }
            if (null != mUploadMessage) {
                handleUploadMessage(requestCode, resultCode, data);
            } else if (mUploadMessages != null) {
                handleUploadMessages(resultCode, data);
            }
        }
    }
//api 35 content://media/picker_get_content/0/com.android.providers.media.photopicker/media/45

    private void handleUploadMessages(final int resultCode, final Intent intent) {
        DLog.d("----->>>>" + (resultCode == Activity.RESULT_CANCELED));
        Uri[] results = null;
        try {
            if (resultCode != Activity.RESULT_OK) {
                results = null;


                if (/*resultCode == Activity.RESULT_OK &&*/
                        mCapturedImageURI != null) {


                    //file:/storage/emulated/0/Android/data/rudos.ru/cache/img_20240811_202938_4605351355341258236.jpg

//                Uri[] results = new Uri[]{
//                        Uri.fromFile(new File("/storage/emulated/0/Android/data/rudos.ru/cache/img_20240811_200744_5778639102896116151.jpg"))
//                };
                    results = new Uri[]{
                            mCapturedImageURI
                    };
//            results = new Uri[]{
//                    UriUtils.getUriFromFile(this, new File("/storage/emulated/0/Pictures/img_20240811_194321_207750622564058918.jpg"))
//            };

//            results = new Uri[]{
//                    Uri.fromFile(new File("/storage/emulated/0/Pictures/img_20240811_194321_207750622564058918.jpg"))
//            };

//            results = new Uri[]{
//                    Uri.fromFile(new File("/storage/emulated/0/Android/data/rudos.ru/cache/img_20240811_200744_5778639102896116151.jpg"))
//            };
                    //content://com.android.providers.media.documents/document/image%3A3060  -- Work
                    DLog.d("@www@" + Arrays.toString(results) + "@@" + mCapturedImageURI);

                }
            } else {
                if (intent != null) {
                    String dataString = intent.getDataString();
                    ClipData clipData = intent.getClipData();
                    if (clipData != null) {
                        results = new Uri[clipData.getItemCount()];
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            ClipData.Item item = clipData.getItemAt(i);
                            results[i] = item.getUri();
                        }
                    }
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                } else {
                    //ooo
                    DLog.d("===================" + mCapturedImageURI);
                    results = new Uri[]{mCapturedImageURI};
                }
            }
        } catch (Exception e) {
            DLog.handleException(e);
        }

        //[content://com.android.providers.media.documents/document/image%3A3060,
        // content://com.android.providers.media.documents/document/image%3A3061]

        DLog.d("@@" + Arrays.toString(results));
        mUploadMessages.onReceiveValue(results);
        mUploadMessages = null;
    }

    @SuppressLint("ObsoleteSdkInt")
    private void handleUploadMessage(final int requestCode, final int resultCode, final Intent data) {
        DLog.d("@@");
        Uri result = null;
        try {
            if (resultCode != RESULT_OK) {
                result = null;
            } else {
                // retrieve from the private variable if the intent is null
                result = data == null ? mCapturedImageURI : data.getData();
            }
        } catch (Exception e) {
            DLog.handleException(e);
        }
        mUploadMessage.onReceiveValue(result);
        mUploadMessage = null;

        // code for all versions except of Lollipop
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            result = null;

            try {
                if (resultCode != RESULT_OK) {
                    result = null;
                } else {
                    // retrieve from the private variable if the intent is null
                    result = data == null ? mCapturedImageURI : data.getData();

                    //ooo
                    DLog.d("===========@@========" + mCapturedImageURI);
                }
            } catch (Exception e) {
                DLog.handleException(e);
                Toast.makeText(activity, "activity :" + e, Toast.LENGTH_LONG).show();
            }
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(result);
            }
            mUploadMessage = null;
        }

    } // end of code for all versions except of Lollipop


}
