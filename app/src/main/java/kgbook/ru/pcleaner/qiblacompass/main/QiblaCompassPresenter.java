package kgbook.ru.pcleaner.qiblacompass.main;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class QiblaCompassPresenter implements SensorEventListener {
    private QiblaCompassView view;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;

    public QiblaCompassPresenter(QiblaCompassView view, Context context) {
        this.view = view;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    public void startListening() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    public void stopListening() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == accelerometer) {
            // Обработка данных с акселерометра
        } else if (event.sensor == magnetometer) {
            // Обработка данных с магнитометра
        }

        // Вычисление азимута и его скорректированного значения

        // Обновление пользовательского интерфейса с полученным азимутом
        float azimuth=0;
        view.updateQiblaDirection(azimuth);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Обработка изменений точности датчика, если необходимо
    }
}