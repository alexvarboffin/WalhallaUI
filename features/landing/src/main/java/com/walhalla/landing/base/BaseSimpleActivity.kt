package com.walhalla.landing.base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.walhalla.landing.R
import com.walhalla.landing.activity.WebActivity
import com.walhalla.landing.databinding.ActivityMainBinding
import com.walhalla.ui.DLog.getAppVersion
import com.walhalla.ui.plugins.Launcher.openBrowser
import com.walhalla.ui.plugins.Launcher.rateUs
import com.walhalla.ui.plugins.Module_U.feedback
import com.walhalla.ui.plugins.Module_U.isFromGooglePlay
import com.walhalla.ui.plugins.Module_U.moreApp
import com.walhalla.ui.plugins.Module_U.shareThisApp
import com.walhalla.webview.ChromeView
import java.util.Calendar

abstract class BaseSimpleActivity : WebActivity(), ChromeView {
    protected open var doubleBackToExitPressedOnce: Boolean = false

    //private Handler mHandler;
    protected var binding: ActivityMainBinding? = null
    protected var presenter: WViewPresenter? = null
    private var activityConfig: ActivityConfig? = null


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //mHandler = new Handler();
        //Handler handler = new Handler(Looper.getMainLooper());
        //presenter = new WPresenterImpl(handler, this);
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        //toolbar.setVisibility(View.GONE);
        setSupportActionBar(binding!!.toolbar)
        activityConfig = getActivityConfig()
        presenter = WViewPresenter(this, activityConfig!!)
    }

    protected abstract fun getActivityConfig(): ActivityConfig

    @SuppressLint("NonConstantResourceId")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == R.id.action_about) {
            aboutDialog(this)
            return true
        } else if (itemId == R.id.action_privacy_policy) {
            openBrowser(this, getString(R.string.url_privacy_policy))
            return true
        } else if (itemId == R.id.action_rate_app) {
            rateUs(this)
            return true
        } else if (itemId == R.id.action_share_app) {
            shareThisApp(this)
            return true
        } else if (itemId == R.id.action_discover_more_app) {
            moreApp(this)
            return true
        } else if (itemId == R.id.action_exit) {
            this.finish()
            return true
        } else if (itemId == R.id.action_feedback) {
            feedback(this)
            return true
        } else if (itemId == R.id.action_refresh) {
            presenter!!.refreshWV()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onPageStarted(url: String?) {
        presenter!!.hideSwipeRefreshing()
    }

    override fun onPageFinished( /*WebView view, */
                                 url: String?
    ) {
        presenter!!.hideSwipeRefreshing()

        //        if (getSupportActionBar() != null) {
//            String m = view.getTitle();
//            if(TextUtils.isEmpty(m)){
//                getSupportActionBar().setSubtitle(m);
//            }
//        }
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawerLayout)
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
            return
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }
            if (USE_HISTORY()) {
                if (presenter!!.canGoBack()) {
                    //Toast.makeText(this, "##" + (mWebBackForwardList.getCurrentIndex()), Toast.LENGTH_SHORT).show();
                    //historyUrl = mWebBackForwardList.getItemAtIndex(mWebBackForwardList.getCurrentIndex()-1).getUrl();
                    presenter!!.goBack()
                    return
                }
            }
        }

        backPressedToast()
        this.doubleBackToExitPressedOnce = true
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 1800)
    }

    protected abstract fun USE_HISTORY(): Boolean


    fun switchViews(b: Boolean) {
        if (b) {
            binding!!.contentFake.root.visibility = View.VISIBLE
            binding!!.contentMain.visibility = View.GONE
            //getSupportActionBar().setTitle("...");
        } else {
            binding!!.contentFake.root.visibility = View.GONE
            binding!!.contentMain.visibility = View.VISIBLE
            //getSupportActionBar().setTitle(R.string.app_name);
        }
    }

    fun setDrawerEnabled(enabled: Boolean) {
        val toggle = ActionBarDrawerToggle(
            this,
            binding!!.drawerLayout,
            binding!!.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding!!.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        val lockMode =
            if (enabled) DrawerLayout.LOCK_MODE_UNLOCKED else DrawerLayout.LOCK_MODE_LOCKED_CLOSED
        binding!!.drawerLayout.setDrawerLockMode(lockMode)
        toggle.isDrawerIndicatorEnabled = enabled
    } //bpt
    //    View coordinatorLayout = findViewById(R.id.coordinatorLayout);
    //        Snackbar.make(coordinatorLayout, getString(R.string.press_again_to_exit), Snackbar.LENGTH_LONG)
    //            .setAction("Action", null).show();


    companion object {
        fun aboutDialog(context: Context) {
            val calendar = Calendar.getInstance()
            val year = calendar[Calendar.YEAR]
            //&#169; - html
            val title = "\u00a9 " + year + " " + context.getString(R.string.play_google_pub)
            val mView = LayoutInflater.from(context).inflate(R.layout.about, null)
            val dialog = AlertDialog.Builder(context)
                .setTitle(null)
                .setCancelable(true)
                .setIcon(null) //.setNegativeButton(R.string.action_discover_more_app, (dialog1, which) -> moreApp(context))
                .setPositiveButton(android.R.string.ok, null)
                .setView(mView)
                .create()
            mView.setOnClickListener { v: View? -> dialog.dismiss() }
            val textView = mView.findViewById<TextView>(R.id.about_version)
            textView.text = getAppVersion(context)
            val _c = mView.findViewById<TextView>(R.id.about_copyright)
            _c.text = title
            val logo = mView.findViewById<ImageView>(R.id.aboutLogo)
            logo.setOnLongClickListener { v: View? ->
                var _o = "[+]gp->" + isFromGooglePlay(mView.context)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    _o = _o + ", category->" + mView.context.applicationInfo.category
                }
                _c.text = _o
                false
            }
            //dialog.setButton();
            dialog.show()
        }
    }
}
