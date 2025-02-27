// Generated by view binder compiler. Do not edit!
package com.walhalla.phonenumber.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.walhalla.phonenumber.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class DialogEditAppItemBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Button buttonSave;

  @NonNull
  public final EditText editFeatures;

  @NonNull
  public final EditText editTextDescription;

  @NonNull
  public final EditText editTextTitle;

  private DialogEditAppItemBinding(@NonNull LinearLayout rootView, @NonNull Button buttonSave,
      @NonNull EditText editFeatures, @NonNull EditText editTextDescription,
      @NonNull EditText editTextTitle) {
    this.rootView = rootView;
    this.buttonSave = buttonSave;
    this.editFeatures = editFeatures;
    this.editTextDescription = editTextDescription;
    this.editTextTitle = editTextTitle;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static DialogEditAppItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static DialogEditAppItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.dialog_edit_app_item, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static DialogEditAppItemBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.buttonSave;
      Button buttonSave = ViewBindings.findChildViewById(rootView, id);
      if (buttonSave == null) {
        break missingId;
      }

      id = R.id.editFeatures;
      EditText editFeatures = ViewBindings.findChildViewById(rootView, id);
      if (editFeatures == null) {
        break missingId;
      }

      id = R.id.editTextDescription;
      EditText editTextDescription = ViewBindings.findChildViewById(rootView, id);
      if (editTextDescription == null) {
        break missingId;
      }

      id = R.id.editTextTitle;
      EditText editTextTitle = ViewBindings.findChildViewById(rootView, id);
      if (editTextTitle == null) {
        break missingId;
      }

      return new DialogEditAppItemBinding((LinearLayout) rootView, buttonSave, editFeatures,
          editTextDescription, editTextTitle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
