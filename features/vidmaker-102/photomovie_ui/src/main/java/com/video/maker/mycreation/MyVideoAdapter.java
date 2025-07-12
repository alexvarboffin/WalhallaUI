package com.video.maker.mycreation;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.video.maker.R;
import com.video.maker.databinding.MyvideolayBinding;

import java.io.File;
import java.util.ArrayList;

public class MyVideoAdapter extends RecyclerView.Adapter<MyVideoAdapter.FileHolder> {

    ArrayList<String> videoFiles;
    Context context;
    CustomItemClickListener listener;

    public static String txtpath;

    public MyVideoAdapter(ArrayList<String> fileList, Context context, CustomItemClickListener listener) {
        this.videoFiles = fileList;
        this.context = context;
        this.listener = listener;
    }

    public static class FileHolder extends RecyclerView.ViewHolder {

        private final MyvideolayBinding binding;

        public FileHolder(MyvideolayBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public FileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyvideolayBinding binding = MyvideolayBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FileHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FileHolder holder, final int position) {
        String item = videoFiles.get(position);
        Glide.with(context)
                .load(item)
                .into(holder.binding.videoThumbIV);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(v, position));
        if (new File(item).getAbsolutePath().contains(".mp4")) {
            holder.binding.play.setVisibility(View.VISIBLE);
        } else {
            holder.binding.play.setVisibility(View.GONE);
        }
        holder.binding.share.setOnClickListener(v -> share(item));
        holder.binding.delete.setOnClickListener(v -> {
            new File(item).delete();
            delete(position);
            Toast.makeText(context, "Delete Successfully!!!", Toast.LENGTH_SHORT).show();
        });
        holder.binding.txtPath.setText(item);
        txtpath = item;
    }

    @Override
    public int getItemCount() {
        return videoFiles.size();
    }

    public void delete(int position) {
        videoFiles.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, videoFiles.size());
    }

    void share(String path) {

        MediaScannerConnection.scanFile(context, new String[]{Uri.fromFile(new File(path)).getPath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
            public void onScanCompleted(String str, Uri uri) {
                Intent intent = new Intent("android.intent.action.SEND");
                if (new File(path).getAbsolutePath().contains(".mp4")) {
                    intent.setType("video/*");
                } else {
                    intent.setType("image/*");
                }
                intent.putExtra("android.intent.extra.SUBJECT", str);
                intent.putExtra("android.intent.extra.TITLE", str);
                intent.putExtra("android.intent.extra.STREAM", uri);
                intent.putExtra("txtpath", videoFiles.get(Integer.parseInt(path)));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_this)));
            }
        });
    }
}
