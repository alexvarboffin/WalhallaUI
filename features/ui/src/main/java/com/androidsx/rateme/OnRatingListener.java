package com.androidsx.rateme;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public interface OnRatingListener extends Parcelable {

    /**
     * Different actions that the user can take.
     */
    enum RatingAction {

        /** We took them to Google Play. Typically after a good rating. */
        HIGH_RATING_WENT_TO_GOOGLE_PLAY,

        /** After a negative rating, he accepted to give us some feedback. */
        LOW_RATING_GAVE_FEEDBACK,

        /** After a negative rating, he didn't give us some feedback. */
        LOW_RATING_REFUSED_TO_GIVE_FEEDBACK,

        /**
         * Gave a negative rating. Providing feedback is not configured, so the user gave the rating
         * and left.
         */
        LOW_RATING,

        /**
         * Dismissed the dialog with the cross in the upper-right corner. Note that we do NOT track
         * if the user dismisses the dialog through the back button.
         */
        DISMISSED_WITH_CROSS,

        /** Shared the link to the app through the button in the top-right corner. */
        SHARED_APP
    }

    /**
     * Arbitrary logic to execute after the user performed their action. Typically, keep track
     * of it for analytics purposes.
     *
     * @param action the action the user performed, among all possible ones
     * @param rating only meaningful for some of the actions
     */
    void onRating(RatingAction action, float rating);
}
