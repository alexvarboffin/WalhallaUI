package com.walhalla.phonenumber.dashboard;

import static com.walhalla.core.UConst.GOOGLE_PLAY_CONSTANT;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.walhalla.phonenumber.AppIconUtils;
import com.walhalla.phonenumber.R;

import com.walhalla.phonenumber.dashboard.AppModel;
import com.walhalla.phonenumber.dashboard.AppViewHolder;
import com.walhalla.phonenumber.dashboard.WordModel;
import com.walhalla.phonenumber.databinding.ItemAppBinding;
import com.walhalla.ui.DLog;
import com.walhalla.ui.Module_U;


import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;


public class WalhallaAppAdapter extends SortedListAdapter<WordModel> {

    private final Context context;

    public interface Listener {
        void onExampleModelClicked(WordModel model);
    }

    private final Listener mListener;

    public WalhallaAppAdapter(Context context, Comparator<WordModel> comparator, Listener listener) {
        super(context, WordModel.class, comparator);
        this.context = context;
        mListener = listener;
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
        }
    }

    private void crash(View view, int position) {
        AppModel topic = (AppModel) getItem(position);

        String mm = "com.walhalla." + topic.q;
        if (topic.q.contains(".")) {
            mm = topic.q;
        }

        String aa = "https://console.firebase.google.com/project/" +
                topic.projectName + "/crashlytics/app/android:" +
                mm + "/issues?state=open&time=last-seven-days&tag=all";
        Module_U.openBrowser(context, aa);
        DLog.d(aa);
    }

    private void showPopupMenu(View v, int position) {
        AppModel topic = (AppModel) getItem(position);
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        MenuInflater inflater = popup.getMenuInflater();
        Menu menu = popup.getMenu();

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

            switch (menuItem.getItemId()) {

                case R.id.sub_text:
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

                case R.id.action_open_link:
                    String mm0 = "com.walhalla." + topic.q;
                    if (topic.q.contains(".")) {
                        mm0 = topic.q;
                    }
                    DLog.d("" + mm0);
                    Module_U.openMarketApp(context, mm0);
                    return true;


                case R.id.action_share_googlePlayLink:
                    String s = "com.walhalla." + topic.q;
                    if (topic.q.contains(".")) {
                        s = topic.q;
                    }
                    s = GOOGLE_PLAY_CONSTANT + s;
                    Module_U.shareText(context, s, "SHARE");
                    return true;


                case R.id.action_open_privacy_policy:
                    String replace = "https://@.web.app/privacy_policy.html".replace("@", topic.projectName);
                    Module_U.openBrowser(context, replace);
                    return true;

                case R.id.action_open_google:
                    String replace1 = "https://www.google.com/search?client=firefox-b-d&q=@".replace("@", topic.q);
                    Module_U.openBrowser(context, replace1);
                    return true;

                case R.id.action_user_feedback:
                    if (topic.app_id != null) {
                        String m0 = "https://play.google.com/console/developers/7076844630778011299/app/@/user-feedback/reviews";
                        String replace2 = m0.replace("@", topic.app_id);
                        Module_U.openBrowser(context, replace2);
                    }
                    return true;

                case R.id.action_dataprivacysecurity:
                    if (topic.app_id != null) {
                        String security = "https://play.google.com/console/developers/7076844630778011299/app/@/app-content/data-privacy-security";
                        String replace21 = security.replace("@", topic.app_id);
                        Module_U.openBrowser(context, replace21);
                    }
                    return true;

//                case R.id.action_share_image:
//                    popup.dismiss();
//                    showWatermark(tv_quotes_watermark);
//                    Bitmap bitmap = Bitmap.createBitmap(relativeLayout.getWidth(), relativeLayout.getHeight(),
//                            Bitmap.Config.ARGB_8888);
//                    Canvas canvas = new Canvas(bitmap);
//                    relativeLayout.draw(canvas);
//                    Uri uri = CoreUtil.getLocalBitmapUri(getActivity(), bitmap);
//                    hideWatermark(tv_quotes_watermark);
//                    String appName = getString(R.string.app_name);
//
//                    String extra = status.text;
//                    if (!TextUtils.isEmpty(status.author)) {
//                        extra = extra + "\n" + "— " + status.author + "\n" + appName;
//                    }
//
//                    Intent intent = Share.makeImageShare(extra);
//                    intent.putExtra(Intent.EXTRA_STREAM, uri);
//                    intent.putExtra(Intent.EXTRA_SUBJECT, appName);
//                    //intent.putExtra(Intent.EXTRA_TITLE, appName);
//
//                    //BugFix
//                    //java.lang.SecurityException: Permission Denial: reading androidx.core.content.FileProvider
//                    Intent chooser = Intent.createChooser(intent, appName);
//                    List<ResolveInfo> resInfoList = getActivity().getPackageManager()
//                            .queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);
//                    for (ResolveInfo resolveInfo : resInfoList) {
//                        String packageName = resolveInfo.activityInfo.packageName;
//                        getActivity().grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION
//                                | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    }
//
//                    getActivity().startActivity(chooser);
//                    Toast.makeText(getActivity(), "Share as Image", Toast.LENGTH_SHORT).show();
//                    return true;
            }
            return false;
        });
        popup.show();
    }
}
