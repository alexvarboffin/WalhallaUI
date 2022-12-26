package com.android.lib.navigation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.walhalla.ui.DLog;
import com.walhalla.ui.R;

public class Navigation {
    
    private final AppCompatActivity activity;
    private static final String F_HOME = "home";

    public Navigation(AppCompatActivity activity) {
        this.activity = activity;
    }

//    public void replaceFragment(Fragment fragment) {
//        FragmentManager fragmentManager = activity.getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                .add(org.apache.cordova.R.id.container, fragment)
//                .commit();
//    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fm = activity.getSupportFragmentManager();
        try {
            String fragmentTag = fragment.getClass().getName();

            boolean fragmentPopped = fm
                    .popBackStackImmediate(fragmentTag, 0); //popBackStackImmediate - some times crashed

            if (!fragmentPopped && fm.findFragmentByTag(fragmentTag) == null) {

                FragmentTransaction ftx = fm.beginTransaction();
                ftx.addToBackStack(fragment.getClass().getSimpleName());
//                ftx.setCustomAnimations(R.anim.slide_in_right,
//                        R.anim.slide_out_left, R.anim.slide_in_left,
//                        R.anim.slide_out_right);
                ftx.replace(R.id.viewPager2, fragment);
                ftx.commit();
            }
        } catch (IllegalStateException e) {
            DLog.handleException(e);
        }
    }

//    public void replaceFragmentDrawer(com.android.rss.fragment.ScreenRSSList screenRSSList) {
//    }

//    public void homeScreen() {
//        //Fragment fragment = TabHolderFragment.newInstance("", "");
//        Fragment fragment = TabHolder2Fragment.newInstance("", "");
//
//        //Fragment fragment = BlockListFragment.newInstance("");
//        activity.getSupportFragmentManager()
//
//                .beginTransaction()
//                //.addToBackStack(null)
//                .add(R.id.container, fragment, fragment.getClass().getSimpleName())
//                .commit();
////        replaceFragment(fragment);
//    }
}
