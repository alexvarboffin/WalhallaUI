package com.walhalla.phonenumber;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.walhalla.ui.DLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AppIconUtils {

    public interface OnIconLoadListener {
        void onIconLoaded(Bitmap iconBitmap);
    }

    public static void loadAppIcon(Context context, String packageName, OnIconLoadListener listener) {

        File internalStorageDir = context.getFilesDir();
        String fileName = packageName + "_icon.png";
        File iconFile = new File(internalStorageDir, fileName);
        if (iconFile.exists()) {
            listener.onIconLoaded(BitmapFactory.decodeFile(iconFile.getAbsolutePath()));
        } else {
            new LoadAppIconTask(context, packageName, listener).execute();
        }
    }

    private static class LoadAppIconTask extends AsyncTask<Void, Void, Bitmap> {

        private final Context context;
        private String packageName;
        private OnIconLoadListener listener;

        public LoadAppIconTask(Context context, String packageName, OnIconLoadListener listener) {
            this.context = context;
            this.packageName = packageName;
            this.listener = listener;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            try {
                PackageManager packageManager = context.getPackageManager();
                ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, 0);
                Drawable appIcon = appInfo.loadIcon(packageManager);

                // Преобразование Drawable в Bitmap
                if (appIcon instanceof BitmapDrawable) {
                    return ((BitmapDrawable) appIcon).getBitmap();
                } else {
                    int width = appIcon.getIntrinsicWidth();
                    int height = appIcon.getIntrinsicHeight();
                    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                    appIcon.setBounds(0, 0, width, height);
                    appIcon.draw(new Canvas(bitmap));
                    return bitmap;
                }
            } catch (PackageManager.NameNotFoundException e) {
                //DLog.handleException(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap iconBitmap) {
            super.onPostExecute(iconBitmap);
            if (iconBitmap != null) {
                saveIconToInternalStorage(context, packageName, iconBitmap);
                if (listener != null) {
                    listener.onIconLoaded(iconBitmap);
                }
            }
        }
    }

    private static void saveIconToInternalStorage(Context context, String packageName, Bitmap iconBitmap) {
        File internalStorageDir = context.getFilesDir();
        String fileName = packageName + "_icon.png";
        File iconFile = new File(internalStorageDir, fileName);

        try (FileOutputStream outputStream = new FileOutputStream(iconFile)) {
            iconBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
