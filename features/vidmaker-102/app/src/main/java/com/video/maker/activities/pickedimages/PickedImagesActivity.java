package com.video.maker.activities.pickedimages;

import static com.video.photoeditor.activity.EditPhotoActivity.KEY_INTENT_PHOTO;
import static com.video.photoeditor.activity.EditPhotoActivity.KEY_INTENT_POSITION;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.video.imagepicker.activity.ImagePickerActivity;

import com.video.maker.activities.main.MainActivity;
import com.video.maker.activity.BaseActivity;

import com.video.maker.activity.VideoPreviewActivity;
import com.video.maker.util.AdManager;
import com.video.maker.util.DLog;
import com.video.makerpro.R;
import com.video.makerpro.databinding.ActivityPickedImagesBinding;
import com.video.photoeditor.activity.CreateMovieAsync;
import com.video.photoeditor.activity.EditPhotoActivity;

import com.video.maker.adapter.PhotoAdapter;
import com.video.maker.listeners.ItemClickListener;
import com.video.maker.model.Photo;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;


import java.io.File;
import java.util.ArrayList;

public class PickedImagesActivity extends BaseActivity implements View.OnClickListener,
        CreateMovieAsync.Callback {

    private static final boolean AUTOSTART_SELECTOR = true;
    //private static final int MIN_PHOTO_LIMIT_VIDEO_EDITOR = 3;
    private static final int MIN_PHOTO_LIMIT_VIDEO_EDITOR = 1;


    private RecyclerViewDragDropManager dragMgr;



    public ArrayList<Photo> listPhoto = new ArrayList<>();


    public String pathSelected;
    private Photo photo1;

    public PhotoAdapter photoAdapter;
    private ArrayList<String> photos = null;

    public Photo photoselected;

    public int positionSelected = 0;
    private ProgressDialog progressDialog;

    private ArrayList<String> selectedPhotos = new ArrayList<>();
    private ArrayList<String> sendPhotos0 = new ArrayList<>();

    private Uri uriSelected = null;
    private ActivityPickedImagesBinding binding;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivityPickedImagesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addControls();
        addRecyclerView();

        if(AUTOSTART_SELECTOR){
            addPhoto();
        }

        binding.back.setOnClickListener(view -> {
            onBackPressed();
        });

        //MAX + Fb bidding Ads
        AdManager.initAD(PickedImagesActivity.this);
        AdManager.BannerAd(PickedImagesActivity.this,
                binding.bannerContainer, getResources().getColor(R.color.bg_color));
        AdManager.LoadInterstitalAd(PickedImagesActivity.this);


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PickedImagesActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void addRecyclerView() {

        RecyclerViewDragDropManager recyclerViewDragDropManager = new RecyclerViewDragDropManager();
        this.dragMgr = recyclerViewDragDropManager;
        recyclerViewDragDropManager.setInitiateOnMove(false);
        this.dragMgr.setInitiateOnLongPress(true);
        this.binding.recyclerPhoto.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        PhotoAdapter photoAdapter2 = new PhotoAdapter(this.listPhoto, this, new ItemClickListener() {
            public void onItemClick(View view, int i) {
                PickedImagesActivity.this.positionSelected = i;
                PickedImagesActivity selectedPhotoActivity = PickedImagesActivity.this;
                selectedPhotoActivity.photoselected = selectedPhotoActivity.listPhoto.get(i);
                PickedImagesActivity selectedPhotoActivity2 = PickedImagesActivity.this;
                selectedPhotoActivity2.pathSelected = selectedPhotoActivity2.photoselected.paths;
                Glide.with(PickedImagesActivity.this).load(PickedImagesActivity.this.photoselected.paths).into(PickedImagesActivity.this.binding.imageViewPhoto);
            }

            public void onItemDeleteClick(View view, int i) {
                PickedImagesActivity.this.listPhoto.remove(i);
                PickedImagesActivity.this.photoAdapter.notifyDataSetChanged();
            }
        });
        this.photoAdapter = photoAdapter2;
        photoAdapter2.setHasStableIds(true);
        this.binding.recyclerPhoto.setAdapter(this.dragMgr.createWrappedAdapter(this.photoAdapter));
        this.dragMgr.attachRecyclerView(this.binding.recyclerPhoto);
    }


    ProgressDialog progressDialog2;


    private void addControls() {
        this.binding.btnAddMore.setOnClickListener(this);
        this.binding.btnAddPhoto.setOnClickListener(this);
        this.binding.btnEditPhoto.setOnClickListener(this);
        this.binding.btnMovie.setOnClickListener(v -> {
            this.photoAdapter.notifyDataSetChanged();
            progressDialog2 = new ProgressDialog(PickedImagesActivity.this);
            progressDialog2.setMessage(getString(R.string.com_facebook_loading));
            progressDialog2.setCanceledOnTouchOutside(false);
            progressDialog2.show();
            new CreateMovieAsync(this, listPhoto).execute();
        });
    }

    private void addPhoto() {
        //Intent intent = ImagePickerActivity.selectPhoto(this, 4);
        Intent intent = ImagePickerActivity.selectPhoto(this, 1);

        startActivityForResult(intent, ImagePickerActivity.PICKER_REQUEST_CODE);
    }

    @SuppressLint("RestrictedApi")
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1 && intent != null && i == ImagePickerActivity.PICKER_REQUEST_CODE) {
            this.selectedPhotos.clear();
            ArrayList<String> arrayList = intent.getExtras()
                    .getStringArrayList(ImagePickerActivity.KEY_DATA_RESULT);//(ArrayList) Matisse.obtainPathResult(intent);

            this.selectedPhotos = arrayList;
            if (arrayList != null && !arrayList.isEmpty()) {
                if (intent != null) {
                    this.binding.btnAddPhoto.setVisibility(View.GONE);
                    this.binding.llRecyclerView.setVisibility(View.VISIBLE);
                    this.binding.btnAddMore.setVisibility(View.VISIBLE);
                    this.binding.btnEditPhoto.setVisibility(View.VISIBLE);
                }
                for (int i3 = 0; i3 < this.selectedPhotos.size(); i3++) {
                    this.listPhoto.add(new Photo(i3, this.selectedPhotos.get(i3)));
                }
                this.photo1 = this.listPhoto.get(0);
                this.uriSelected = Uri.fromFile(new File(this.photo1.paths));
                Glide.with(this).load(this.uriSelected).into(this.binding.imageViewPhoto);
                this.photoAdapter.notifyDataSetChanged();
            }
        }
        if (i == 113 && i2 == 115) {
            this.photos = intent.getStringArrayListExtra("AFTER");
            this.selectedPhotos.clear();
            this.sendPhotos0.clear();
            this.listPhoto.clear();
            ArrayList<String> arrayList2 = this.photos;
            if (arrayList2 != null) {
                this.selectedPhotos.addAll(arrayList2);
                for (int i4 = 0; i4 < this.selectedPhotos.size(); i4++) {
                    this.listPhoto.add(new Photo(i4, this.selectedPhotos.get(i4)));
                }
            }
            this.photo1 = this.listPhoto.get(0);
            this.uriSelected = Uri.fromFile(new File(this.photo1.paths));
            Glide.with(this).load(this.uriSelected).into(this.binding.imageViewPhoto);
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnEditPhoto) {
            gotoPhotoEditor();
        } else if (id == R.id.btnAddPhoto || id == R.id.btnAddMore) {
            addPhoto();
        }
    }

    @Override
    public void postExecute(ArrayList<String> raw) {
        this.sendPhotos0 = raw;
        if (progressDialog2 != null) {
            progressDialog2.dismiss();
        }
        if (this.sendPhotos0.size() < MIN_PHOTO_LIMIT_VIDEO_EDITOR) {
            String msg = getString(R.string.more_than_3_photos);
            Toast.makeText(PickedImagesActivity.this, msg, Toast.LENGTH_SHORT).show();
        } else {

            DLog.d(String.valueOf(selectedPhotos));
            Intent intent = VideoPreviewActivity.newIntent0(this, this.sendPhotos0);
//                startActivity(intent);

            AdManager.adCounter = AdManager.adDisplayCounter;
            AdManager.showMaxInterstitial(PickedImagesActivity.this, intent, 0);
        }
    }





    public void onPostResume() {
        super.onPostResume();
    }


    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }


    public void onStop() {
        if (progressDialog2 != null && progressDialog2.isShowing()) {
            this.progressDialog.dismiss();
        }
        super.onStop();
    }


    public void gotoPhotoEditor() {
        Intent intent = new Intent(PickedImagesActivity.this, EditPhotoActivity.class);
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < this.listPhoto.size(); i++) {
            arrayList.add(this.listPhoto.get(i).paths);
        }
        intent.putStringArrayListExtra(KEY_INTENT_PHOTO, arrayList);
        intent.putExtra(KEY_INTENT_POSITION, this.positionSelected);
        if (this.positionSelected < arrayList.size()) {
            startActivityForResult(intent, 113);
        }
    }


}
