package com.walhalla.phonenumber.activity;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;

import com.walhalla.phonenumber.R;

public class PhoneNumberActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CALL_PHONE_PERMISSION = 990;
    private TextView phone_number;

    @RequiresApi(api = Build.VERSION_CODES.M)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#11FF01")));
            getSupportActionBar().setSubtitle("*161#\uD83D\uDCDE");
        }

        // Binding views
        phone_number = findViewById(R.id.phone_number);
        phone_number.setText("79592287622");

        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                
            }
        });

    }

    String ussdCode = "*161#";


    // Function will run after click to button
    public void GetNumber(View v) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivityForResult(new Intent("android.intent.action.CALL", Uri.parse("tel:" + ussdCode)), 1);

        } else {
            // Запрашиваем разрешение
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_CALL_PHONE_PERMISSION);
        }
//        if (ActivityCompat.checkSelfPermission(this, READ_SMS) == PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this, READ_PHONE_NUMBERS) ==
//                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
//                READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
//            // Permission check
//
//            // Create obj of TelephonyManager and ask for current telephone service
//            TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
//            @SuppressLint("HardwareIds") String phoneNumber = telephonyManager.getLine1Number();
//
//            phone_number.setText("-->>" + phoneNumber);
//            return;
//        } else {
//            // Ask for permission
//            requestPermission();
//        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requestPermissions(new String[]{READ_SMS, READ_PHONE_NUMBERS, READ_PHONE_STATE}, 100);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{READ_SMS, READ_PHONE_STATE}, 100);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_CALL_PHONE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Разрешение получено, выполняем действие
            } else {
                // Разрешение не получено, обрабатываем ошибку
            }
        } else if (requestCode == 100) {
            TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, READ_SMS) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            @SuppressLint("HardwareIds") String phoneNumber = telephonyManager.getLine1Number();

            phone_number.setText("-->>" + phoneNumber);
        } else {
            Toast.makeText(this, "Unexpected value: " + requestCode, Toast.LENGTH_SHORT).show();
        }
    }
}