package com.video.maker.activity;

import static com.walhalla.sharedlib.SharedNetwork.comPinterestEXTRA_DESCRIPTION;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.video.maker.R;
import com.video.maker.databinding.ActivityShareBinding;
import com.video.maker.mycreation.MyVideoAdapter;

import com.video.maker.util.DLog;
import com.video.maker.util.ShareUtils;
import com.walhalla.intentresolver.TiktokIntent;
import com.walhalla.sharedlib.SharedNetwork;
import com.walhalla.intentresolver.LikeeIntent;
import com.walhalla.intentresolver.YoutubeIntent;
import com.walhalla.sharedlib.SharedObj;

import java.io.File;
import java.io.Serializable;

public class ShareActivity extends BaseActivity implements View.OnClickListener {

    public static final String KEY_ARG_VIDEO = "VIDEO";
    private static final String KEY_ARG_PHOTO = "PHOTO";

    TextView txtpath;
    Uri uri = null;
    private ImageView playImg;
    private ActivityShareBinding binding;
    private YoutubeIntent mm;
    private String argKeyPhoto;
    private SharedObj sharedObj;


    //private AdManager am;


//    public static Intent newIntent(Context context, File file, boolean isPhoto, SharedObj obj) {
//        Intent intent = new Intent(context, ShareActivity.class);
//        intent.putExtra(isPhoto ? KEY_ARG_PHOTO : KEY_ARG_VIDEO, Uri.fromFile(file));
//        intent.putExtra(ShareUtils.ARG_KEY_ISPHOTO, isPhoto ? ShareUtils.ARG_KEY_YES : ShareUtils.ARG_KEY_NO);
//        if (obj != null) {
//            intent.putExtra(ShareUtils.ARG_KEY_SHARE_OBJ, obj);
//        }
//        return intent;
//    }

    public static Intent newIntent(Context context, File file, boolean isPhoto) {
        Intent intent = new Intent(context, ShareActivity.class);
        intent.putExtra(isPhoto ? KEY_ARG_PHOTO : KEY_ARG_VIDEO, Uri.fromFile(file));
        intent.putExtra(ShareUtils.ARG_KEY_ISPHOTO, isPhoto ? ShareUtils.ARG_KEY_YES : ShareUtils.ARG_KEY_NO);
        return intent;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivityShareBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mm = new YoutubeIntent();


        handleIntent();

        inIt();
        loadVideoThumb();

//        am = new AdManager(getString(R.string.admob_inter_id));

        RelativeLayout rl_native_ad = findViewById(R.id.rl_native_ad);
        //MAX + Fb bidding Ads
//        am.initAD(ShareActivity.this);
//        am.createNativeAdMAX(ShareActivity.this, rl_native_ad);

        txtpath = findViewById(R.id.txt_path);
        txtpath.setText(MyVideoAdapter.txtpath);
    }

    private void handleIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(ShareUtils.ARG_KEY_ISPHOTO)) {
                argKeyPhoto = intent.getStringExtra(ShareUtils.ARG_KEY_ISPHOTO);
            }
            if (intent.hasExtra(ShareUtils.ARG_KEY_SHARE_OBJ)) {
                sharedObj = (SharedObj) intent.getSerializableExtra(ShareUtils.ARG_KEY_SHARE_OBJ);
            }
        }
        //sharedViewModel = new ViewModelProvider(this).get(SharedObj.class);
    }


    private void loadVideoThumb() {
        Uri uri2;
        if (ShareUtils.ARG_KEY_NO.equals(argKeyPhoto)) {
            uri2 = getIntent().getParcelableExtra(KEY_ARG_VIDEO);
            playImg.setVisibility(View.VISIBLE);
            if (uri2 != null) {
                binding.videoViewThumbnail.setImageBitmap(ThumbnailUtils.createVideoThumbnail(uri2.getPath(), 1));
            }
        } else {
            uri2 = getIntent().getParcelableExtra(KEY_ARG_PHOTO);
            playImg.setVisibility(View.GONE);
            Glide.with(this)
                    .load(uri2)
                    .into(binding.videoViewThumbnail);

        }
        this.uri = uri2;

    }


    private void inIt() {
        playImg = findViewById(R.id.playImg);
        binding.videoViewThumbnail.setOnClickListener(this);
        binding.btnShareMore.setOnClickListener(this);
        binding.btnInstagram.setOnClickListener(v->{
            shareVideo(getString(R.string.app_name_string), this.uri.getPath(), ShareUtils.INSTA);
        });
        binding.btnFacebook.setOnClickListener(this);
        binding.btnMessenger.setOnClickListener(this);
        binding.btnYoutube.setOnClickListener(v -> {
            shareVideo(getString(R.string.app_name_string), this.uri.getPath(), ShareUtils.YOUTU);
        });
        binding.btnGmail.setOnClickListener(this);
        binding.btnWhatsApp.setOnClickListener(this);
        binding.btnTwitter.setOnClickListener(this);
//        findViewById(R.id.btn_new.setOnClickListener(this);
        findViewById(R.id.btnBackFinal).setOnClickListener(this);

        binding.btnLikee.setOnClickListener(v -> {
            shareVideo(getString(R.string.app_name_string), this.uri.getPath(), SharedNetwork.Package.LIKEE);
        });
        binding.btnTikTok.setOnClickListener(v -> {
            shareVideo(getString(R.string.app_name_string), this.uri.getPath(),
                    SharedNetwork.Package.TIKTOK_M_PACKAGE);
        });

    }

