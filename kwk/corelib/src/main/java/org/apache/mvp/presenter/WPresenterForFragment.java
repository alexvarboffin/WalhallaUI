package org.apache.mvp.presenter;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.appcompat.UWView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.walhalla.ui.BuildConfig;
import com.walhalla.webview.ChromeView;
import com.walhalla.webview.CustomWebViewClient;

import org.apache.cordova.domen.BodyClass;
import org.apache.cordova.repository.impl.AbstractFirebaseRepository;

import java.io.File;

public class WPresenterForFragment extends BaseZPresenter{


    private final AbstractFirebaseRepository repository1;

    protected ActivityResultLauncher<Intent> requestSelectFileLauncher0;
    protected ActivityResultLauncher<Intent> requestFileChooser;


    public WPresenterForFragment(Handler handler, AppCompatActivity compatActivity0, Fragment fragment) {
        super(compatActivity0);

        this.requestSelectFileLauncher0 = fragment.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            final int resultCode = result.getResultCode();
//                    if (resultCode == Activity.RESULT_OK) {
//                        Intent data = result.getData();
//                        defaultMarketRateCallback(data, code);
//                    } else if (code == Activity.RESULT_CANCELED) {
//                        Intent data = result.getData();
//                        defaultMarketRateCallback(data, code);
//                    } else {
//                        Intent data = result.getData();
//                        defaultMarketRateCallback(data, code);
//                    }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (mUploadMessages == null) return;
                if (result.getResultCode() == RESULT_OK) {
                }// Файлы были успешно выбраны
                Intent data = result.getData();
                Uri[] selectedFiles = WebChromeClient.FileChooserParams.parseResult(resultCode, data);
                //alert(result, selectedFiles, activity);
                mUploadMessages.onReceiveValue(selectedFiles);
                mUploadMessages = null;
            }
        });

        requestFileChooser = fragment.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            final int resultCode = result.getResultCode();
            if (null == mUploadMessage) return;
            Intent intent = result.getData();
            // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
            // Use RESULT_OK only if you're implementing WebView inside an Activity
            Uri result0 = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result0);
            mUploadMessage = null;
        });

        repository1 = new AbstractFirebaseRepository(compatActivity0) {
            @Override
            public void getConfig(Context context) {

            }
        };
    }

