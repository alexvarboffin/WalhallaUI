package kgbook.ru.activity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import kgbook.ru.pcleaner.ui.LighterView;

public class MainActivity extends AppCompatActivity
        implements SensorEventListener {

    //private ImageView imageView;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float lastX, lastY, lastZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        LighterView lighterView = new LighterView(this);
//        FrameLayout frameLayout = findViewById(R.id.frameLayout);
//        frameLayout.addView(lighterView);
        LighterView lighterView = new LighterView(this);
        setContentView(lighterView);

        //setContentView(R.layout.activity_main);

        // Находим ImageView и загружаем в него изображение горящей зажигалки
//        imageView = findViewById(R.id.imageView);
//        imageView.setImageResource(R.drawable.lighter);

        // Получаем SensorManager и Sensor для работы с акселерометром
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Регистрируем слушатель SensorEventListener для получения данных с акселерометра
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Получаем данные с акселерометра
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        // Вычисляем изменение положения телефона
        float deltaX = Math.abs(lastX - x);
        float deltaY = Math.abs(lastY - y);
        float deltaZ = Math.abs(lastZ - z);

        // Если изменение было достаточно большим, поворачиваем изображение горящей зажигалки
        if (deltaX > 2 || deltaY > 2 || deltaZ > 2) {
//            float rotation = imageView.getRotation() + 5;
//            imageView.setRotation(rotation);
        }

        // Запоминаем последнее положение телефона
        lastX = x;
        lastY = y;
        lastZ = z;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Ничего не делаем здесь
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Возобновляем слушатель SensorEventListener при возобновлении активности
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Останавливаем слушатель SensorEventListener при приостановке активности
        sensorManager.unregisterListener(this);
    }
}