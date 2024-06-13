package kgbook.ru.pcleaner.qiblacompass.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import kgbook.ru.R;

import com.walhalla.plugins.Launcher;
import com.walhalla.plugins.Module_U;
import com.walhalla.ui.DLog;

import java.util.Locale;

public class QiblaCompassActivity extends AppCompatActivity implements LocationListener {
    private ImageView compassImage;
    private TextView angleText;

    private LocationManager locationManager;
    private double userLatitude;
    private double userLongitude;
    private double kaabaLatitude = 21.4225;
    private double kaabaLongitude = 39.8262;
    private float currentAzimuth = 0.0f; // Инициализация переменной currentAzimuth
    private TextView instructionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qibla_compass);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        compassImage = findViewById(R.id.compass_image);
        angleText = findViewById(R.id.angle_text);
        instructionTextView = findViewById(R.id.instructionTextView);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Проверяем разрешение на доступ к геолокации
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Запрашиваем разрешение у пользователя
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }

        // Получаем текущие координаты пользователя
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
    }

    @Override
    public void onLocationChanged(Location location) {

        userLatitude = location.getLatitude();
        userLongitude = location.getLongitude();


        // Вычисляем азимут между текущим местоположением пользователя и координатами Каабы
        float azimuth = calculateAzimuth(userLatitude, userLongitude, kaabaLatitude, kaabaLongitude);

        DLog.d("@@@Обновляем координаты пользователя");
        DLog.d("@@@ " + azimuth + " " + userLatitude + " " + userLongitude);
        // Отображаем азимут на компасе
        rotateCompass(azimuth);
    }

    //использованием формулы Haversine
    private float calculateAzimuth(double userLat, double userLng, double kaabaLat, double kaabaLng) {
        double deltaLng = Math.toRadians(kaabaLng - userLng);

        double userLatRad = Math.toRadians(userLat);
        double kaabaLatRad = Math.toRadians(kaabaLat);

        double y = Math.sin(deltaLng) * Math.cos(kaabaLatRad);
        double x = Math.cos(userLatRad) * Math.sin(kaabaLatRad) - Math.sin(userLatRad) * Math.cos(kaabaLatRad) * Math.cos(deltaLng);

        double azimuthRad = Math.atan2(y, x);
        double azimuthDeg = Math.toDegrees(azimuthRad);

        // Преобразование азимута в диапазон от 0 до 360 градусов
        return (float) ((azimuthDeg + 360) % 360);
    }

    private void rotateCompass(float azimuth) {
        // Анимация вращения компаса
        RotateAnimation rotateAnimation = new RotateAnimation(
                -currentAzimuth, -azimuth,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setDuration(800);
        rotateAnimation.setFillAfter(true);
        compassImage.startAnimation(rotateAnimation);

        currentAzimuth = azimuth;

        // Отображение значения азимута в градусах
        angleText.setText(String.format(Locale.getDefault(), "@@@%.1f°", azimuth));

        // Определение направления Киблы на основе азимута
        String direction;
        if (azimuth >= 0 && azimuth < 90) {
            direction = "Северо-восток";
        } else if (azimuth >= 90 && azimuth < 180) {
            direction = "Юго-восток";
        } else if (azimuth >= 180 && azimuth < 270) {
            direction = "Юго-запад";
        } else {
            direction = "Северо-запад";
        }

        // Вывод инструкции пользователю
        String instruction = "Повернитесь в направлении " + direction;
        instructionTextView.setText(instruction);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

//            case R.string.start_test_again:
//                return false;

            case R.id.action_about:
                //Module_U.aboutDialog(this);
//                startActivity(new Intent(getApplicationContext(), ActivityAbout.class));
//                overridePendingTransition(R.anim.open_next, R.anim.close_main);
                return true;

            case R.id.action_privacy_policy:
                Launcher.openBrowser(this, "https://google.com");
                return true;

            case R.id.action_rate_app:
                Launcher.rateUs(this);
                return true;

            case R.id.action_share_app:
                Module_U.shareThisApp(this);
                return true;

//            case R.id.action_discover_more_app:
//                Module_U.moreApp(this);
//                return true;

//            case R.id.action_exit:
//                this.finish();
//                return true;

            case R.id.action_feedback:
                Module_U.feedback(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


        //action_how_to_use_app
        //action_support_developer

        //return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //openGPSSettings();
    }

    private void openGPSSettings() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }
}

