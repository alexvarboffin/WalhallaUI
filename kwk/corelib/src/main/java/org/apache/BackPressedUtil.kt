package org.apache

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import org.apache.cordova.R

object BackPressedUtil {
    @JvmStatic
    fun backPressedToast(activity: Activity) {
        //View view = activity.findViewById(android.R.id.content);
        val view = activity.findViewById<View>(R.id.cLayout)
        if (view == null) {
            showToast(activity, R.string.press_again_to_exit, Toast.LENGTH_SHORT)
        } else {
            showSnackbar(activity, view, R.string.press_again_to_exit, Snackbar.LENGTH_LONG)
        }
    }

    private fun showToast(context: Context, resId: Int, duration: Int) {
        Toast.makeText(context, resId, duration).show()
    }

    private fun showSnackbar(activity: Activity, view: View, resId: Int, duration: Int) {
        Snackbar.make(view, resId, duration).setAction("Action", null).show()
    }
}
