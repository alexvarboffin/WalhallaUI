package com.video.maker.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.video.maker.util.DLog

open class BaseActivity : AppCompatActivity() {

    public override fun attachBaseContext(context: Context) {
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DLog.d("[A] " + javaClass.simpleName)
    }
}
