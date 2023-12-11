package tk.alexapp.freestuffandcoupons.activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import tk.alexapp.freestuffandcoupons.R;

public class MediaStoreDownloads extends AppCompatActivity {
    String fileName = "example0009999.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);


        findViewById(R.id.button).setOnClickListener(v -> writeFS());
        findViewById(R.id.button2).setOnClickListener(v -> readFS(fileName));
    }

    private void writeFS() {
        String textToWrite = "{!!" + System.currentTimeMillis()+"}";


        Uri downloadsUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Downloads.DISPLAY_NAME + "=?";
        String[] selectionArgs = new String[]{fileName};

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(downloadsUri, null, selection, selectionArgs, null);

        if (cursor != null && cursor.moveToFirst()) {
            long fileId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Downloads._ID));
            Uri fileUri = Uri.withAppendedPath(downloadsUri, String.valueOf(fileId));

            try {
                OutputStream outputStream = contentResolver.openOutputStream(fileUri, "w");
                if (outputStream != null) {
                    outputStream.write(textToWrite.getBytes());
                    outputStream.close();
                    Toast.makeText(this, "Файл успешно обновлен.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("FileUpdate", "Ошибка обновления файла: " + e.getMessage());
            } finally {
                cursor.close();
            }
        } else {
            Log.e("FileUpdate", "Файл для обновления не найден в директории загрузок.");
            createFile0(fileName, "====================================");
        }
    }

    private void createFile0(String fileName, String textToWrite) {
        ContentResolver resolver = getContentResolver();

        // Создаем контейнер для нового файла
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Downloads.DISPLAY_NAME, fileName);
        contentValues.put(MediaStore.Downloads.MIME_TYPE, "text/plain");
        contentValues.put(MediaStore.Downloads.IS_PENDING, 1); // Устанавливаем IS_PENDING, чтобы файл был создан как временный

        Uri contentUri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);

        try {
            if (contentUri != null) {
                OutputStream outputStream = resolver.openOutputStream(contentUri);
                if (outputStream != null) {
                    outputStream.write(textToWrite.getBytes());
                    outputStream.close();

                    // Устанавливаем IS_PENDING обратно на 0, чтобы файл стал не временным
                    contentValues.clear();
                    contentValues.put(MediaStore.Downloads.IS_PENDING, 0);
                    resolver.update(contentUri, contentValues, null, null);

                    Toast.makeText(this, "Файл успешно записан.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("FileWrite", "Не удалось создать файл в директории загрузок.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("FileWrite", "Ошибка записи файла: " + e.getMessage());
        }
    }

    private void readFS(String fileName0) {
        // URI для запроса файлов из директории загрузок
        Uri downloadsUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;

        // Поля, которые вы хотите получить (в данном случае, путь к файлу)
        String[] projection = {MediaStore.Downloads._ID, MediaStore.Downloads.DISPLAY_NAME};

        // Запрос к MediaStore
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(downloadsUri, projection, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String fileName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Downloads.DISPLAY_NAME));
                if (fileName0.equals(fileName)) {
                    long fileId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Downloads._ID));
                    Uri fileUri = Uri.withAppendedPath(downloadsUri, String.valueOf(fileId));

                    try {
                        InputStream inputStream = contentResolver.openInputStream(fileUri);
                        if (inputStream != null) {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                            StringBuilder stringBuilder = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                stringBuilder.append(line).append("\n");
                            }
                            inputStream.close();
                            Log.d("FileRead", "Содержимое файла: " + stringBuilder.toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("FileRead", "Ошибка чтения файла: " + e.getMessage());
                    }
                }
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            Log.e("FileRead", "Нет файлов в директории загрузок");
        }
    }
}
