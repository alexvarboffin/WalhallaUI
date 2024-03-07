package com.walhalla.phonenumber.apps.adapter.appitem;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.walhalla.phonenumber.R;

public class AppViewHolder extends RecyclerView.ViewHolder {

    public ImageView installedIcon;

    public TextView titleTextView;
    public TextView rateTextView;
    public TextView countTextView;

    public AppViewHolder(@NonNull View view) {
        super(view);
        titleTextView = view.findViewById(R.id.text_title);
        rateTextView = view.findViewById(R.id.text_rate);
        countTextView = view.findViewById(R.id.text_count);
        installedIcon = view.findViewById(R.id.icon);


    }
}
