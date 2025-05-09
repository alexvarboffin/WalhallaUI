package com.ratingdialog.simple;

import android.content.Context;
import android.os.Handler;

import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;

import com.walhalla.ui.R;



public class ScaleRatingBar extends TBaseRatingBar {

    private static Handler sUiHandler = new Handler();

    public ScaleRatingBar(Context context) {
        super(context);
    }

    public ScaleRatingBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ScaleRatingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void emptyRatingBar() {
        // Need to remove all previous runnable to prevent emptyRatingBar and fillRatingBar out of sync
        sUiHandler.removeCallbacksAndMessages(null);

        int delay = 0;
        for (final PartialView view : mPartialViews) {
            sUiHandler.postDelayed(view::setEmpty, delay += 5);
        }
    }

    @Override
    protected void fillRatingBar(final float rating) {
        // Need to remove all previous runnable to prevent emptyRatingBar and fillRatingBar out of sync
        sUiHandler.removeCallbacksAndMessages(null);

        int delay = 0;

        for (final PartialView partialView : mPartialViews) {
            final int ratingViewId = partialView.getId();
            final double maxIntOfRating = Math.ceil(rating);

            if (ratingViewId > maxIntOfRating) {
                partialView.setEmpty();
                continue;
            }

            sUiHandler.postDelayed(() -> {
                if (ratingViewId == maxIntOfRating) {
                    partialView.setPartialFilled(rating);
                } else {
                    partialView.setFilled();
                }

                if (ratingViewId == rating) {
                    Animation scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
                    Animation scaleDown = AnimationUtils.loadAnimation(getContext(), R.anim.scale_down);
                    partialView.startAnimation(scaleUp);
                    partialView.startAnimation(scaleDown);
                }

            }, delay += 15);
        }
    }
}

