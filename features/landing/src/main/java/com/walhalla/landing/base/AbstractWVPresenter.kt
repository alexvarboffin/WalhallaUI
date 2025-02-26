package com.walhalla.landing.base

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.walhalla.landing.R
import com.walhalla.landing.databinding.AboutBinding
import com.walhalla.ui.DLog.getAppVersion
import com.walhalla.ui.plugins.Module_U.isFromGooglePlay
import java.util.Calendar

abstract class AbstractWVPresenter {
    fun aboutDialog(context: Context) {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        //&#169; - html
        val title = "\u00a9 " + year + " " + context.getString(R.string.play_google_pub)

        val binding = AboutBinding.inflate(LayoutInflater.from(context))
        //View mView = binding.getRoot();
        val dialog = AlertDialog.Builder(context)
            .setTitle(null)
            .setCancelable(true)
            .setIcon(null) //.setNegativeButton(R.string.action_discover_more_app, (dialog1, which) -> moreApp(context))
            .setPositiveButton(android.R.string.ok, null)
            .setView(binding.root)
            .create()
        binding.root.setOnClickListener { v: View? -> dialog.dismiss() }
        binding.aboutVersion.text = getAppVersion(context)
        binding.aboutCopyright.text = title
        binding.aboutLogo.setOnLongClickListener { v: View? ->
            var _o = "[+]gp->" + isFromGooglePlay(context)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                _o = _o + ", category->" + context.applicationInfo.category
            }
            _o = """
                $_o
                Устройство имеет плотность ${dest(context)}
                """.trimIndent()
            binding.aboutCopyright.text = _o
            false
        }
        //dialog.setButton();
        dialog.show()
    }

    private fun dest(context: Context): String {
        val metrics = context.resources.displayMetrics
        val densityDpi = metrics.densityDpi
        return if (densityDpi == DisplayMetrics.DENSITY_LOW) {
            "ldpi"
        } else if (densityDpi == DisplayMetrics.DENSITY_MEDIUM) {
            "mdpi"
        } else if (densityDpi == DisplayMetrics.DENSITY_HIGH) {
            "hdpi"
        } else if (densityDpi == DisplayMetrics.DENSITY_XHIGH) {
            "xhdpi"
        } else if (densityDpi == DisplayMetrics.DENSITY_XXHIGH) {
            "xxhdpi"
        } else if (densityDpi == DisplayMetrics.DENSITY_XXXHIGH) {
            "xxxhdpi"
        } else {
            "Другая плотность"
        }
    }
}
