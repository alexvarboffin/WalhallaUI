package com.video.maker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.video.maker.R;
import com.video.maker.databinding.ItemTransitionBinding;
import com.video.maker.model.TransferItem;

import java.util.ArrayList;
import java.util.Set;

public class TransitionsAdapter extends RecyclerView.Adapter<TransitionsAdapter.ViewHolderTransfer> {

    private final int gray;
    private final Set<Integer> unlockedItems;

    TransferAdapterListener listener;
    Context mContext;
    int row_selected = 0;
    ArrayList<TransferItem> transferItemArrayList;

    public interface TransferAdapterListener {
        void onTransferSelected(TransferItem transferItem, int position);
    }

    public TransitionsAdapter(ArrayList<TransferItem> arrayList, Context context, Set<Integer> unlockedItems, TransferAdapterListener transferAdapterListener) {
        this.transferItemArrayList = arrayList;
        this.mContext = context;
        this.unlockedItems = unlockedItems;
        this.listener = transferAdapterListener;
        gray = this.mContext.getResources().getColor(R.color.gray);
    }

    @NonNull
    public ViewHolderTransfer onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        @NonNull ItemTransitionBinding binding = ItemTransitionBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        return new ViewHolderTransfer(binding, unlockedItems);
    }

    public void onBindViewHolder(@NonNull ViewHolderTransfer holder, int position) {
        TransferItem item = this.transferItemArrayList.get(position);
        if (holder.isItemLocked(position)) {
            holder.binding.premium.setVisibility(View.VISIBLE);
        } else {
            holder.binding.premium.setVisibility(View.GONE);
        }
        if (this.row_selected == position) {
            holder.binding.imgTransfer.setImageResource(item.getImgRes());
            holder.binding.imgTransfer.setBorderWidth(4);
            holder.binding.imgTransfer.setBorderColor(this.mContext.getResources().getColor(R.color.bg_purpal));
            holder.binding.nameTransfer.setText(item.getName());
            holder.binding.nameTransfer.setTextColor(this.mContext.getResources().getColor(R.color.bg_purpal));
            return;
        }
        holder.binding.imgTransfer.setImageResource(item.getImgRes());
        holder.binding.imgTransfer.setBorderWidth(2);
        holder.binding.imgTransfer.setBorderColor(gray);
        holder.binding.nameTransfer.setText(item.getName());
        holder.binding.nameTransfer.setTextColor(gray);
    }

    public int getItemCount() {
        return this.transferItemArrayList.size();
    }

    public class ViewHolderTransfer extends LockedViewHolder {

        public final ItemTransitionBinding binding;


        public ViewHolderTransfer(ItemTransitionBinding binding, Set<Integer> unlockedItems) {
            super(binding.getRoot(), unlockedItems);
            this.binding = binding;

            binding.getRoot().setOnClickListener(view1 -> {
                if (ViewHolderTransfer.this.getAdapterPosition() < TransitionsAdapter.this.transferItemArrayList.size()) {
                    TransitionsAdapter.this.listener.onTransferSelected(TransitionsAdapter.this.transferItemArrayList.get(ViewHolderTransfer.this.getAdapterPosition()), getAdapterPosition());
                    TransitionsAdapter.this.row_selected = ViewHolderTransfer.this.getAdapterPosition();
                    TransitionsAdapter.this.notifyDataSetChanged();
                }
            });
        }
    }


}
