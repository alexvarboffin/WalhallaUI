package com.walhalla.phonenumber.dashboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;

import com.github.wrdlbrnft.modularadapter.ModularAdapter;
import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.walhalla.phonenumber.AppIconUtils;
import com.walhalla.phonenumber.R;

import com.walhalla.phonenumber.databinding.ItemAppBinding;

import com.walhalla.ui.DLog;
import com.walhalla.ui.UConst;
import com.walhalla.ui.plugins.Launcher;
import com.walhalla.ui.plugins.Module_U;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WalhallaAppAdapter extends SortedListAdapter<WordModel> {

    private static final String KEY_PROJECT_NAME = "__projectName__";
    private final Context context;
    Map<String, String> map = new HashMap<>();

    public interface Listener {
        void onExampleModelClicked(WordModel model);
    }

    private final Listener mListener;

    public WalhallaAppAdapter(Context context, Comparator<WordModel> comparator, Listener listener) {
        super(context, WordModel.class, comparator);
        this.context = context;
        mListener = listener;

        map.put("Безопасность данных", "https://play.google.com/console/developers/7076844630778011299/app/@/app-content/data-privacy-security");
        map.put("Отзывы", "https://play.google.com/console/developers/7076844630778011299/app/@/user-feedback/reviews");
        map.put("@ Соответствие правилам", "https://play.google.com/console/developers/7076844630778011299/app/@/policy-center");
        map.put("Контент приложения (деклараций)", "https://play.google.com/console/developers/7076844630778011299/app/@/app-content/overview");
        map.put("Рабочая версия", "https://play.google.com/console/developers/7076844630778011299/app/@/tracks/production");

        map.put("Доступность приложения", "https://play.google.com/console/developers/7076844630778011299/app/@/advanced-distribution");


        map.put("app-ads.txt", "https://__projectName__.web.app/app-ads.txt");

    }

    @NonNull
    @Override
    protected ViewHolder<? extends WordModel> onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType) {
        final ItemAppBinding binding = ItemAppBinding.inflate(inflater, parent, false);
        return new AppViewHolder(binding, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ModularAdapter.ViewHolder<? extends WordModel> holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if (holder instanceof AppViewHolder) {
            AppViewHolder h0 = (AppViewHolder) holder;
            h0.mBinding.overflowMenu.setOnClickListener(view -> showPopupMenu(view, position));
            h0.mBinding.crashlytics.setOnClickListener(view -> crash(view, position));


            AppModel topic = (AppModel) getItem(position);
            String packageName = topic.q;
            if (!packageName.contains(".")) {
                packageName = "com.walhalla." + topic.q;
            }
            AppIconUtils.loadAppIcon(h0.mBinding.getRoot().getContext(), packageName, new AppIconUtils.OnIconLoadListener() {
                @Override
                public void onIconLoaded(Bitmap iconBitmap) {
                    // Установите полученную иконку в ImageView
                    h0.mBinding.installedIcon.setImageBitmap(iconBitmap);
                }
            });

            h0.mBinding.installedIcon.setOnClickListener(v->{
                showEditDialog(topic, position);
            });
        }
    }

    EditAppItemDialog dialog;

    private void showEditDialog(AppModel appItem, int position) {
        dialog = new EditAppItemDialog(context,
                appItem, new EditAppItemDialog.OnSaveListener() {
            @Override
            public void onSave(String newTitle, String newDescription, String newFeatures) {
                appItem.features=newFeatures;
                FirebaseDatabase.getInstance().getReference("walhalla")
                        .child(String.valueOf(position))
                        .setValue(appItem)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                dialog.dismiss();
                            } else {
                                // Handle failure
                            }
                        });
            }
        });
        dialog.show();
    }

    private void crash(View view, int position) {
        AppModel topic = (AppModel) getItem(position);

        String mm = "com.walhalla." + topic.q;
        if (topic.q.contains(".")) {
            mm = topic.q;
        }

        String entryValue = "https://console.firebase.google.com/project/__projectName__/crashlytics/app/android:" + mm + "/issues?state=open&time=last-seven-days&tag=all";
        if (!TextUtils.isEmpty(topic.projectName)) {
            entryValue = entryValue.replace(KEY_PROJECT_NAME, topic.projectName);
        }
        Launcher.openBrowser(context, entryValue);
        DLog.d(entryValue);
    }

    private void showPopupMenu(View v, int position) {
        AppModel topic = (AppModel) getItem(position);
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        MenuInflater inflater = popup.getMenuInflater();
        Menu menu = popup.getMenu();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            menu.add(entry.getKey()).setOnMenuItemClickListener(item -> {

                String entryValue = entry.getValue();
                if (topic.app_id != null) {
                    entryValue = entryValue.replace("@", topic.app_id);
                }
                if (!TextUtils.isEmpty(topic.projectName)) {
                    entryValue = entryValue.replace(KEY_PROJECT_NAME, topic.projectName);
                }
                DLog.d("" + entryValue);
                Launcher.openBrowser(context, entryValue);
                return true;
            });
        }

        inflater.inflate(R.menu.popup_topic, menu);
        Object menuHelper;
        Class<?>[] argTypes;
        try {
            Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
            fMenuHelper.setAccessible(true);
            menuHelper = fMenuHelper.get(popup);
            argTypes = new Class[]{boolean.class};
            menuHelper.getClass().getDeclaredMethod("setForceShowIcon", argTypes).invoke(menuHelper, true);
        } catch (Exception e) {
        }
        popup.setOnMenuItemClickListener(menuItem -> {

            int itemId = menuItem.getItemId();
            if (itemId == R.id.sub_text) {
                if (popup != null) {
                    popup.dismiss();
                }
                Intent intent1 = new Intent(Intent.ACTION_SEND);
                intent1.setType("text/plain");
                String mm = "com.walhalla." + topic.q;
                if (topic.q.contains(".")) {
                    mm = topic.q;
                }
                String shareBody = mm + "\n";
                intent1.putExtra(Intent.EXTRA_SUBJECT, "app");
                intent1.putExtra(Intent.EXTRA_TEXT, shareBody);
                intent1.putExtra("com.pinterest.EXTRA_DESCRIPTION", shareBody);
                context.startActivity(Intent.createChooser(intent1, "Share Quote"));
                Toast.makeText(context, "share_as_text", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.action_open_link) {
                String mm0 = "com.walhalla." + topic.q;
                if (topic.q.contains(".")) {
                    mm0 = topic.q;
                }
                DLog.d("" + mm0);
                Launcher.openMarketApp(context, mm0);
                return true;
            } else if (itemId == R.id.action_share_googlePlayLink) {
                String s = "com.walhalla." + topic.q;
                if (topic.q.contains(".")) {
                    s = topic.q;
                }
                s = UConst.GOOGLE_PLAY_CONSTANT + s;
                Module_U.shareText(context, s, "SHARE");
                return true;
            } else if (itemId == R.id.action_open_privacy_policy) {
                String replace = "https://@.web.app/privacy_policy.html".replace("@", topic.projectName);
                Launcher.openBrowser(context, replace);
                return true;
            } else if (itemId == R.id.action_open_google) {
                String replace1 = "https://www.google.com/search?client=firefox-b-d&q=@".replace("@", topic.q);
                Launcher.openBrowser(context, replace1);
                return true;
            }

//            else if (itemId == R.id.action_share_image) {
//                popup.dismiss();
//                showWatermark(tv_quotes_watermark);
//                Bitmap bitmap = Bitmap.createBitmap(relativeLayout.getWidth(), relativeLayout.getHeight(),
//                        Bitmap.Config.ARGB_8888);
//                Canvas canvas = new Canvas(bitmap);
//                relativeLayout.draw(canvas);
//                Uri uri = CoreUtil.getLocalBitmapUri(getActivity(), bitmap);
//                hideWatermark(tv_quotes_watermark);
//                String appName = getString(R.string.app_name);
//
//                String extra = status.text;
//                if (!TextUtils.isEmpty(status.author)) {
//                    extra = extra + "\n" + "— " + status.author + "\n" + appName;
//                }
//
//                Intent intent = Share.makeImageShare(extra);
//                intent.putExtra(Intent.EXTRA_STREAM, uri);
//                intent.putExtra(Intent.EXTRA_SUBJECT, appName);
//                //intent.putExtra(Intent.EXTRA_TITLE, appName);
//
//                //BugFix
//                //java.lang.SecurityException: Permission Denial: reading androidx.core.content.FileProvider
//                Intent chooser = Intent.createChooser(intent, appName);
//                List<ResolveInfo> resInfoList = getActivity().getPackageManager()
//                        .queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);
//                for (ResolveInfo resolveInfo : resInfoList) {
//                    String packageName = resolveInfo.activityInfo.packageName;
//                    getActivity().grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION
//                            | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                }
//
//                getActivity().startActivity(chooser);
//                Toast.makeText(getActivity(), "Share as Image", Toast.LENGTH_SHORT).show();
//                return true;
//            }
            return false;
        });
        popup.show();
    }
}
