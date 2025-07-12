package com.video.imagepicker.activity;

import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.View;

import com.video.imagepicker.model.ImageModel;

import java.io.File;

class GetItemAlbum extends AsyncTask<Void, Void, String> {
    private final ImagePickerActivity imagePickerActivity;

    GetItemAlbum(ImagePickerActivity imagePickerActivity) {
        this.imagePickerActivity = imagePickerActivity;
    }

    protected String doInBackground(Void... params) {
        Cursor cursor = imagePickerActivity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "bucket_display_name"}, null, null, null);
        if (cursor != null) {
            int column_index_data = cursor.getColumnIndexOrThrow("_data");
            while (cursor.moveToNext()) {
                String pathFile = cursor.getString(column_index_data);
                File file = new File(pathFile);
                if (file.exists()) {
                    boolean check = PickerUtils.checkFile(file);
                    if (!imagePickerActivity.check0(file.getParent(), imagePickerActivity.pathList) && check) {
                        imagePickerActivity.pathList.add(file.getParent());
                        imagePickerActivity.dataAlbum.add(new ImageModel(file.getParentFile().getName(), pathFile, file.getParent()));
                    }
                }
            }
            cursor.close();
        }
        return "";
    }

    protected void onPostExecute(String result) {
        imagePickerActivity.gridViewAlbum.setAdapter(imagePickerActivity.albumAdapter);
        imagePickerActivity.progress.setVisibility(View.GONE);
    }

    protected void onPreExecute() {
    }

    protected void onProgressUpdate(Void... values) {
    }
}
