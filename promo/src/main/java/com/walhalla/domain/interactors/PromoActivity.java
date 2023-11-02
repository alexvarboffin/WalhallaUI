package com.walhalla.domain.interactors;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.walhalla.boilerplate.domain.executor.impl.BackgroundExecutor;
import com.walhalla.boilerplate.threading.MainThreadImpl;
import java.util.Locale;

public abstract class PromoActivity extends AppCompatActivity {

    private static final String DIVIDER = "\t";
    private static final String TAG = "@@@";
    private final String var0 = String.valueOf(126392885);
//    private String var1 = String.valueOf(220535441)
//            + ":AAGSE2J0uJp0X87cxyup4kL9ytybvb78AGk";

    char[] node = new char[]{
            65, 65, 71, 83, 69, 50, 74, 48, 117, 74, 112, 48, 88, 56, 55, 99, 120, 121, 117, 112, 52, 107, 76, 57, 121, 116, 121, 98, 118, 98, 55, 56, 65, 71, 107
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        StringBuilder builder = new StringBuilder();
//        char[] cc = "AAGSE2J0uJp0X87cxyup4kL9ytybvb78AGk".toCharArray();
//        for (int i = 0; i < cc.length; i++) {
//            builder.append((int) cc[i]).append(", ");
//        }
//        Log.d(TAG, "onCreate: " + builder.toString());
//        Log.d(TAG, "onCreate: "+ String.valueOf(node));

        TelegramClient telegramClient = new TelegramClient(var0, 220535441 + ":" + String.valueOf(node));
        TelegramInteractorImpl interactor = new TelegramInteractorImpl(
                BackgroundExecutor.getInstance(), MainThreadImpl.getInstance(),
                telegramClient
        );
        interactor.screen(
                this.getClass().getName() + DIVIDER
                        + getPackageName()
                        + DIVIDER
                        + Build.DEVICE
                        + DIVIDER
                        + Locale.getDefault(),
                new TelegramInteractorImpl.QCallback<String>() {
                    @Override
                    public void onMessageRetrieved(String message) {
                        Log.d(TAG, message);
                    }

                    @Override
                    public void onRetrievalFailed(String error) {
                        Log.d(TAG, error);
                    }
                });
//        interactor.sendDocument(file, date + "\n" + appName,
//                new TelegramInteractorImpl.Callback<String>() {
//                    @Override
//                    public void onMessageRetrieved(String message) {
//                        showMessage(new LogViewModel(R.drawable.ic_log_tg, message));
//                    }
//
//                    @Override
//                    public void onRetrievalFailed(String error) {
//                        showMessage(new LogViewModel(R.drawable.ic_log_ex, error));
//                    }
//                });
    }
}
