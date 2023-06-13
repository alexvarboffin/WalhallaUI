package kgbook.ru.pcleaner.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import kgbook.ru.R;

import java.util.ArrayList;
import java.util.Random;

public class LighterView extends View {

    // Lighter image
    private Bitmap mLighterBitmap;
    private int mLighterWidth;
    private int mLighterHeight;
    private int mLighterLeft;
    private int mLighterTop;

    // Flame image
    private Bitmap mFlameBitmap;
    private int mFlameWidth;
    private int mFlameHeight;
    private int mFlameLeft;
    private int mFlameTop;
    private int mFlameRight;
    private int mFlameBottom;
    private float mFlameDeltaY;
    private float mFlameDeltaX;
    private float mFlameRotation;
    private float mFlameRotationDelta;
    private float mFlameAmplitude;

    // Spark properties
    private static class Spark {
        int x;
        int y;
        int size;
        int speed;
        int color;

        public Spark(int x, int y, int size, int speed, int color) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.speed = speed;
            this.color = color;
        }

        public void update() {
            y -= speed;
            x += speed / 2;
        }
    }

    private ArrayList<Spark> mSparks;
    private Paint mSparkPaint;
    private Rect mSparkRect;
    private int mSparkCounter;
    private int mSparkInterval;
    private int mMinSparkSize;
    private int mMaxSparkSize;
    private int mMinSparkSpeed;
    private int mMaxSparkSpeed;
    private int[] mSparkColors;
    private Random mRandom;

    public LighterView(Context context) {
        this(context, null);
    }

    public LighterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LighterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // Load the lighter and flame images
        mLighterBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lighter);
        mFlameBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flame);

        // Initialize the spark properties
        mSparks = new ArrayList<>();
        mSparkPaint = new Paint();
        mSparkPaint.setStyle(Paint.Style.FILL);
        mSparkRect = new Rect();
        mSparkCounter = 0;
        mSparkInterval = 2;
        mMinSparkSize = 4;
        mMaxSparkSize = 16;
        mMinSparkSpeed = 2;
        mMaxSparkSpeed =6;
        mSparkColors = new int[] {
                Color.parseColor("#ffffff"), // white
                Color.parseColor("#ffcc33"), // yellow
                Color.parseColor("#ff6600"), // orange
                Color.parseColor("#ff3333"), // red
        };
        mRandom = new Random();
        // Initialize the flame properties
        mFlameWidth = mFlameBitmap.getWidth();
        mFlameHeight = mFlameBitmap.getHeight();
        mFlameLeft = mLighterLeft + mLighterWidth / 2 - mFlameWidth / 2;
        mFlameTop = mLighterTop - mFlameHeight / 2;
        mFlameRight = mFlameLeft + mFlameWidth;
        mFlameBottom = mFlameTop + mFlameHeight;
        mFlameDeltaY = -3;
        mFlameDeltaX = 2;
        mFlameRotation = 0;
        mFlameRotationDelta = 1;
        mFlameAmplitude = 5;

        // Start the animation
        postInvalidateOnAnimation();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Update the position and size of the flame
        mFlameLeft += mFlameDeltaX;
        mFlameRight += mFlameDeltaX;
        mFlameTop += mFlameDeltaY;
        mFlameBottom += mFlameDeltaY;
        mFlameRotation += mFlameRotationDelta;

        if (mFlameTop < mLighterTop - mFlameHeight / 2 - mFlameAmplitude) {
            mFlameDeltaY = Math.abs(mFlameDeltaY);
            mFlameAmplitude = mRandom.nextInt(5) + 5;
        } else if (mFlameTop > mLighterTop - mFlameHeight / 2 + mFlameAmplitude) {
            mFlameDeltaY = -Math.abs(mFlameDeltaY);
            mFlameAmplitude = mRandom.nextInt(5) + 5;
        }

        // Draw the lighter and flame
        canvas.drawBitmap(mLighterBitmap, mLighterLeft, mLighterTop, null);
        canvas.save();
        canvas.rotate(mFlameRotation, mFlameLeft + mFlameWidth / 2, mFlameTop + mFlameHeight / 2);
        canvas.drawBitmap(mFlameBitmap, mFlameLeft, mFlameTop, null);
        canvas.restore();

        // Draw the sparks
        for (int i = 0; i < mSparks.size(); i++) {
            Spark spark = mSparks.get(i);
            mSparkPaint.setColor(spark.color);
            mSparkRect.set(spark.x - spark.size / 2, spark.y - spark.size / 2, spark.x + spark.size / 2, spark.y + spark.size / 2);
            canvas.drawRect(mSparkRect, mSparkPaint);
            spark.update();
        }

        // Add new sparks at random intervals
//        mSparkCounter++;
//        if (mSparkCounter >= mSparkInterval) {
//            int sparkColor = mSparkColors[mRandom.nextInt(mSparkColors.length)];
//            mSparks.add(new Spark(mFlameLeft + mRandom.nextInt(mFlameWidth),
//                    mFlameTop + mFlameHeight / 2 + mRandom.nextInt(mFlameHeight / 2),
//                    mRandom.nextInt(mMaxSparkSize - mMinSparkSize + 1) + mMinSparkSize,
//                    mRandom.nextInt(mMaxSparkSpeed - mMinSparkSpeed + 1)
//                    + mMinSparkSpeed, sparkColor));
//        }

        if (mSparkCounter < mSparkInterval) {
            mSparkCounter++;
        } else {
            mSparkCounter = 0;
            int sparkSize = mRandom.nextInt(mMaxSparkSize - mMinSparkSize) + mMinSparkSize;
            int sparkX = mRandom.nextInt(mFlameWidth - sparkSize) + mFlameLeft;
            int sparkY = mFlameTop + mFlameHeight - sparkSize;
            int sparkSpeed = mRandom.nextInt(mMaxSparkSpeed - mMinSparkSpeed) + mMinSparkSpeed;
            int sparkColor = mSparkColors[mRandom.nextInt(mSparkColors.length)];
            mSparks.add(new Spark(sparkX, sparkY, sparkSize, sparkSpeed, sparkColor));
        }


        // Remove old sparks
        for (int i = 0; i < mSparks.size(); i++) {
            Spark spark = mSparks.get(i);
            if (spark.y < mFlameTop || spark.x < mFlameLeft || spark.x > mFlameRight) {
                mSparks.remove(i);
                i--;
            }
        }

        // Schedule the next animation frame
        postInvalidateOnAnimation();
    }

    /**
     * Represents a spark that flies off from the flame.
     */
//    private static class Spark {
//        public int x;
//        public int y;
//        public int size;
//        public int speed;
//        public int color;
//
//        public Spark(int x, int y, int size, int speed, int color) {
//            this.x = x;
//            this.y = y;
//            this.size = size;
//            this.speed = speed;
//            this.color = color;
//        }
//
//        public void update() {
//            x += speed;
//            y -= speed;
//        }
//    }

}
