package com.walhalla.landing.utility;

import android.app.Activity;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.walhalla.landing.R;


public class TooltipUtils {


    public static void tapTargetView(Activity activity) {
//        new TapTargetView(activity,
//                null,
//                null,
//                null, null);

//        TapTargetView.showFor(activity,                 // `this` is an Activity
//                TapTarget.forView(activity.findViewById(R.id.target), "This is a target", "We have the best targets, believe me")
//                        // All options below are optional
//                        .outerCircleColor(android.R.color.holo_red_dark)      // Specify a color for the outer circle
//                        .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
//                        .targetCircleColor(android.R.color.white)   // Specify a color for the target circle
//                        .titleTextSize(20)                  // Specify the size (in sp) of the title text
//                        .titleTextColor(android.R.color.white)      // Specify the color of the title text
//                        .descriptionTextSize(10)            // Specify the size (in sp) of the description text
//                        .descriptionTextColor(android.R.color.holo_red_light)  // Specify the color of the description text
//                        .textColor(android.R.color.holo_blue_bright)            // Specify a color for both the title and description text
//                        .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
//                        .dimColor(android.R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
//                        .drawShadow(true)                   // Whether to draw a drop shadow or not
//                        .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
//                        .tintTarget(true)                   // Whether to tint the target view's color
//                        .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
//                        //@@@.icon(Drawable)                     // Specify a custom drawable to draw as the target
//                        .targetRadius(60),                  // Specify the target radius (in dp)
//                new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
//                    @Override
//                    public void onTargetClick(TapTargetView view) {
//                        super.onTargetClick(view);      // This call is optional
//                        //doSomething();
//                    }
//                });


        Rect rickTarget = new Rect(0, 0, 0, 0);
        Drawable rick = activity.getDrawable(R.drawable.favorite);
        TapTargetSequence.Listener listener = new TapTargetSequence.Listener() {
            // This listener will tell us when interesting(tm) events happen in regards
            // to the sequence
            @Override
            public void onSequenceFinish() {
                // Yay
            }

            @Override
            public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
                // Perform action for the current target
            }

            @Override
            public void onSequenceCanceled(TapTarget lastTarget) {
                // Boo
            }
        };
        TapTargetSequence sequence = new TapTargetSequence(activity)
                .targets(
                        TapTarget.forView(activity.findViewById(R.id.never), "Gonna"),
                        TapTarget.forView(activity.findViewById(R.id.give), "You", "Up")
                                .dimColor(R.color.never)
                                .outerCircleColor(R.color.gonna)
                                .targetCircleColor(R.color.let)
                                .textColor(R.color.you),
                        TapTarget.forBounds(rickTarget, "Down", ":^)")
                                .cancelable(false)
                                .icon(rick)
                )
                .listener(listener);
        sequence.start();

    }
}
