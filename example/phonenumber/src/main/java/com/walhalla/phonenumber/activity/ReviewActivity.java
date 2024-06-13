package com.walhalla.phonenumber.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.walhalla.phonenumber.R;
import com.walhalla.ui.UConst;
import com.walhalla.ui.observer.RateAppModule;

public class ReviewActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private boolean reviewCompleted;
    private boolean sharingCompleted;
    private RateAppModule module;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewiew);
        setSupportActionBar(findViewById(R.id.toolbar));
        module = new RateAppModule(this);
        // Инициализация SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        reviewCompleted = sharedPreferences.getBoolean("review_completed", false);
        sharingCompleted = sharedPreferences.getBoolean("sharing_completed", false);

        // Обработчик нажатия на кнопку "Оставить отзыв"
        Button reviewButton = findViewById(R.id.review_button);
        reviewButton.setOnClickListener(v -> module.launchNow());

        // Обработчик нажатия на кнопку "Поделиться"
        Button shareButton = findViewById(R.id.share_button);
        shareButton.setOnClickListener(v -> shareThisApp(this,"xxx"));

        // Обновление статуса заданий
        updateQuestStatus();
    }

    // Метод для отображения диалога о выполнении задания "Поделиться"
    private void showShareDialog(ReviewActivity reviewActivity, Intent data, int resultCode) {
        Toast.makeText(reviewActivity, "###", Toast.LENGTH_SHORT).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Задание выполнено!" + resultCode)
                .setMessage("Вы успешно поделились приложением." + data)
                .setPositiveButton("OK", (dialog, which) -> {
                    sharingCompleted = true;
                    sharedPreferences.edit().putBoolean("sharing_completed", true).apply();
                    updateQuestStatus();
                })
                .show();
    }
    public void shareThisApp(AppCompatActivity context, @Nullable String message) {

        if (message == null) {
            message = context.getString(com.walhalla.ui.R.string.share_text_default)
                    + (char) 10 + UConst.GOOGLE_PLAY_CONSTANT
                    + context.getPackageName() + (char) 10;
        }


        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(com.walhalla.ui.R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        //v1
        //context.startActivityForResult(intent, Module_U.REQUEST_CODE_SHARE_APP);

        //v2
        Intent sender = Intent.createChooser(intent, "Share " + context.getString(R.string.app_name));
        context.startActivityForResult(sender, 656);
    }

    // Метод для обновления статуса заданий
    private void updateQuestStatus() {
        // Обновление статуса задания "Оставить отзыв"
        if (reviewCompleted) {
            // Отметить задание "Оставить отзыв" как выполненное
        }

        // Обновление статуса задания "Поделиться"
        if (sharingCompleted) {
            // Отметить задание "Поделиться" как выполненное
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == RateAppModule.REQUEST_CODE_MARKET) {
//            if (resultCode == RESULT_OK) {
//                // Пользователь вернулся из маркета после просмотра страницы приложения
//                // Вы можете выполнить необходимые действия здесь
//                showReviewDialog(this, data, resultCode);
//            } else if (resultCode == RESULT_CANCELED) {
//                // Пользователь вернулся из маркета без каких-либо действий
//                showReviewDialog(this, data, resultCode);
//            }
//        }

        if (resultCode == RESULT_OK) {
            // Пользователь вернулся из маркета после просмотра страницы приложения
            // Вы можете выполнить необходимые действия здесь
            showShareDialog(this, data, resultCode);
        } else if (resultCode == RESULT_CANCELED) {
            // Пользователь вернулся из маркета без каких-либо действий
            showShareDialog(this, data, resultCode);
        }
    }

    private void showReviewDialog(Context context, Intent data, int code) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Задание выполнено!" + code)
                .setMessage("Вы успешно оставили отзыв о приложении." + data)
                .setPositiveButton("OK", (dialog, which) -> {
//                    reviewCompleted = true;
//                    sharedPreferences.edit().putBoolean("review_completed", true).apply();
//                    updateQuestStatus();
                })
                .show();
    }
}
