package com.video.imagepicker.activity;

import android.os.AsyncTask;
import android.view.View;

import com.video.imagepicker.model.ImageModel;

import java.io.File;

class GetItemListAlbum extends AsyncTask<Void, Void, String> {
    private final ImagePickerActivity imagePickerActivity;
    String pathAlbum;

    GetItemListAlbum(ImagePickerActivity imagePickerActivity, String pathAlbum) {
        this.imagePickerActivity = imagePickerActivity;
        this.pathAlbum = pathAlbum;
    }

    protected String doInBackground(Void... params) {
        File file = new File(this.pathAlbum);
        if (file.isDirectory()) {
            File[] m = file.listFiles();
            if (m != null) {
                for (File fileTmp : m) {
                    if (fileTmp.exists()) {
                        boolean check = PickerUtils.checkFile(fileTmp);
                        if (!fileTmp.isDirectory() && check) {
                            imagePickerActivity.dataListPhoto.add(new ImageModel(fileTmp.getName(), fileTmp.getAbsolutePath(), fileTmp.getAbsolutePath()));
                            publishProgress();
                        }
                    }
                }
            }
        }
        return "";
    }

    protected void onPostExecute(String result) {
        imagePickerActivity.progress.setVisibility(View.GONE);
//            try {
//                Collections.sort(ImagePickerActivity.this.dataListPhoto, new Comparator<ImageModel>() {
//                    @Override
//                    public int compare(ImageModel item, ImageModel t1) {
//                        File fileI = new File(item.getPathFolder());
//                        File fileJ = new File(t1.getPathFolder());
//                        if (fileI.lastModified() > fileJ.lastModified()) {
//                            return -1;
//                        }
//                        if (fileI.lastModified() < fileJ.lastModified()) {
//                            return 1;
//                        }
//                        return 0;
//                    }
//                });
//            } catch (Exception e) {
//            }
        imagePickerActivity.listAlbumAdapter.notifyDataSetChanged();
    }

    protected void onPreExecute() {
        imagePickerActivity.progress.setVisibility(View.VISIBLE);
    }

    protected void onProgressUpdate(Void... values) {
    }
}
