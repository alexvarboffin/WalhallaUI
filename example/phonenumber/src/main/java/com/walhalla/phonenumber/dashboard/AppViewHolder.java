package com.walhalla.phonenumber.dashboard;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.walhalla.phonenumber.databinding.ItemAppBinding;
import com.walhalla.ui.plugins.Launcher;


public class AppViewHolder
        extends SortedListAdapter.ViewHolder<AppModel>
{

    public final ItemAppBinding mBinding;

    public AppViewHolder(ItemAppBinding binding, WalhallaAppAdapter.Listener listener) {
        super(binding.getRoot());
        binding.setListener(listener);
        mBinding = binding;

    }

    @Override
    protected void performBind(@NonNull AppModel obj) {
        mBinding.setModel(obj);

        String packageName = resolvePackageName(obj.q);
        String displayName = !TextUtils.isEmpty(obj.name) ? obj.name : shortId(obj.q);
        mBinding.fillName.setText(displayName);
        // Always force package on its own line (visible + selectable)
        mBinding.packageName.setText(packageName);
        mBinding.packageName.setVisibility(View.VISIBLE);
        // Belt-and-suspenders: also keep package in content description for a11y/debug
        mBinding.getRoot().setContentDescription(displayName + " " + packageName);

        if (obj.isInstalled) {
            mBinding.installedIcon.setVisibility(View.VISIBLE);
        } else {
            mBinding.installedIcon.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(obj.projectName)) {
            mBinding.crashlytics.setVisibility(View.VISIBLE);
            mBinding.title.setVisibility(View.VISIBLE);
            mBinding.title.setText(obj.projectName);
        } else {
            mBinding.crashlytics.setVisibility(View.GONE);
            mBinding.title.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(obj.features)) {
            mBinding.features.setVisibility(View.VISIBLE);
            mBinding.features.setText(obj.features);

        } else {
            mBinding.features.setVisibility(View.GONE);
        }

//        apk editor pro
//        apk extractor

        if (!TextUtils.isEmpty(obj.privacy_policy)) {
            mBinding.privacyPolicy.setVisibility(View.VISIBLE);
            mBinding.privacyPolicy.setOnClickListener(v -> {
                Launcher.openBrowser(v.getContext(), obj.privacy_policy);
            });

        } else {
            mBinding.privacyPolicy.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(obj.color)) {
            mBinding.color.setBackgroundColor(Color.parseColor(obj.color));
        } else {
            //mBinding.crashlytics.setVisibility(View.GONE);
        }

        itemView.setOnLongClickListener(v -> {
            Toast.makeText(v.getContext(), "@", Toast.LENGTH_SHORT).show();
            return false;
        });
        if (obj.time > 0) {
            mBinding.main.setBackgroundResource(obj.time);
        }
    }

    /** Full applicationId: dotted package as-is, else com.walhalla. + short id. */
    private static String resolvePackageName(String q) {
        if (TextUtils.isEmpty(q)) {
            return "";
        }
        return q.contains(".") ? q : ("com.walhalla." + q);
    }

    private static String shortId(String q) {
        if (TextUtils.isEmpty(q)) {
            return "";
        }
        if (!q.contains(".")) {
            return q;
        }
        int last = q.lastIndexOf('.');
        return last >= 0 && last < q.length() - 1 ? q.substring(last + 1) : q;
    }

}
