package com.github.wrdlbrnft.modularadapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Minimal local stand-in for com.github.wrdlbrnft:modular-adapter (JitPack unavailable).
 */
public abstract class ModularAdapter<T> extends RecyclerView.Adapter<ModularAdapter.ViewHolder<? extends T>> {

    public abstract static class ViewHolder<M> extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        protected abstract void performBind(@NonNull M item);

        public final void bind(@NonNull M item) {
            performBind(item);
        }
    }
}
