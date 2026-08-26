package com.walhalla.phonenumber.dashboard;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;

import com.walhalla.phonenumber.databinding.DialogEditAppItemBinding;

public class EditAppItemDialog extends Dialog {

    private final AppModel item;
    private DialogEditAppItemBinding binding;

    private final OnSaveListener onSaveListener;

    public EditAppItemDialog(Context context, AppModel appItem, OnSaveListener onSaveListener) {
        super(context);
        this.item = appItem;
        this.onSaveListener = onSaveListener;
    }

    public interface OnSaveListener {
        void onSave(String title, String description, String newFeatures);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DialogEditAppItemBinding.inflate(LayoutInflater.from(getContext()));
        setContentView(binding.getRoot());

        binding.editTextTitle.setText(item.q);
        binding.editTextDescription.setText(item.app_id);
        binding.editFeatures.setText(item.features);
        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle = binding.editTextTitle.getText().toString();
                String newDescription = binding.editTextDescription.getText().toString();
                String newFeatures = binding.editFeatures.getText().toString();

                if (onSaveListener != null) {
                    onSaveListener.onSave(newTitle, newDescription, newFeatures);
                }
                dismiss();
            }
        });
    }
}
