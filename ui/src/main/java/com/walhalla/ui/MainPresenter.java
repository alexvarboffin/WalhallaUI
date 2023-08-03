package com.walhalla.ui;

import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class MainPresenter {

    private boolean doubleBackToExitPressedOnce;

    public MainPresenter(AppCompatActivity main) {}


    public boolean onBackPressedRequest(@NonNull AppCompatActivity var0) {

//        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
//            mBinding.drawerLayout.closeDrawer(GravityCompat.START);
//        } else {
        //Pressed back => return to home screen
        FragmentManager fm = var0.getSupportFragmentManager();
        int count = fm.getBackStackEntryCount();
        boolean result = count > 0;

        if (var0.getSupportActionBar() != null) {
            var0.getSupportActionBar().setHomeButtonEnabled(result);
        }
        if (result) {
            fm.popBackStack(fm.getBackStackEntryAt(0).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {//count == 0
//                Dialog
//                new AlertDialog.Builder(this)
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .setTitle("Leaving this App?")
//                        .setMessage("Are you sure you want to close this application?")
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                finish();
//                            }
//
//                        })
//                        .setNegativeButton("No", null)
//                        .show();
            //super.onBackPressed();
            if (doubleBackToExitPressedOnce) {
                return true;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(var0, var0.getString(R.string.press_again_to_exit), Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 500);
        }
            /*
            //Next/Prev Navigation
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Leaving this App?")
                        .setMessage("Are you sure you want to close this application?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
            else
            {
            super.onBackPressed();
            }
            */
//        }
        return false;
    }
}
