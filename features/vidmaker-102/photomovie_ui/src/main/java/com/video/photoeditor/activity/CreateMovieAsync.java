package com.video.photoeditor.activity;

import android.os.AsyncTask;

import com.video.maker.model.Photo;

import java.util.ArrayList;

public class CreateMovieAsync extends AsyncTask<Void, Void, Void> {

    private final Callback callback;
    private final ArrayList<Photo> listPhoto;
    ArrayList<String> sendPhotos = new ArrayList<>();

    public interface Callback {

        void postExecute(ArrayList<String> sendPhotos);
    }


    public CreateMovieAsync(Callback activity, ArrayList<Photo> listPhoto) {
        this.callback = activity;
        this.listPhoto = listPhoto;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Void doInBackground(Void... voids) {
        sendPhotos.clear();
        for (int i = 0; i < listPhoto.size(); i++) {
            sendPhotos.add(listPhoto.get(i).paths);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        callback.postExecute(sendPhotos);
    }
}
