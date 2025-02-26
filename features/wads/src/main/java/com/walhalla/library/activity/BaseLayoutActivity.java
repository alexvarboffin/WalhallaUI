//package com.walhalla.library.activity;
//
//import android.os.Bundle;
//import android.view.View;
//
//import android.widget.FrameLayout;
//
//import androidx.annotation.LayoutRes;
//
//import com.walhalla.library.R;
//
//public abstract class BaseLayoutActivity extends BActivityPresenter {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_base_layout);
//
//        //LinearLayout rootView = findViewById(R.id.root_layout);
//        //View contentView = getLayoutInflater().inflate(getContentViewLayoutId(), rootView, false);
//        //rootView.addView(contentView);
//
//        FrameLayout contentContainer = findViewById(R.id.content_container);
//        View contentView = getLayoutInflater().inflate(getContentViewLayoutId(), contentContainer, false);
//        contentContainer.addView(contentView);
//    }
//
//    // Метод, возвращающий ID макета, который будет вставлен в центр LinearLayout
//    @LayoutRes
//    protected abstract View getContentViewLayoutId();
//}
//
