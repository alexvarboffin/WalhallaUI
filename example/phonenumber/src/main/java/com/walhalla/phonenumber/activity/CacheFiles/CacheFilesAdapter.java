package com.walhalla.phonenumber.activity.CacheFiles;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.walhalla.ui.DLog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CacheFilesAdapter extends RecyclerView.Adapter<CacheFilesAdapter.ViewHolder> {

    private final List<String> fileList;
    private final LayoutInflater inflater;

    public CacheFilesAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.fileList = new ArrayList<>();

        File filesDir = context.getFilesDir();
        aaa("getFilesDir", context, filesDir);

        File filesDi0r = context.getCacheDir();
        aaa("getCacheDir", context, filesDi0r);



        File cacheDir = context.getExternalCacheDir();
        aaa("getExternalCacheDir",context, cacheDir);

        File mm = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        aaa("getExternalFilesDir", context, mm);

        aaa("", context, new File(mm, "1.apk"));
        aaa("", context, new File(cacheDir, "1.apk"));

        aaa("", context, new File("/data/local/tmp/perfd/libjvmtiagent_arm.so"));
        aaa("", context, new File("/data/local/tmp/profileable_reporter.tmp"));
        aaa("", context, new File("/data/local/tmp/sky.com.example.webviewflutter.sha1"));

        aaa("", context, new File("/data/local/tmp/external/1.txt"));


    }


    private void aaa(String s, Context context, File mm) {
        String rr = "---";
        if (mm.isFile()) {

            try {
                Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", mm);
                rr = "W";
            } catch (IllegalArgumentException e) {
                DLog.handleException(e);
                //Toast.makeText(context, "@"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                rr = "NO";
            }
        }


        fileList.add("{"+s+"}"+mm + "||" + mm.canRead() + "||" + mm.canWrite() + "||" + rr);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String file = fileList.get(position);
        holder.fileNameTextView.setText(file);
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

//    private List<File> getFilesList(File directory) {
//        List<File> files = new ArrayList<>();
//        if (directory != null && directory.exists() && directory.isDirectory()) {
//            File[] fileList = directory.listFiles();
//            if (fileList != null) {
//                for (File file : fileList) {
//                    files.add(file);
//                }
//            }
//        }
//        return files;
//    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView fileNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fileNameTextView = itemView.findViewById(android.R.id.text1);
        }
    }
}

