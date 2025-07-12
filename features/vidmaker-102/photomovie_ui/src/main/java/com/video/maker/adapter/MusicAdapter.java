package com.video.maker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.video.maker.R;
import com.video.maker.databinding.ItemMusicBinding;
import com.video.maker.model.MusicType;

import java.util.ArrayList;
import java.util.Set;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewholderMusic> {


    private final Set<Integer> unlockedItems;

    MusicAdapterListener listener;
    Context mContext;
    ArrayList<MusicType> musicTypeArrayList;
    int row_selected = -1;

    public interface MusicAdapterListener {
        void onMusicSelected(int i);
    }

    public MusicAdapter(ArrayList<MusicType> arrayList, Context context, Set<Integer> unlockedItems, MusicAdapterListener musicAdapterListener) {
        this.musicTypeArrayList = arrayList;
        this.mContext = context;
        this.listener = musicAdapterListener;
        this.row_selected = 0;
        this.unlockedItems = unlockedItems;
    }

    @NonNull
    public ViewholderMusic onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater in = LayoutInflater.from(viewGroup.getContext());
        @NonNull ItemMusicBinding binding = ItemMusicBinding.inflate(in, viewGroup, false);
        return new ViewholderMusic(binding, unlockedItems);
    }

    public void onBindViewHolder(@NonNull ViewholderMusic viewholderMusic, int position) {

        MusicType item = this.musicTypeArrayList.get(position);
        //wads
        if (viewholderMusic.isItemLocked(position)) {
            viewholderMusic.binding.premium.setVisibility(View.VISIBLE);
        } else {
            viewholderMusic.binding.premium.setVisibility(View.GONE);
        }
        if (this.row_selected == position) {
            viewholderMusic.binding.nameMusic.setText(item.getNameMusic());
//            Glide.with(this.mContext).load(Integer.valueOf(item.getImgMusic())).apply((BaseRequestOptions<?>) new RequestOptions().centerCrop()).into((ImageView) viewholderMusic.binding.imgMusic);
            Glide.with(this.mContext).load(item.getImgMusic()).into(viewholderMusic.binding.imgMusic);
            viewholderMusic.binding.imgUnChecked.setImageResource(R.drawable.ic_music_check_shape);
            return;
        }
        viewholderMusic.binding.nameMusic.setText(item.getNameMusic());
//        Glide.with(this.mContext).load(Integer.valueOf(item.getImgMusic())).apply((BaseRequestOptions<?>) new RequestOptions().centerCrop()).into((ImageView) viewholderMusic.binding.imgMusic);
        Glide.with(this.mContext).load(item.getImgMusic()).into(viewholderMusic.binding.imgMusic);
        viewholderMusic.binding.imgUnChecked.setImageResource(R.drawable.ic_music_uncheck_shape);
    }

    public int getItemCount() {
        return this.musicTypeArrayList.size();
    }

    public class ViewholderMusic extends LockedViewHolder {
        private final ItemMusicBinding binding;

        public ViewholderMusic(ItemMusicBinding binding, Set<Integer> unlockedItems) {
            super(binding.getRoot(), unlockedItems);
            this.binding = binding;

            binding.getRoot().setOnClickListener(view -> {
                MusicAdapter.this.listener.onMusicSelected(ViewholderMusic.this.getAdapterPosition());
                MusicAdapter.this.row_selected = ViewholderMusic.this.getAdapterPosition();
                MusicAdapter.this.notifyDataSetChanged();
            });
        }
    }

    public void setRowSelected(int i) {
        this.row_selected = i;
    }
}
