package kgbook.ru.pcleaner.ui;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import kgbook.ru.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class CustomView extends SurfaceView implements SurfaceHolder.Callback {

    private DrawThread drawThread;

    public CustomView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawThread(holder);
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        drawThread.setRunning(false);
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    private class DrawThread extends Thread {

        private final SurfaceHolder surfaceHolder;
        private boolean running;

        public DrawThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
            running = true;
        }

        public void setRunning(boolean running) {
            this.running = running;
        }

        @Override
        public void run() {
            while (running) {
                Canvas canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas();
                    if (canvas != null) {
                        // draw here
                        draw(canvas);
                    }
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }

        private void draw(Canvas canvas) {
//            canvas.drawColor(Color.BLACK); // set background color
//
//            // draw lighter
//            Paint lighterPaint = new Paint();
//            lighterPaint.setColor(Color.LTGRAY);
//            lighterPaint.setStrokeWidth(8);
//            lighterPaint.setStyle(Paint.Style.STROKE);
//            canvas.drawRoundRect(new RectF(100, 100, 500, 500), 50, 50, lighterPaint);
//
//            // draw flame
//            Paint flamePaint = new Paint();
//            flamePaint.setShader(new RadialGradient(300, 300, 200,
//                    new int[]{Color.YELLOW, Color.RED, Color.TRANSPARENT},
//                    new float[]{0, 0.8f, 1}, Shader.TileMode.CLAMP));
//            canvas.drawCircle(300, 300, 200, flamePaint);
//
//            // draw sparks
//            Random random = new Random();
//            for (int i = 0; i < 50; i++) {
//                int size = random.nextInt(50) + 10;
//                int x = random.nextInt(getWidth() - size);
//                int y = random.nextInt(getHeight() - size);
//                Paint sparkPaint = new Paint();
//                sparkPaint.setColor(Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
//                canvas.drawOval(new RectF(x, y, x + size, y + size), sparkPaint);
//            }

            canvas.drawColor(Color.BLACK); // установить цвет фона

            int centerX = getWidth() / 2;
            int bottomY = getHeight() - 100;

            // рисовать зажигалку
            Paint lighterPaint = new Paint();
            lighterPaint.setColor(Color.LTGRAY);
            lighterPaint.setStrokeWidth(8);
            lighterPaint.setStyle(Paint.Style.STROKE);
            canvas.drawRoundRect(new RectF(centerX - 200, bottomY - 400, centerX + 200, bottomY), 50, 50, lighterPaint);

            // рисовать пламя
            Paint flamePaint = new Paint();
            flamePaint.setShader(new RadialGradient(centerX, bottomY - 200, 200,
                    new int[]{Color.YELLOW, Color.RED, Color.TRANSPARENT},
                    new float[]{0, 0.8f, 1}, Shader.TileMode.CLAMP));
            canvas.drawCircle(centerX, bottomY - 200, 200, flamePaint);

            // рисовать искры
            Random random = new Random();
            for (int i = 0; i < 50; i++) {
                int size = random.nextInt(50) + 10;
                int x = random.nextInt(getWidth() - size);
                int y = random.nextInt(getHeight() - size);
                Paint sparkPaint = new Paint();
                //sparkPaint.setColor(Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
                sparkPaint.setColor(Color.rgb(255, random.nextInt(200), 0));
                canvas.drawOval(new RectF(x, y, x + size, y + size), sparkPaint);
            }
        }
    }
}