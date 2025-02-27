package com.ratingdialog.simple;

import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class PartialView extends RelativeLayout {

    private ImageView mFilledView;
    private ImageView mEmptyView;

    public PartialView(Context context) {
        super(context);
        init();
    }

    public PartialView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PartialView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mFilledView = new ImageView(getContext());
        mFilledView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mEmptyView = new ImageView(getContext());
        mEmptyView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addView(mFilledView);
        addView(mEmptyView);
    }

    public void setFilledDrawable(Drawable drawable) {
        ClipDrawable clipDrawable = new ClipDrawable(drawable, Gravity.LEFT, ClipDrawable.HORIZONTAL);
        mFilledView.setImageDrawable(clipDrawable);
    }

    public void setEmptyDrawable(Drawable drawable) {
        ClipDrawable clipDrawable = new ClipDrawable(drawable, Gravity.RIGHT, ClipDrawable.HORIZONTAL);
        mEmptyView.setImageDrawable(clipDrawable);
    }

    public void setFilled() {
        mFilledView.setImageLevel(10000);
        mEmptyView.setImageLevel(0);
    }

    public void setPartialFilled(float rating) {
        float percentage = rating % 1;
        int level = (int) (10000 * percentage);
        level = level == 0 ? 10000 : level;
        mFilledView.setImageLevel(level);
        mEmptyView.setImageLevel(10000 - level);
    }

    public void setEmpty() {
        mFilledView.setImageLevel(0);
        mEmptyView.setImageLevel(10000);
    }

}
