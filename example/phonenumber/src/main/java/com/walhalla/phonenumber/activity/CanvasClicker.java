package com.walhalla.phonenumber.activity;

import static androidx.databinding.DataBindingUtil.setContentView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.walhalla.phonenumber.CircleView;
import com.walhalla.phonenumber.R;

import java.util.ArrayList;
import java.util.List;

public class CanvasClicker extends AppCompatActivity {
    private CircleView container;
    private TextView counterTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.canvasactivity_main);

        container = findViewById(R.id.container);
        counterTextView = findViewById(R.id.counterTextView);

        container.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                this.drawCircle(event.getX(), event.getY());
                container.invalidate();

                updateCounter();
                return true;
            }
            return false;
        });
    }

    private void drawCircle(float x, float y) {

        float x0 = container.getWidth() / 2f;
        float y0 = container.getHeight() / 2f;
        float radius0 = Math.min(x, y);


        Canvas canvas = new Canvas();
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        float radius = 50f; // Устанавливаем радиус круга
        canvas.drawCircle(x0, y0, radius0, paint);
        canvas.drawCircle(x, y, radius, paint);
        container.draw(canvas);

        container.wraps.add(new ClickWrap((int) x, (int) y));

        if (container.wraps.size() > 10) {
            // Очищаем контейнер после 10 нажатий
            //container.removeAllViews();
            container.wraps.clear();
        }
    }

    private void updateCounter() {
        StringBuilder sb = new StringBuilder();
        sb.append("Нажатий: ").append(container.wraps.size()).append("\n");
        for (ClickWrap wrap : container.wraps) {
            sb.append(wrap.x).append("x").append(wrap.y).append("\n");
        }
        counterTextView.setText(sb.toString());
    }
}
