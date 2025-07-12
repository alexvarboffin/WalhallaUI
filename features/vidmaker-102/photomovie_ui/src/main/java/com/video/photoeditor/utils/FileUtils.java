package com.video.photoeditor.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import com.video.maker.R;
import com.video.maker.util.DLog;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {


    static String safeDirName = "StoryMaker666688";

    //String path0 = Environment.getExternalStorageDirectory() + "/Download/StoryMaker";
    static String path = Environment.getExternalStorageDirectory() + "/Download/StoryMaker";

    private static final String mimeType = "audio/x-m4r";
    //private static final Uri contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    //static Uri contentUri = MediaStore.Files.getContentUri("external");

    static Uri filesContentUri = MediaStore.Files.getContentUri("external");//==>//Download, Documents

    //MediaStore.Video.Media.EXTERNAL_CONTENT_URI

    //    Music/ - для аудио файлов (музыкальные файлы).
//    Podcasts/ - для файлов подкастов.
//            Ringtones/ - для звуковых файлов рингтонов.
//    Alarms/ - для звуковых файлов будильников.
//    Notifications/ - для звуковых файлов уведомлений.
//    Audiobooks/ - для аудиокниг.
    static Uri audioContentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;


    public static Uri copyRAWtoSDCard(Context context, int resourceId, File file) throws IOException {

        String filePath = file.getPath();
        String displayName = file.getName();

//        InputStream openedRawResource = context.getResources().openRawResource(resourceId);
//        FileOutputStream outputStream = new FileOutputStream(filePath);
//        byte[] buff = new byte[1024];
//        int read;
//        try {
//            while ((read = openedRawResource.read(buff)) > 0) {
//                outputStream.write(buff, 0, read);
//            }
//            DLog.d("@@ success @@");
//        } finally {
//            openedRawResource.close();
//            outputStream.close();
//        }

        boolean fileExists = isFileExistInMediaStore(context, displayName, mimeType);
        if (!fileExists) {
            ContentResolver resolver = context.getContentResolver();
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName);
            values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
            values.put(MediaStore.Audio.Media.MIME_TYPE, mimeType);
            values.put(MediaStore.Audio.AudioColumns.TITLE, displayName);
            values.put(MediaStore.Audio.AudioColumns.DATE_ADDED, System.currentTimeMillis() / 1000);


            Uri uri;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
//                int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
//                int idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
//                int path = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);


//                values.put(MediaStore.Audio.Media.DATA, filePath);
//                values.put(MediaStore.Audio.Media.TITLE, title);
//                values.put(MediaStore.Audio.Media.ARTIST, artist);
//                values.put(MediaStore.Audio.Media.ALBUM, album);

                String fullFinalPath =
                        new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath())
                                .append(File.separator)
                                .append(Environment.DIRECTORY_MUSIC)
                                .append(File.separator)
                                .append(safeDirName)
                                .append(File.separator)
                                .append(displayName)
                                .toString();

                File parent = new File(new File(fullFinalPath).getParent());
                DLog.d("@@@" + parent);
                if (!parent.exists()) {
                    boolean b = parent.mkdirs();
                }
                values.put(MediaStore.Audio.Media.DATA, fullFinalPath);
                //указание абсолютного пути к добавляемому файлу
                //content.put(MediaStore.Audio.Media.DATA, "/sdcard/myoutputfile.mp4");

                //вставка новой строки в MediaStore
                uri = resolver.insert(audioContentUri, values);
            } else {

                //private static String relativePath = "Download/";
                //String relativePath = "Music/";
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_MUSIC);
                //values.put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath); // Adjust the path as needed

                // Get the Uri for the new file
                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                    DLog.d("mounted");
                }
                uri = resolver.insert(audioContentUri, values);
            }
            if (uri == null) {
                throw new IOException("Failed to create new MediaStore record.");
            }

            // Get the InputStream from the resource
            try (InputStream in = context.getResources().openRawResource(resourceId);
                 OutputStream out = context.getContentResolver().openOutputStream(uri)) {
                if (out == null) {
                    throw new IOException("Failed to get output stream.");
                }

                // Copy the data
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) > 0) {
                    out.write(buffer, 0, bytesRead);
                }
                DLog.d("@@ success @@ " + uri);
                return uri;
            }
        } else {
            DLog.d("@x@ exist @x@");
        }
        return null;
    }

    private static void scanFile(Context context, String[] paths) {
        MediaScannerConnection.scanFile(context,
                paths,
                null,
                (path, uri) -> {
                    // Коллбэк вызывается после завершения сканирования
                    DLog.d("!!! File scanned and added to media store: " + path);
                });
    }

    public static boolean isFileExistInMediaStore(Context context, String displayName, String mimeType) {
        ContentResolver resolver = context.getContentResolver();
//        Cursor cursor = resolver.query(
//                contentUri,
//                null,
//                MediaStore.MediaColumns.DISPLAY_NAME + "=? AND " + MediaStore.MediaColumns.RELATIVE_PATH + "=? AND " + MediaStore.MediaColumns.MIME_TYPE + "=?",
//                new String[]{displayName, relativePath, mimeType},
//                null
//        );

//        String selection = MediaStore.MediaColumns.DISPLAY_NAME + "=? AND " + MediaStore.MediaColumns.RELATIVE_PATH + "=? AND " + MediaStore.MediaColumns.MIME_TYPE + "=?";
//        String[] selectionArgs = new String[]{displayName, relativePath, mimeType};

//        String selection = MediaStore.MediaColumns.DISPLAY_NAME + "=? AND " + MediaStore.MediaColumns.RELATIVE_PATH + "=? AND " + MediaStore.MediaColumns.MIME_TYPE + "=?";
//        String[] selectionArgs = new String[]{displayName, relativePath, mimeType};


        String selection;
        String[] selectionArgs;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
//            selection = MediaStore.MediaColumns.DISPLAY_NAME + "=?";
//            selectionArgs = new String[]{displayName};

            selection = MediaStore.MediaColumns.DISPLAY_NAME + "=? AND "
                    + MediaStore.Audio.Media.DATA
                    + "=?";


            String fullFinalPath =
                    new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath())
                            .append(File.separator)
                            .append(Environment.DIRECTORY_MUSIC)
                            .append(File.separator)
                            .append(safeDirName)
                            .append(File.separator)
                            .append(displayName)
                            .toString();
            selectionArgs = new String[]{displayName, fullFinalPath};

