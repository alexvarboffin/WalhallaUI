package com.walhalla.landing.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.walhalla.landing.R
import com.walhalla.landing.databinding.ActivityAboutBinding
import com.walhalla.ui.DLog.getAppVersion

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAboutBinding.inflate(
            layoutInflater
        )
        setContentView(binding.root)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setTitle(R.string.nav_about)
        }
        binding.appVersion.text = getAppVersion(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                //NavUtils.navigateUpFromSameTask(this);
                this.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}