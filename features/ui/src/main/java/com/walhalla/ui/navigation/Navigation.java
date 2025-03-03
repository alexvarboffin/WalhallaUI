package com.walhalla.ui.navigation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.walhalla.ui.DLog;

public class Navigation {

    private final AppCompatActivity activity;
    private static final String F_HOME = "home";

    public Navigation(AppCompatActivity activity) {
        this.activity = activity;
    }

//    public void replaceFragment(Fragment fragment) {
//        FragmentManager fragmentManager = activity.getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                .add(org.apache.cordova.R.@@@@@@@@@@@@@@@@@, fragment)
//                .commit();
//    }

//    public void replaceFragment(Fragment fragment, int container) {
//        replaceFragment(fragment, R.@@@@@@@@@@@@@@@@@);
//    }

    /**
     *
     *          CLEAR STACK
     */
    public void replaceWebFragment(Fragment fragment, int container) {
        //Clear back stack
        FragmentManager fm = activity.getSupportFragmentManager();
        //final int count = getSupportFragmentManager().getBackStackEntryCount();
        if (!fm.isDestroyed()) {
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fr = fm.beginTransaction();
            fr.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fr.replace(container, fragment, fragment.getClass().getSimpleName());
            fr.commitAllowingStateLoss();
        }
    }


    /**
     *
     *          NOT CLEAR STACK
     */
    public void replaceFragment(Fragment fragment, int container) {
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
                ftx.replace(container, fragment);
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
//                .add(R.@@@@@@@@@@@@@@@@@, fragment, fragment.getClass().getSimpleName())
//                .commit();
////        replaceFragment(fragment);
//    }
}
