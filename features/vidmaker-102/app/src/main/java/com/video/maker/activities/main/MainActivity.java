package com.video.maker.activities.main;

import android.os.Bundle;


import com.video.maker.R;

import com.video.maker.activity.BaseActivity;
import com.video.maker.util.AdManager;
import com.video.makerpro.databinding.ActivityDemo2Binding;


public class MainActivity extends BaseActivity
{


    private ActivityDemo2Binding binding;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDemo2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            MainFragment home = MainFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, home, "123")
                    .commit();
        }

        //MAX + Fb bidding Ads
        AdManager.initAD(MainActivity.this);
//        AdManager.LoadInterstitalAd(MainActivity.this);
//        AdManager.createNativeAdMAX(MainActivity.this, binding.rl_native_ad);


    }



}