//@@
//            selection = MediaStore.MediaColumns.DISPLAY_NAME + "=? AND " + MediaStore.MediaColumns.MIME_TYPE + "=?";
//            selectionArgs = new String[]{displayName, mimeType};

        } else {
            selection = MediaStore.MediaColumns.DISPLAY_NAME + "=? AND "
                    + MediaStore.MediaColumns.RELATIVE_PATH //Only API > 29
                    + "=?";
            selectionArgs = new String[]{displayName, "Music/"}; // Папка Music/
        }


        String[] projection = {
                MediaStore.MediaColumns._ID,
                MediaStore.MediaColumns.DISPLAY_NAME
        };

        Cursor cursor = resolver.query(
                //MediaStore.Files.getContentUri("external")
                audioContentUri,
                projection,
                selection,
                selectionArgs,
                null
        );

        // Проверяем результат запроса
        if (cursor != null && cursor.moveToFirst()) {


            // content://media/external/audio/media/54   ContentResolver Uri
            // /external/audio/media/162                 ContentResolver FileUri

            // /storage/emulated/0/Music/0 (1).m4r
            // /storage/emulated/0/Download/0.m4r


            //Android 9
            //content://media/external/audio/media/13434 /storage/emulated/0/Music/StoryMaker/7.m4r
            //Caused by: android.database.CursorIndexOutOfBoundsException: Index 0 requested, with a size of 0
            int idColumnIndex = cursor.getColumnIndex(MediaStore.MediaColumns._ID);

            int r0 = cursor.getColumnCount();
            for (int i = 0; i < r0; i++) {
                String m = cursor.getColumnName(i);
                DLog.d("@@@" + r0 + " " + idColumnIndex + " " + m);
            }

            long fileId = cursor.getLong(idColumnIndex);
            Uri fileUri = Uri.withAppendedPath(audioContentUri, String.valueOf(fileId));

            DLog.d("####" + fileUri + " " + getRealPathFromUri(context, fileUri));
            cursor.close();
            return true;
        } else {
            return false;
        }
    }

    // Метод для получения физического пути к файлу из URI

    private static String getRealPathFromUri(Context context, Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        }
        return null;
    }

    public static void dropAudioFiles(Context context) {


        List<String> result = new ArrayList<>();

        int[] iArr = {R.raw.love, R.raw.friend, R.raw.beach, R.raw.travel, R.raw.christmas, R.raw.happy, R.raw.movie, R.raw.positive, R.raw.summer};

        File file = new File(path);
        boolean m = file.mkdirs();
//        for (File listFile : file.listFiles()) {
//            listFile.delete();
//        }

        for (int i = 0; i < iArr.length; i++) {
            if (file.isDirectory()) {
                String fileName = i + ".m4r";
                File file1 = new File(path + File.separator + fileName);
                //DLog.d("@"+file1.getPath()+" = " + file1.exists() + "@@" + file1.length());
                try {
                    Uri tmp = FileUtils.copyRAWtoSDCard(context, iArr[i], file1);
                    if (tmp != null) {
                        result.add(tmp.getPath());//объявление о доступности добавленного файла
                        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, tmp));
                    }
                } catch (IOException e) {
                    DLog.handleException(e);
                }
            }
        }

        if (!result.isEmpty()) {
            String[] h = result.toArray(new String[0]);
            DLog.d("File saved to MediaStore: " + h);
            scanFile(context, h);
        }
    }

