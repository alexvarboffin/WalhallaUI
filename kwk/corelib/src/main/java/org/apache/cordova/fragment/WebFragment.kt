package org.apache.cordova.fragment

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.UWView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.walhalla.ui.DLog
import com.walhalla.webview.ChromeView
import com.walhalla.webview.ReceivedError
import org.apache.cordova.Gfg
import org.apache.cordova.R
import org.apache.cordova.domen.BodyClass
import org.apache.cordova.generated.P
import org.apache.mvp.presenter.WPresenterForFragment

class WebFragment : Fragment(), ChromeView {
    private var aaa: Gfg? = null
    protected var progressBar: ProgressBar? = null
    protected lateinit var clazz1: FrameLayout

    protected var mOnScrollChangedListener: ViewTreeObserver.OnScrollChangedListener? = null

    var swipeRefreshLayout: SwipeRefreshLayout? = null
    var mWebView: UWView? = null
    private var listener: Lecallback? = null
    private var launchUrl: String? = null

    private var presenter: WPresenterForFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            launchUrl = requireArguments().getString(P.ARG_PARAM1)
            aaa = requireArguments().getSerializable(P.ARG_PARAM2) as Gfg?
        }
        val handler = Handler(Looper.getMainLooper())

        presenter = WPresenterForFragment(handler, activity as AppCompatActivity?, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onStop() {
        swipeRefreshLayout!!.viewTreeObserver.removeOnScrollChangedListener(mOnScrollChangedListener)
        super.onStop()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clazz1 = view.findViewById(R.id.browser)
        progressBar = view.findViewById(R.id.progressBar1)

        if (aaa!!.isProgressbarEnabled) {
            (progressBar as ProgressBar).setVisibility(View.VISIBLE)
        } else {
            (progressBar as ProgressBar).setVisibility(View.GONE)
        }

        //Generate Dynamic Gui
        context?.let { onViewCreated(clazz1, it) }
    }

    protected fun onViewCreated(view: ViewGroup, context: Context) {
        DLog.d("===================" + view.javaClass.simpleName)

        //mWebView = privacy.findViewById(R.id.web_view);
        //swipeRefreshLayout = privacy.findViewById(R.id.refresh);
        swipeRefreshLayout = SwipeRefreshLayout(context)
        swipeRefreshLayout!!.setBackgroundColor(Color.RED)
        mWebView = UWView(ContextThemeWrapper(context, R.style.AppTheme))
        val lp = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        swipeRefreshLayout!!.layoutParams = lp
        mWebView!!.layoutParams = lp

        swipeRefreshLayout!!.addView(mWebView)
        swipeRefreshLayout!!.viewTreeObserver.addOnScrollChangedListener(
            ViewTreeObserver.OnScrollChangedListener {
                if (mWebView!!.scrollY == 0) swipeRefreshLayout!!.isEnabled =
                    true
                else swipeRefreshLayout!!.isEnabled = false
            }.also { mOnScrollChangedListener = it })

        swipeRefreshLayout!!.setOnRefreshListener {
            mWebView!!.reload()
            if (swipeRefreshLayout!!.isRefreshing) {
                swipeRefreshLayout!!.isRefreshing = false
            }
        }
        mWebView!!.visibility = View.VISIBLE
        //webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        presenter!!.a123(this, mWebView)
        //view.addView(swipeRefreshLayout, 100,100);
        view.addView(swipeRefreshLayout)
    }


    override fun onResume() {
        super.onResume()
        presenter!!.loadUrl(launchUrl, mWebView)
    }


    override fun onPageStarted(s: String?) {
        mWebView!!.visibility = View.VISIBLE
        //        // only make it invisible the FIRST time the app is run
//        if (ShowOrHideWebViewInitialUse.equals("show")) {
//            //mWebView.setVisibility(View.INVISIBLE);
//        }
        if (aaa!!.isProgressbarEnabled) {
            progressBar!!.visibility = View.VISIBLE
        }
    }

    override fun onPageFinished(url: String?) {
        val title = mWebView!!.title
        if (WEBTITLE_ENABLE && !TextUtils.isEmpty(title) && listener != null) {
            if (title != null && title.startsWith(mWebView!!.url!!)) {
                listener!!.setActionBarTitle(title)
            }
        }
        //        if (ShowOrHideWebViewInitialUse.equals("show")) {
//            ShowOrHideWebViewInitialUse = "hide";
//            mWebView.setVisibility(View.VISIBLE);
//        }
        progressBar!!.visibility = View.GONE
    }

    override fun webClientError(failure: ReceivedError) {
        DLog.d("@@@@@@@@@@@@@@@@@@@@@")
        Toast.makeText(activity, "" + failure.description, Toast.LENGTH_SHORT).show()
    }


    //    protected void actionRefresh() {
    //        String url = mWebView.getUrl();
    //        if (url != null) {
    //            mWebView.reload();
    //            //getContent(url);
    //            Utils.snack(getActivity(), /*url*/"Data updated.");
    //        }
    //    }
    fun mAcceptPressed(url: String?) {
        //makeScreen(new UIVisibleDataset(ScreenType.GAME_VIEW, NONENONE));
    }


    fun eventRequest(body: BodyClass?) {
        presenter!!.event(body)
    }


    fun setErrorPage() {
        //NOT USE ->> getActivity()

        //Toast.makeText(getContext(), "setErrorPage", Toast.LENGTH_SHORT).show();
    }

    override fun removeErrorPage() {
        //NOT USE ->> getActivity()

        //Toast.makeText(getActivity(), "removeErrorPage", Toast.LENGTH_SHORT).show();
    }

    override fun setErrorPage(receivedError: ReceivedError) {

    }


    override fun openBrowser(url: String) {
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Lecallback) {
            this.listener = context
        } else {
            throw RuntimeException("$context must implement Lecallback")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface Lecallback {
        fun setActionBarTitle(title: String?)
    }

    companion object {
        private const val WEBTITLE_ENABLE = true

        //private String ShowOrHideWebViewInitialUse = "show";
        @JvmStatic
        fun newInstance(url: String?, config: Gfg?): WebFragment {
            val fragment = WebFragment()
            val bundle = Bundle()
            bundle.putString(P.ARG_PARAM1, url)
            bundle.putSerializable(P.ARG_PARAM2, config)
            fragment.arguments = bundle
            return fragment
        }
    }
}
