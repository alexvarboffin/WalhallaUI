package com.walhalla.ui.observer;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.androidsx.rateme.OnRatingListener;

public class DefaultOnRatingListener implements OnRatingListener, Parcelable {

    public static final String TAG = "@@@";

    public DefaultOnRatingListener() {
    }

    protected DefaultOnRatingListener(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DefaultOnRatingListener> CREATOR = new Creator<DefaultOnRatingListener>() {
        @Override
        public DefaultOnRatingListener createFromParcel(Parcel in) {
            return new DefaultOnRatingListener(in);
        }

        @Override
        public DefaultOnRatingListener[] newArray(int size) {
            return new DefaultOnRatingListener[size];
        }
    };

    @Override
    public void onRating(RatingAction action, float rating) {
        Log.d(TAG, "Rating " + rating + ", after " + action);
    }
}