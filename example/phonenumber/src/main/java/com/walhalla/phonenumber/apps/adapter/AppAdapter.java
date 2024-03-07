package com.walhalla.phonenumber.apps.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.walhalla.core.UConst;
import com.walhalla.phonenumber.AppIconUtils;
import com.walhalla.phonenumber.R;
import com.walhalla.phonenumber.apps.adapter.appitem.AppObj;
import com.walhalla.phonenumber.apps.adapter.appitem.AppViewHolder;
import com.walhalla.phonenumber.dashboard.AppModel;
import com.walhalla.ui.DLog;
import com.walhalla.ui.Module_U;
import com.walhalla.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppViewHolder> {

    private final Context context;
    private List<AppObj> appList;

    public AppAdapter(Context context, String fileName) {
        this.context = context;
        try {
            loadJSONFromAsset(context, fileName);
        } catch (Exception e) {
            DLog.handleException(e);
        }
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_other, parent, false);
        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        try {
            AppObj app = appList.get(position);
            holder.titleTextView.setText(app.getTitle());
            holder.rateTextView.setText(app.getRate());
            holder.countTextView.setText(app.getCount());

            if (app.isInstalled) {
                holder.installedIcon.setVisibility(View.VISIBLE);
            } else {
                holder.installedIcon.setImageResource(R.mipmap.ic_launcher);
            }
            holder.itemView.setOnClickListener(v ->
            {
                if (app.isInstalled) {
                    Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(app.url);
                    context.startActivity(launchIntent);
                } else {
                    String mm0 = "com.walhalla." + app.url;
                    if (app.url.contains(".")) {
                        mm0 = app.url;
                    }
                    DLog.d("" + mm0);
                    Module_U.openMarketApp(context, mm0.replace(UConst.GOOGLE_PLAY_CONSTANT, ""));
                }

                //Module_U.openMarketApp(context, app.url);
            });

            holder.itemView.setOnLongClickListener(v -> {
                showPopupMenu(v);
                return true;
            });


            String packageName = app.url;
            packageName = packageName.replace(UConst.GOOGLE_PLAY_CONSTANT, "");
            packageName = packageName.replace("https://play.google.com/store/apps/details?id\u003d", "");

            if (!packageName.contains(".")) {
                packageName = "com.walhalla." + app.url;
            }

            //DLog.d("@@@@@@@@@@@@@@@@"+packageName+"@@"+app.isInstalled);

            AppIconUtils.loadAppIcon(holder.installedIcon.getContext(), packageName, new AppIconUtils.OnIconLoadListener() {
                @Override
                public void onIconLoaded(Bitmap iconBitmap) {
                    if (iconBitmap == null) {
                        holder.installedIcon.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        holder.installedIcon.setImageBitmap(iconBitmap);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    private void loadJSONFromAsset(Context context, String fileName) {
        try {
            appList = new ArrayList<>();
            InputStream inputStream = context.getAssets().open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, "UTF-8");

            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int id = jsonObject.getInt("id");
                String url = jsonObject.getString("url");
                String title = jsonObject.getString("title");
                String rate = jsonObject.getString("rate");
                String count = jsonObject.getString("count");

                // Проверка установки приложения по пакету
                boolean isInstalled = AppUtils.isAppInstalled(context, url);

                AppObj app = new AppObj(id, url, title, rate, count, isInstalled);
                appList.add(app);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }



    private void openGooglePlay(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        boolean isIntentSafe = activities.size() > 0;
        if (isIntentSafe) {
            context.startActivity(intent);
        }
    }

    @SuppressLint("NonConstantResourceId")
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_app_item, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_option1:
                    // Обработка действия всплывающего меню
                    return true;
                case R.id.menu_option2:
                    // Обработка действия всплывающего меню
                    return true;
                default:
                    return false;
            }
        });

        popupMenu.show();
    }

}

