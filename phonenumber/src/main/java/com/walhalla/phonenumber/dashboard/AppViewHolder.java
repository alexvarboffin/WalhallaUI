package com.walhalla.phonenumber.dashboard;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.walhalla.phonenumber.databinding.ItemAppBinding;
import com.walhalla.ui.Module_U;


public class AppViewHolder extends SortedListAdapter.ViewHolder<AppModel> {

    public final ItemAppBinding mBinding;

    public AppViewHolder(ItemAppBinding binding, WalhallaAppAdapter.Listener listener) {
        super(binding.getRoot());
        binding.setListener(listener);
        mBinding = binding;

    }

    @Override
    protected void performBind(@NonNull AppModel obj) {
        mBinding.setModel(obj);
        String mm = "" + obj.q;
        if (!TextUtils.isEmpty(obj.name)) {
            mm = obj.name + " (" + obj.q + ")";
        }
        mBinding.fillName.setText(mm);

        if (obj.isInstalled) {
            mBinding.installedIcon.setVisibility(View.VISIBLE);
        } else {
            mBinding.installedIcon.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(obj.projectName)) {
            mBinding.crashlytics.setVisibility(View.VISIBLE);
            mBinding.title.setText(obj.projectName);
        } else {
            mBinding.crashlytics.setVisibility(View.GONE);
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
                Module_U.openBrowser(v.getContext(), obj.privacy_policy);
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
    }

}
