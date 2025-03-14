// Generated by view binder compiler. Do not edit!
package com.walhalla.phonenumber.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.walhalla.phonenumber.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityRewiewBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final BottomNavigationView bottomNavigation;

  @NonNull
  public final ConstraintLayout container0;

  @NonNull
  public final Button reviewButton;

  @NonNull
  public final Button shareButton;

  @NonNull
  public final Toolbar toolbar;

  private ActivityRewiewBinding(@NonNull RelativeLayout rootView,
      @NonNull BottomNavigationView bottomNavigation, @NonNull ConstraintLayout container0,
      @NonNull Button reviewButton, @NonNull Button shareButton, @NonNull Toolbar toolbar) {
    this.rootView = rootView;
    this.bottomNavigation = bottomNavigation;
    this.container0 = container0;
    this.reviewButton = reviewButton;
    this.shareButton = shareButton;
    this.toolbar = toolbar;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityRewiewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityRewiewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_rewiew, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityRewiewBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.bottomNavigation;
      BottomNavigationView bottomNavigation = ViewBindings.findChildViewById(rootView, id);
      if (bottomNavigation == null) {
        break missingId;
      }

      id = R.id.container0;
      ConstraintLayout container0 = ViewBindings.findChildViewById(rootView, id);
      if (container0 == null) {
        break missingId;
      }

      id = R.id.review_button;
      Button reviewButton = ViewBindings.findChildViewById(rootView, id);
      if (reviewButton == null) {
        break missingId;
      }

      id = R.id.share_button;
      Button shareButton = ViewBindings.findChildViewById(rootView, id);
      if (shareButton == null) {
        break missingId;
      }

      id = R.id.toolbar;
      Toolbar toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }

      return new ActivityRewiewBinding((RelativeLayout) rootView, bottomNavigation, container0,
          reviewButton, shareButton, toolbar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
