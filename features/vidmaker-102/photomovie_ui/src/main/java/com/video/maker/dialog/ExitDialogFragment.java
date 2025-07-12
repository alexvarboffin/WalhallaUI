package com.video.maker.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.video.maker.R;
import com.video.maker.databinding.DialogExitBinding;


public class ExitDialogFragment extends DialogFragment {

    public static final String KEY_INPUT_OUTPUT_DATA_FONTVALUE = "key_input_datafontvalue";
    public static final String REQUEST_KEY = "requestKey";

    public static ExitDialogFragment newInstance() {
        return new ExitDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        com.video.maker.databinding.DialogExitBinding binding = DialogExitBinding.inflate(getActivity().getLayoutInflater());
        View view = binding.getRoot();
        builder.setView(view);

        binding.tvDiscard.setOnClickListener(view0 -> {
            okRequest(view0.getId());
            ExitDialogFragment.this.dismiss();
        });

        binding.tvKeep.setOnClickListener(view0 -> ExitDialogFragment.this.dismiss());

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    private void okRequest(int result) {
        Bundle resultBundle = new Bundle();
        resultBundle.putInt(KEY_INPUT_OUTPUT_DATA_FONTVALUE, result);
        FragmentManager fm = getParentFragmentManager();
        fm.setFragmentResult(REQUEST_KEY, resultBundle);
    }
}

