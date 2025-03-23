package com.walhalla.ui

import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager

class MainPresenter(main: AppCompatActivity?) {

    private var doubleBackToExitPressedOnce = false

    fun onBackPressedRequest(var0: AppCompatActivity): Boolean {
//        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
//            mBinding.drawerLayout.closeDrawer(GravityCompat.START);
//        } else {
        //Pressed back => return to home screen

        val fm = var0.supportFragmentManager
        val count = fm.backStackEntryCount
        val result = count > 0

        if (var0.supportActionBar != null) {
            var0.supportActionBar!!.setHomeButtonEnabled(result)
        }
        if (result) {
            fm.popBackStack(fm.getBackStackEntryAt(0).id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        } else { //count == 0
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
                return true
            }

            this.doubleBackToExitPressedOnce = true
            Toast.makeText(var0, var0.getString(com.walhalla.shared.R.string.press_again_to_exit), Toast.LENGTH_SHORT).show()
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 1600)
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
        return false
    }
}