//    private void alert(ActivityResult result, Uri[] selectedFiles, Context context) {
//        Intent intent = result.getData();
//        StringBuilder sb = new StringBuilder();
//        if (selectedFiles != null) {
//            for (Uri uri : selectedFiles) {
//                sb.append(uri.toString()).append("\n");
//            }
//        }
//        if (intent != null) {
//            sb.append(intent);
//        }
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("debug" + ((selectedFiles == null) ? selectedFiles : selectedFiles.length))
//
//                .setMessage(sb.toString()).setPositiveButton(context.getString(android.R.string.ok), (dialog, id) -> {
//                    dialog.cancel();
//                });
//        AlertDialog alert = builder.setCancelable(false).create();
//        alert.show();
//    }

    
    
    

    private void makeFileSelector21_x(UWView view) {

//        view.setWebChromeClient(new WebChromeClient() {
//            // For 3.0+ Devices (Start)
//            // onActivityResult attached before constructor
//            private void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
//                openImageChooser(uploadMsg, acceptType);
//            }
//
//            // For Lollipop 5.0+ Devices
//            @Override
//            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
//                openImageChooser(webView, filePathCallback, fileChooserParams);
//                return true;
//            }
//
//            // openFileChooser for Android < 3.0
//
//            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
//                openFileChooser(uploadMsg, "");
//            }
//
//            //openFileChooser for other Android versions
//
//            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
//                openFileChooser(uploadMsg, acceptType);
//            }
//        });

        view.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }

            // openFileChooser for Android 3.0+

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                openImageChooser();
            }

            // For Lollipop 5.0+ Devices

            public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                mUploadMessages = filePathCallback;
                openImageChooser();
                return true;
            }

            // openFileChooser for Android < 3.0

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                openFileChooser(uploadMsg, "");
            }

            //openFileChooser for other Android versions

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                openFileChooser(uploadMsg, acceptType);
            }

            private void openImageChooser() {
                try {
                    File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "FolderName");
                    if (!imageStorageDir.exists()) {
                        imageStorageDir.mkdirs();
                    }
                    File file = new File(imageStorageDir + File.separator + "IMG_" + System.currentTimeMillis() + ".jpg");
                    mCapturedImageURI = Uri.fromFile(file);

                    final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);

                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType("image/*");
                    i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{captureIntent});
                    //activity.startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
                    requestFileChooser.launch(Intent.createChooser(chooserIntent, "File Browser"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }

    public void openImageChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        if (mUploadMessages != null) {
            mUploadMessages.onReceiveValue(null);
            mUploadMessages = null;
        }
        mUploadMessages = filePathCallback;
        Intent intent = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent = fileChooserParams.createIntent();
            if (fileChooserParams.getAcceptTypes() != null) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, fileChooserParams.getAcceptTypes());
            }
        }
        try {
            requestSelectFileLauncher0.launch(intent);
            // activity.startActivityForResult(intent, REQUEST_SELECT_FILE);
        } catch (ActivityNotFoundException e) {
            mUploadMessages = null;
            //return false;
        }
    }

    public void openImageChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        mUploadMessage = uploadMsg;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*"); //intent.setType("image/*");//"image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        //activity.startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
        requestFileChooser.launch(Intent.createChooser(intent, "File Browser"));
    }

    @SuppressLint({"SetJavaScriptEnabled", "ObsoleteSdkInt"})
    public void a123(ChromeView chromeView, UWView mView) {
        mView.getSettings().setSupportZoom(false);
        mView.getSettings().setDefaultTextEncodingName("utf-8");
        mView.getSettings().setLoadWithOverviewMode(true);
        mView.getSettings().getLoadsImagesAutomatically();
        mView.getSettings().setGeolocationEnabled(true);
        mView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mView.getSettings().setDomStorageEnabled(true);
        mView.getSettings().setBuiltInZoomControls(false);
        mView.getSettings().setJavaScriptEnabled(true);
        mView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mView.getSettings().setAllowFileAccess(true);
        mView.getSettings().setAllowContentAccess(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mView.getSettings().setAllowFileAccessFromFileURLs(true);
            mView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        }

        //"Mozilla/5.0 (Linux; Android 5.1.1; Nexus 7 Build/LMY47V) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.78 Safari/537.36 OPR/30.0.1856.93524"
        //System.getProperty("http.agent")
        String tmp = mView.getSettings().getUserAgentString();
        mView.getSettings().setUserAgentString(tmp.replace("; wv)", ")"));
        //mView.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/118.0");
        if (BuildConfig.DEBUG) {
            mView.setBackgroundColor(Color.parseColor("#80000000"));
        }
        mView.setWebViewClient(new CustomWebViewClient(mView, chromeView, activity));
        makeFileSelector21_x(mView);

        CookieManager.getInstance().setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(mView, true);
        }

//        __mView.addJavascriptInterface(
//                new MyJavascriptInterface(CompatActivity.this, __mView), "JSInterface");
//@@        mView.addJavascriptInterface(new MyJavascriptInterface(mView.getContext(), mView), "Client");
    }

    public static final int REQUEST_SELECT_FILE = 100;


//    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            if (requestCode == REQUEST_SELECT_FILE) {
//                if (uploadMessage == null) return;
//                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
//                uploadMessage = null;
//            }
//        } else if (requestCode == FILECHOOSER_RESULTCODE) {
//            if (null == mUploadMessage) return;
//            Uri result = intent == null || resultCode != Activity.RESULT_OK ? null : intent.getData();
//            mUploadMessage.onReceiveValue(result);
//            mUploadMessage = null;
//        }
//    }


    public void loadUrl(String launchUrl, UWView mWebView) {
        //DLog.d("00000000000000000000000000000000000000000" + Thread.currentThread());
        //getActivity().runOnUiThread(() -> mWebView.loadUrl(launchUrl));
        if (mWebView != null) {
            mWebView.loadUrl(launchUrl);
        }
    }

    public void event(BodyClass body) {
        repository1.event(activity, body);
    }
}
