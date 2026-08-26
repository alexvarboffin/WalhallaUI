package com.walhalla.phonenumber;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.walhalla.phonenumber.activity.ClickWrap;

import java.util.ArrayList;
import java.util.List;

public class CircleView extends FrameLayout {

    private Paint paint;
    private int number;

    Paint textPaint = new Paint();


    public CircleView(@NonNull Context context) {
        super(context);
        mm();
    }

    private void mm() {
        this.number = 0;
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
    }

    public CircleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mm();
    }

    public CircleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mm();
    }

    public CircleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mm();
    }


//    public void drawCircle(float x, float y) {
//        this.x999 = x;
//        this.y999 = y;
//    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        for (ClickWrap wrap : wraps) {
            float x = getWidth() * 0.05f;
            float y = getHeight() * 0.05f;
            float radius = Math.min(x, y);
            canvas.drawCircle(wrap.x, wrap.y, radius, paint);


            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(12f);
            textPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(String.valueOf(number), x, y + 15, textPaint);
        }
    }

    public List<ClickWrap> wraps = new ArrayList<>();

}