//    public static String getAudioFileUri(Context context, String displayName) {
//       return path + File.separator + displayName;
//    }

    public static Uri getAudioFileUri(Context context, String displayName) {
        ContentResolver resolver = context.getContentResolver();
//        Cursor cursor = resolver.query(
//                contentUri,
//                null,
//                MediaStore.MediaColumns.DISPLAY_NAME + "=? AND " + MediaStore.MediaColumns.RELATIVE_PATH + "=? AND " + MediaStore.MediaColumns.MIME_TYPE + "=?",
//                new String[]{displayName, relativePath, mimeType},
//                null
//        );

//        String selection = MediaStore.MediaColumns.DISPLAY_NAME + "=? AND " + MediaStore.MediaColumns.RELATIVE_PATH + "=? AND " + MediaStore.MediaColumns.MIME_TYPE + "=?";
//        String[] selectionArgs = new String[]{displayName, relativePath, mimeType};

//        String selection = MediaStore.MediaColumns.DISPLAY_NAME + "=? AND " + MediaStore.MediaColumns.RELATIVE_PATH + "=? AND " + MediaStore.MediaColumns.MIME_TYPE + "=?";
//        String[] selectionArgs = new String[]{displayName, relativePath, mimeType};


        String selection;
        String[] selectionArgs;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
//            selection = MediaStore.MediaColumns.DISPLAY_NAME + "=?";
//            selectionArgs = new String[]{displayName};

            selection = MediaStore.MediaColumns.DISPLAY_NAME + "=? AND "
                    + MediaStore.Audio.Media.DATA
                    + "=?";


            String fullFinalPath =
                    new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath())
                            .append(File.separator)
                            .append(Environment.DIRECTORY_MUSIC)
                            .append(File.separator)
                            .append(safeDirName)
                            .append(File.separator)
                            .append(displayName)
                            .toString();
            selectionArgs = new String[]{displayName, fullFinalPath};

        } else {
            selection = MediaStore.MediaColumns.DISPLAY_NAME + "=? AND "
                    + MediaStore.MediaColumns.RELATIVE_PATH //Only API > 29
                    + "=?";
            selectionArgs = new String[]{displayName, "Music/"}; // Папка Music/
        }


        String[] projection = {MediaStore.MediaColumns._ID, MediaStore.MediaColumns.DISPLAY_NAME};

        Cursor cursor = resolver.query(
                //MediaStore.Files.getContentUri("external")
                audioContentUri,
                projection,
                selection,
                selectionArgs,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID);
            long id = cursor.getLong(idColumn);
            cursor.close();
            return Uri.withAppendedPath(audioContentUri, String.valueOf(id));
        }
        if (cursor != null) {
            cursor.close();
        }
        //Failed to open file '/external/audio/media/13440'. (No such file or directory)
        return null; // Файл не найден
    }

    public static File initVideoFile(Context context) {
        File externalStoragePublicDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/VideoMaker");
        if (!externalStoragePublicDirectory.exists()) {
            boolean b = externalStoragePublicDirectory.mkdirs();
        }
        if (!externalStoragePublicDirectory.exists()) {
            externalStoragePublicDirectory = context.getCacheDir();
        }
        return new File(externalStoragePublicDirectory, String.format("Story_maker_%s.mp4", new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(Long.valueOf(System.currentTimeMillis()))));
    }
}
