package com.video.imagepicker.activity;

import static com.video.photoeditor.utils.BitmapProcess.calculateInSampleSize;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.video.imagepicker.Constants;
import com.video.imagepicker.adapters.AlbumAdapter;
import com.video.imagepicker.adapters.ListAlbumAdapter;
import com.video.imagepicker.model.ImageModel;
import com.video.imagepicker.myinterface.OnAlbum;
import com.video.imagepicker.myinterface.OnListAlbum;
import com.video.maker.R;
import com.video.maker.activities.pickedimages.PickedImagesActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ImagePickerActivity extends AppCompatActivity implements OnClickListener, OnAlbum, OnListAlbum {

    public static final String KEY_DATA_RESULT = "KEY_DATA_RESULT";
    private static final String KEY_LIMIT_MAX_IMAGE = "KEY_LIMIT_MAX_IMAGE";
    private static final String KEY_LIMIT_MIN_IMAGE = "KEY_LIMIT_MIN_IMAGE";


    public static final int PICKER_REQUEST_CODE = 1001;
    AlbumAdapter albumAdapter;
    ArrayList<ImageModel> dataAlbum = new ArrayList();
    ArrayList<ImageModel> dataListPhoto = new ArrayList();
    GridView gridViewAlbum;
    GridView gridViewListAlbum;
    HorizontalScrollView horizontalScrollView;
    LinearLayout layoutListItemSelect;
    public static int limitImageMax = 30;
    int limitImageMin = 2;
    ListAlbumAdapter listAlbumAdapter;
    public static ArrayList<ImageModel> listItemSelect;
    int pWHBtnDelete;
    int pWHItemSelected;
    ArrayList<String> pathList;
    AlertDialog sortDialog;
    TextView txtTotalImage;
    private Handler mHandler;
    private ProgressDialog pd;
    private final int position = 0;
    private static final int READ_STORAGE_CODE = 1001;
    private static final int WRITE_STORAGE_CODE = 1002;


    public static ArrayList<String> saveFiles;

    ProgressBar progress;

    GetItemAlbum getItemAlbum;
    GetItemListAlbum getItemListAlbum;

    public static Intent selectPhoto(Context context, int minImages) {
        Intent intent = new Intent(context, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.KEY_LIMIT_MAX_IMAGE, 30);
        intent.putExtra(ImagePickerActivity.KEY_LIMIT_MIN_IMAGE, minImages);
        return intent;
    }

    public void OnItemListAlbumClick(ImageModel item) {
        if (listItemSelect.size() < limitImageMax) {
            addItemSelect(item);
        } else {
            Toast.makeText(this, "Limit " + limitImageMax + " images", Toast.LENGTH_SHORT).show();
        }
    }

    ImageView btnBack;

    CardView btnDone;
    RelativeLayout header;
    LinearLayout footer;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);

        progress = findViewById(R.id.loader);
        setTV();
        saveFiles = new ArrayList<String>();

        listItemSelect = new ArrayList<>();
        pathList = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            limitImageMax = bundle.getInt(KEY_LIMIT_MAX_IMAGE, 30);
            this.limitImageMin = bundle.getInt(KEY_LIMIT_MIN_IMAGE, 2);
            if (this.limitImageMin > limitImageMax) {
                finish();
            }
            if (this.limitImageMin < 1) {
                finish();
            }
//            Log.e("ImagePickerActivity", "limitImageMin = " + this.limitImageMin);
//            Log.e("ImagePickerActivity", "limitImageMax = " + this.limitImageMax);
        }
        this.pWHItemSelected = (((int) ((((float) getDisplayInfo(this).heightPixels) / 100.0f) * 25.0f)) / 100) * 80;
        this.pWHBtnDelete = (this.pWHItemSelected / 100) * 25;

        btnBack = findViewById(R.id.back);
        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });


        header = findViewById(R.id.header);

        footer = findViewById(R.id.footer);


        this.gridViewListAlbum = findViewById(R.id.gridViewListAlbum);
        this.txtTotalImage = findViewById(R.id.txtTotalImage);

        btnDone = findViewById(R.id.btnDone);
        btnDone.setOnClickListener(this);

        this.layoutListItemSelect = findViewById(R.id.layoutListItemSelect);
        this.horizontalScrollView = findViewById(R.id.horizontalScrollView);
        this.horizontalScrollView.getLayoutParams().height = this.pWHItemSelected;
        this.gridViewAlbum = findViewById(R.id.gridViewAlbum);


        pd = new ProgressDialog(ImagePickerActivity.this);
        pd.setIndeterminate(true);
        pd.setMessage("Loading...");

        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
            }
        };

