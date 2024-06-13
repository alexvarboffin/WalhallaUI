package com.walhalla.phonenumber.activity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import com.walhalla.phonenumber.R;
import com.walhalla.phonenumber.apps.AppListFragment;
import com.walhalla.phonenumber.apps.ViewPagerAdapter;
import com.walhalla.phonenumber.dashboard.WalhallaAppListFragment;

import com.walhalla.ui.DLog;
import com.walhalla.ui.plugins.Launcher;
import com.walhalla.ui.plugins.Module_U;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private final List<String> indexToPage = new ArrayList<>();
    private ViewPagerAdapter mPagerAdapter;
    private PowerManager.WakeLock wakeLock;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_simle_main);
        // Получаем объект PowerManager
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP, "YourApp:WakeLockTag");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        TabLayout tabs = findViewById(R.id.tabs);

        viewPager2 = findViewById(R.id.mainViewPager);
        viewPager2.setUserInputEnabled(false);

        final BottomNavigationView navView = findViewById(R.id.bottomNavigation);
        navView.setOnItemSelectedListener(mOnNavigationItemSelectedListener);


        mPagerAdapter = new ViewPagerAdapter(this);

        mPagerAdapter.addFragment(new WalhallaAppListFragment(), "W");
        indexToPage.add("W");
        Menu m = navView.getMenu();
        m.add("W").setIcon(R.drawable.ic_dots);

        int j = 0;

        try {
            String base = "apps";
            String[] kk = this.getAssets().list(base);

            for (int i = 0; i < kk.length; i++) {
                String s = kk[i];
                mPagerAdapter.addFragment(AppListFragment.newInstance(base + "/" + s), base + "/" + s);
                String title = s.split("\\.")[0];
//PPP               Menu m = navView.getMenu();
//PPP               m.add(title).setIcon(R.drawable.cancel);
                indexToPage.add(title);
                ++j;
            }
        } catch (IOException e) {
            DLog.handleException(e);
        }


        viewPager2.setAdapter(mPagerAdapter);
        viewPager2.setOffscreenPageLimit(indexToPage.size());

        // Connect the TabLayout with the ViewPager
        new TabLayoutMediator(tabs, viewPager2,
                (tab, position) -> tab.setText(indexToPage.get(position))
        ).attach();
    }


    @SuppressLint("NonConstantResourceId")
    private final NavigationBarView.OnItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {

        Integer position = indexToPage.indexOf(item.getTitle());
        if (position >= 0) {
            menuSelected(position);
            return true;
        }

//        switch (item.getItemId()) {
//            case R.id.navigation_home:
//                fm.beginTransaction().hide(active).show(fragment1).commit();
//                active = fragment1;
//                return true;
//
//            case R.id.navigation_dashboard:
//                fm.beginTransaction().hide(active).show(fragment2).commit();
//                active = fragment2;
//                return true;
//
//            case R.id.navigation_notifications:
//                fm.beginTransaction().hide(active).show(fragment3).commit();
//                active = fragment3;
//                return true;
//        }
        return false;
    };

    private void menuSelected(Integer position) {
        handleSelectedSuccess(position);
    }

    private void handleSelectedSuccess(int position) {
        if (viewPager2.getCurrentItem() != position) {
            setItem(position);
        }
    }

    private void setItem(Integer position) {
        viewPager2.setCurrentItem(position);
        //viewModel.push(position);
    }

    @SuppressLint("WakelockTimeout")
    @Override
    protected void onResume() {
        super.onResume();
        // Включаем WakeLock
        wakeLock.acquire();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Отключаем WakeLock при приостановке активности
        wakeLock.release();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_about) {
            Module_U.aboutDialog(this);
            return true;
        } else if (itemId == R.id.action_developer) {
            //AppUtils.openChromeBrowser(this, "https://play.google.com/store/apps/developer?id=Walhalla+Dynamics");
            Module_U.moreApp(getApplicationContext());
            return true;
        } else if (itemId == R.id.action_rate_app) {
            Launcher.rateUs(this);
            return true;
        } else if (itemId == R.id.action_share_app) {
            Module_U.shareThisApp(this);
            return true;
        } else if (itemId == R.id.action_discover_more_app) {
            Module_U.moreApp(this);
            return true;
        } else if (itemId == R.id.action_feedback) {
            Module_U.feedback(this);
            return true;
        }
        return super.onOptionsItemSelected(item);

//            case R.id.action_exit:
//                this.finish();
//                return true;
        //            case R.id.action_more_app_01:
//                Module_U.moreApp(this, "com.walhalla.ttloader");
//                return true;
//            case R.id.action_more_app_02:
//                Module_U.moreApp(this, "com.walhalla.vibro");
//                return true;
    }
}
