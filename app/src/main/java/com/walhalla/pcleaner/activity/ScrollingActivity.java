package com.walhalla.pcleaner.activity;

import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;


import com.walhalla.pcleaner.R;

public class ScrollingActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scrolling);

//        Toolbar toolbar = binding.toolbar;
//        setSupportActionBar(toolbar);
//        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
//        toolBarLayout.setTitle(getTitle());
//
//        FloatingActionButton fab = binding.fab;
//        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show());
    }
}