//        try {
//            Collections.sort(this.dataAlbum, new Comparator<ImageModel>() {
//                @Override
//                public int compare(ImageModel lhs, ImageModel rhs) {
//                    return lhs.getName().compareToIgnoreCase(rhs.getName());
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        this.albumAdapter = new AlbumAdapter(this, R.layout.piclist_row_album, this.dataAlbum);
        this.albumAdapter.setOnItem(this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (isPermissionGranted(Manifest.permission.READ_MEDIA_IMAGES) && isPermissionGranted(Manifest.permission.READ_MEDIA_AUDIO)) {
                getItemAlbum = new GetItemAlbum(this);
                getItemAlbum.execute();
            } else {
                requestPermission(Manifest.permission.READ_MEDIA_IMAGES, READ_STORAGE_CODE);
                requestPermission(Manifest.permission.READ_MEDIA_AUDIO, READ_STORAGE_CODE);
            }
        }
        else {
            if (isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                getItemAlbum = new GetItemAlbum(this);
                getItemAlbum.execute();
            } else {
                requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_STORAGE_CODE);
            }
        }


        updateTxtTotalImage();


    }

    public void setTV() {
//        LinearLayout adContainer = findViewById(R.id.banner_container);
//
//        if (!AdManager.isloadFbMAXAd) {
//            //admob
//            AdManager.initAd(ImagePickerActivity.this);
//            AdManager.loadBannerAd(ImagePickerActivity.this, adContainer);
//            AdManager.loadInterAd(ImagePickerActivity.this);
//        } else {
//            //MAX + Fb banner Ads
//            AdManager.initMAX(ImagePickerActivity.this);
//            AdManager.maxBannerBg(ImagePickerActivity.this, adContainer, getColor(R.color.bottom_bg_color));
//            AdManager.maxInterstital(ImagePickerActivity.this);
//        }
    }

    @Override
    public void onBackPressed() {
//        AdManager.adCounter++;
//        if (AdManager.adCounter == AdManager.adDisplayCounter) {
//            if (!AdManager.isloadFbMAXAd) {
//                AdManager.showInterAd(ImagePickerActivity.this, null, 0);
//            } else {
//                AdManager.showMaxInterstitial(ImagePickerActivity.this, null, 0);
//            }
//        } else {

        if (getItemListAlbum != null && getItemListAlbum.getStatus() == AsyncTask.Status.RUNNING) {
            getItemListAlbum.cancel(true);
        }

        if (getItemAlbum != null && getItemAlbum.getStatus() == AsyncTask.Status.RUNNING) {
            getItemAlbum.cancel(true);
        }
            if (this.gridViewListAlbum.getVisibility() == View.VISIBLE) {
                this.dataListPhoto.clear();
                this.listAlbumAdapter.notifyDataSetChanged();
                this.gridViewListAlbum.setVisibility(View.GONE);
                return;
            }
            super.onBackPressed();
//        }
    }




    private boolean isPermissionGranted(String permission) {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, permission);

        //If permission is granted returning true
        return result == PackageManager.PERMISSION_GRANTED;

        //If permission is not granted returning false
    }


    //Requesting permission
    private void requestPermission(String permission, int code) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{permission}, code);
    }


    public boolean check0(String a, ArrayList<String> list) {
        return !list.isEmpty() && list.contains(a);
    }


    void addItemSelect(final ImageModel item) {
        item.setId(listItemSelect.size());
        listItemSelect.add(item);
        updateTxtTotalImage();
        final View viewItemSelected = View.inflate(this, R.layout.piclist_item_selected, null);
        FrameLayout layoutRoot = viewItemSelected.findViewById(R.id.layoutRoot);
        ImageView imageItem = viewItemSelected.findViewById(R.id.imageItem);
        ImageView btnDelete = viewItemSelected.findViewById(R.id.btnDelete);

        imageItem.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with((Activity) this).load(item.getPathFile()).placeholder(R.drawable.piclist_icon_default).into(imageItem);


        btnDelete.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ImagePickerActivity.this.layoutListItemSelect.removeView(viewItemSelected);
                listItemSelect.remove(item);
                ImagePickerActivity.this.updateTxtTotalImage();
                listAlbumAdapter.notifyDataSetChanged();

            }
        });

        ImagePickerActivity.this.layoutListItemSelect.addView(viewItemSelected);
        viewItemSelected.startAnimation(AnimationUtils.loadAnimation(ImagePickerActivity.this, R.anim.abc_fade_in));
        ImagePickerActivity.this.sendScroll();

    }


    void updateTxtTotalImage() {
        this.txtTotalImage.setText(String.format(getResources().getString(R.string.text_images), Integer.valueOf(listItemSelect.size())));
    }

    private void sendScroll() {
        final Handler handler = new Handler();
        new Thread(() -> handler.post(() -> ImagePickerActivity.this.horizontalScrollView.fullScroll(66))).start();
    }

    void showListAlbum(String pathAlbum) {
        this.listAlbumAdapter = new ListAlbumAdapter(this, R.layout.piclist_row_list_album, this.dataListPhoto);
        this.listAlbumAdapter.setOnListAlbum(this);
        this.gridViewListAlbum.setAdapter(this.listAlbumAdapter);
        this.gridViewListAlbum.setVisibility(View.VISIBLE);
        getItemListAlbum = new GetItemListAlbum(this, pathAlbum);
        getItemListAlbum.execute();
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btnDone) {
            ArrayList<String> listString = getListString(listItemSelect);
            if (listString.size() >= this.limitImageMin) {
                // Show a progress dialog to indicate image processing
                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Processing Images...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                // Perform image processing on a background thread
                new Thread(() -> {
                    ArrayList<Bitmap> resizedImages = new ArrayList<>();
                    DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                    int targetWidth = displayMetrics.widthPixels;
                    int targetHeight = displayMetrics.heightPixels;

                    for (String imagePath : listString) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(imagePath, options);


                        options.inSampleSize = calculateInSampleSize(options, targetWidth, targetHeight);

                        options.inJustDecodeBounds = false;
                        Bitmap originalBitmap = BitmapFactory.decodeFile(imagePath, options);

                        resizedImages.add(originalBitmap);
                    }

                    // Dismiss the progress dialog on the UI thread
                    runOnUiThread(() -> {
                        progressDialog.dismiss();

                        // Perform any further actions after image processing here
                        done(resizedImages);
                    });
                }).start();
            } else {
                Toast.makeText(this, "Please select at least " + this.limitImageMin + " images", Toast.LENGTH_SHORT).show();
            }
        }

    }


    private void done(ArrayList<Bitmap> bitmapList) {
        ArrayList<String> imagePathList = new ArrayList<>();

        for (Bitmap bitmap : bitmapList) {
            // Convert Bitmap to a file path
            String imagePath = saveBitmapToFile(bitmap, "image_" + System.currentTimeMillis() + ".jpg");
            imagePathList.add(imagePath);
        }

        Intent mIntent = new Intent();
        setResult(Activity.RESULT_OK, mIntent);
        mIntent.putStringArrayListExtra(KEY_DATA_RESULT, imagePathList);
        finish();
    }

    private String saveBitmapToFile(Bitmap bitmap, String filePath) {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), filePath);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            // Compress the bitmap as a JPEG with 100% quality
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file.getAbsolutePath();
    }

    ArrayList<String> getListString(ArrayList<ImageModel> listItemSelect) {
        ArrayList<String> listString = new ArrayList();
        for (int i = 0; i < listItemSelect.size(); i++) {
            listString.add(listItemSelect.get(i).getPathFile());
        }
        return listString;
    }



    public static DisplayMetrics getDisplayInfo(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }


    public void OnItemAlbumClick(int position) {
        dataListPhoto.clear();
        showListAlbum(this.dataAlbum.get(position).getPathFolder());
    }


}
