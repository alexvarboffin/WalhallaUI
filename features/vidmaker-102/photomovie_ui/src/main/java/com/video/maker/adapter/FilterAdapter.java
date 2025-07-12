package com.video.maker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.video.maker.R;
import com.video.maker.databinding.ItemFilterBinding;
import com.video.maker.model.FilterItem;

import java.util.ArrayList;
import java.util.Set;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolderFilter> {

    private final Set<Integer> unlockedItems;
    ArrayList<FilterItem> filterArrayList;
    FilterAdapterListener listener;
    Context mContext;
    int row_selected = 0;

    public interface FilterAdapterListener {
        void onFilterSelected(FilterItem filterItem, int position);
    }

    public FilterAdapter(ArrayList<FilterItem> arrayList, Context context, Set<Integer> unlockedItems, FilterAdapterListener filterAdapterListener) {
        this.filterArrayList = arrayList;
        this.mContext = context;
        this.listener = filterAdapterListener;
        this.unlockedItems = unlockedItems;
    }

    @NonNull
    @Override
    public ViewHolderFilter onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        ItemFilterBinding binding = ItemFilterBinding.inflate(inflater, viewGroup, false);
        return new ViewHolderFilter(binding, unlockedItems);
    }

    public void onBindViewHolder(@NonNull ViewHolderFilter holder, int i) {
        //wads
        FilterItem item = this.filterArrayList.get(i);
        if (holder.isItemLocked(i)) {
            holder.binding.premium.setVisibility(View.VISIBLE);
        } else {
            holder.binding.premium.setVisibility(View.GONE);
        }

        if (this.row_selected == i) {
            Glide.with(this.mContext).load(item.getImgRes()).thumbnail(0.1f).into(holder.binding.filterImg);
            holder.binding.filterContainer.setBorderWidth(this.mContext.getResources().getDimension(R.dimen.border_width));
            holder.binding.filterContainer.setBorderColor(this.mContext.getResources().getColor(R.color.bg_purpal));
            holder.binding.filterName.setText(item.getName());
            holder.binding.filterName.setTextColor(this.mContext.getResources().getColor(R.color.bg_purpal));
            return;
        }
        Glide.with(this.mContext).load(item.getImgRes()).thumbnail(0.1f).into(holder.binding.filterImg);
        holder.binding.filterContainer.setBorderWidth(0.0f);
        holder.binding.filterContainer.setBorderColor(this.mContext.getResources().getColor(R.color.gray));
        holder.binding.filterName.setText(item.getName());
        holder.binding.filterName.setTextColor(this.mContext.getResources().getColor(R.color.gray));
    }

    public int getItemCount() {
        return this.filterArrayList.size();
    }

    public class ViewHolderFilter extends LockedViewHolder {
        private final ItemFilterBinding binding;

        public ViewHolderFilter(@NonNull ItemFilterBinding binding, Set<Integer> unlockedItems) {
            super(binding.getRoot(), unlockedItems);
            this.binding = binding;
            binding.getRoot().setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (filterArrayList.size() > position && position >= 0) {
                    if (listener != null) {
                        listener.onFilterSelected(filterArrayList.get(position), position);
                    }
                    row_selected = position;
                    notifyDataSetChanged();
                }
            });
        }
    }
}