//    @Override
//    public void onBackPressed() {
////def        Intent intent = new Intent(this, MainActivity.class);
////def        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////def        startActivity(intent);
//    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnBackFinal) {
            super.onBackPressed();
            return;
        } else if (id == R.id.btnFacebook) {
            shareVideo(getString(R.string.app_name_string), this.uri.getPath(), ShareUtils.FACE);
            return;
        } else if (id == R.id.btnGmail) {
            shareVideo(getString(R.string.app_name_string), this.uri.getPath(), ShareUtils.GMAIL);
            return;
        }else if (id == R.id.btnMessenger) {
            shareVideo(getString(R.string.app_name_string), this.uri.getPath(), ShareUtils.MESSEGER);
            return;
        } else if (id == R.id.btnShareMore) {
            shareMore(getString(R.string.app_name_string), this.uri.getPath());
            return;
        } else if (id == R.id.btnTwitter) {
            shareVideo(getString(R.string.app_name_string), this.uri.getPath(), ShareUtils.TWITTER);
            return;
        } else if (id == R.id.btnWhatsApp) {
            shareVideo(getString(R.string.app_name_string), this.uri.getPath(), ShareUtils.WHATSAPP);
            return;
        }
        //else if (id == R.id.btn_new) {
//            case R.id.btn_new:
//                Intent intent = new Intent(this, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                return;
        //}
        else if (id == R.id.videoView_thumbnail) {
            if (argKeyPhoto.equals(ShareUtils.ARG_KEY_NO)) {
                Intent intent2 = new Intent(this, VideoPlayerActivity.class);
                intent2.putExtra(KEY_ARG_VIDEO, this.uri);
                startActivity(intent2);
            }
            return;
        }
    }

    public void shareMore(final String appName, String str2) {
        MediaScannerConnection.scanFile(this, new String[]{str2}, null, new MediaScannerConnection.OnScanCompletedListener() {

            @Override
            public void onScanCompleted(String path, Uri uri) {
                Intent intent = new Intent("android.intent.action.SEND");
                if (ShareUtils.ARG_KEY_NO.equals(argKeyPhoto)) {
                    intent.setType("video/*");
                } else {
                    intent.setType("image/*");
                }
                intent.putExtra(Intent.EXTRA_SUBJECT, appName);
                intent.putExtra(Intent.EXTRA_TITLE, appName);
                intent.putExtra(comPinterestEXTRA_DESCRIPTION, appName);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                ShareActivity finalActivity = ShareActivity.this;

                Intent chooser = Intent.createChooser(intent, finalActivity.getString(R.string.share_this));
                finalActivity.startActivity(chooser);
            }
        });
    }

    public void shareVideo(final String str, String filePath, final String packageName) {
        if (isPackageInstalled(this, packageName)) {
            MediaScannerConnection.scanFile(this, new String[]{filePath}, null, new MediaScannerConnection.OnScanCompletedListener() {

                @Override
                public void onScanCompleted(String str, Uri uri) {
                    Intent intent = new Intent("android.intent.action.SEND");
                    if (ShareUtils.ARG_KEY_NO.equals(argKeyPhoto)) {
                        intent.setType("video/*");
                    } else {
                        intent.setType("image/*");
                    }
                    if (packageName.equals(ShareUtils.YOUTU)) {
                        mm.shareMp4Selector(ShareActivity.this, new File(filePath), uri, sharedObj);
                    } else if (packageName.equals(SharedNetwork.Package.LIKEE)) {
                        new LikeeIntent().shareMp4Selector(ShareActivity.this, new File(filePath), uri);
                    } else if (packageName.equals(SharedNetwork.Package.TIKTOK_M_PACKAGE)) {
                        new TiktokIntent().shareMp4Selector(ShareActivity.this, new File(filePath), uri);
                    } else {
                        intent.putExtra(Intent.EXTRA_SUBJECT, str);
                        intent.putExtra("android.intent.extra.TITLE", str);
                        intent.putExtra("android.intent.extra.STREAM", uri);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                        intent.setPackage(packageName);
                        ShareActivity.this.startActivity(intent);
                    }
                }
            });
            return;
        }
        Toast.makeText(this, getString(R.string.app_not_install), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse("market://details?id=" + packageName));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @SuppressLint("WrongConstant")
    public static boolean isPackageInstalled(Context context, String str) {
        try {
            context.getPackageManager().getPackageInfo(str, 128);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }


    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }
}
