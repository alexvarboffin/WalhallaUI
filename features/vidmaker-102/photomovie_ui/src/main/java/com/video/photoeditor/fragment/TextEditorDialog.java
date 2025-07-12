package com.video.photoeditor.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.video.maker.R;


public class TextEditorDialog extends Dialog implements View.OnClickListener {
    private Context context;

    public EditText edtQuotes;
    private TextFragmentListener listener;

    public interface TextFragmentListener {
        void onText(String str);
    }

    public void setTextListener(TextFragmentListener textFragmentListener) {
        this.listener = textFragmentListener;
    }


    public TextEditorDialog(final Context r4, String str) {
        super(r4);
        this.context = r4;
        setContentView(R.layout.fragment_text_editor);
        edtQuotes = (findViewById(R.id.edtQuotes));
        findViewById(R.id.btnCancel).setOnClickListener(this);
        findViewById(R.id.btnDone).setOnClickListener(this);
        if (!str.equals(r4.getString(R.string.doubletap))) {
            this.edtQuotes.setText(str);
        }
        if (this.edtQuotes.requestFocus()) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    ((InputMethodManager) r4.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(TextEditorDialog.this.edtQuotes, 1);
                }
            }, 200);
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnCancel) {
            this.edtQuotes.setText("");
            return;
        } else if (id == R.id.btnDone) {
            String obj = this.edtQuotes.getText().toString();
            if (obj == "" || obj.isEmpty()) {
                dismiss();
                return;
            }
            TextFragmentListener textFragmentListener = this.listener;
            if (textFragmentListener != null) {
                textFragmentListener.onText(obj);
                dismiss();
                return;
            }
            return;
        }
        return;
    }
}
