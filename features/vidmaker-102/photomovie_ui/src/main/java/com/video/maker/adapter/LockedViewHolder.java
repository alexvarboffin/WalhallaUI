package com.video.maker.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Set;

public abstract class LockedViewHolder extends RecyclerView.ViewHolder {


    protected final Set<Integer> unlockedItems;

    public LockedViewHolder(@NonNull View itemView, Set<Integer> unlockedItems) {
        super(itemView);
        this.unlockedItems = unlockedItems;
    }

    public boolean isItemLocked(int position) {
        return unlockedItems.contains(position);
    }
}
