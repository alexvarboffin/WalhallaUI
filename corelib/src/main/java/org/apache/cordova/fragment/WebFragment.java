package org.apache.cordova.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.UWView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.apache.P;
import org.apache.Utils;
import org.apache.cordova.ChromeView;
import org.apache.cordova.GConfig;
import org.apache.cordova.R;
import org.apache.cordova.domen.BodyClass;
import com.walhalla.ui.DLog;
import org.apache.mvp.presenter.WPresenterForFragment;

import javax.annotation.Nullable;

public class WebFragment extends Fragment implements ChromeView {

    private static final boolean WEBTITLE_ENABLE = true;

    private GConfig aaa;
    protected ProgressBar progressBar;
    protected FrameLayout clazz1;

    protected ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener;

    public SwipeRefreshLayout swipeRefreshLayout;
    public UWView mWebView;
    private Lecallback listener;
    private String launchUrl;

    private WPresenterForFragment presenter;
    //private String ShowOrHideWebViewInitialUse = "show";

    public static WebFragment newInstance(String url, GConfig config) {
        WebFragment fragment = new WebFragment();
        Bundle bundle = new Bundle();
        bundle.putString(P.ARG_PARAM1, url);
        bundle.putSerializable(P.ARG_PARAM2, config);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            launchUrl = getArguments().getString(P.ARG_PARAM1);
            aaa = (GConfig) getArguments().getSerializable(P.ARG_PARAM2);
        }
        Handler handler = new Handler(Looper.getMainLooper());

        presenter = new WPresenterForFragment(handler, (AppCompatActivity) getActivity(), this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onStop() {
        swipeRefreshLayout.getViewTreeObserver().removeOnScrollChangedListener(mOnScrollChangedListener);
        super.onStop();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        clazz1 = view.findViewById(R.id.browser);
        progressBar = view.findViewById(R.id.progressBar1);

        if (aaa.isPROGRESSBAR_ENABLED()) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
        //Generate Dynamic Gui
        onViewCreated(clazz1, getContext());
    }

    protected void onViewCreated(ViewGroup view, Context context) {
        DLog.d("===================" + view.getClass().getSimpleName());

        //mWebView = privacy.findViewById(R.id.web_view);
        //swipeRefreshLayout = privacy.findViewById(R.id.refresh);
        swipeRefreshLayout = new SwipeRefreshLayout(context);
        swipeRefreshLayout.setBackgroundColor(Color.RED);
        mWebView = new UWView(new ContextThemeWrapper(context, R.style.AppTheme));
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        swipeRefreshLayout.setLayoutParams(lp);
        mWebView.setLayoutParams(lp);

        swipeRefreshLayout.addView(mWebView);
        swipeRefreshLayout.getViewTreeObserver().addOnScrollChangedListener(mOnScrollChangedListener =
                () -> {
                    if (mWebView.getScrollY() == 0)
                        swipeRefreshLayout.setEnabled(true);
                    else
                        swipeRefreshLayout.setEnabled(false);
                });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            mWebView.reload();
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        mWebView.setVisibility(View.VISIBLE);
        //webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        presenter.a123(this, mWebView);
        //view.addView(swipeRefreshLayout, 100,100);
        view.addView(swipeRefreshLayout);
    }


    @Override
    public void onResume() {
        super.onResume();
        presenter.loadUrl(launchUrl, mWebView);
    }


    @Override
    public void onPageStarted(String s) {
        mWebView.setVisibility(View.VISIBLE);
//        // only make it invisible the FIRST time the app is run
//        if (ShowOrHideWebViewInitialUse.equals("show")) {
//            //mWebView.setVisibility(View.INVISIBLE);
//        }
        if (aaa.isPROGRESSBAR_ENABLED()) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPageFinished(String url) {
        String title = mWebView.getTitle();
        if (WEBTITLE_ENABLE && !TextUtils.isEmpty(title) && listener != null) {
            if (title != null && title.startsWith(mWebView.getUrl())) {
                listener.setActionBarTitle(title);
            }
        }
//        if (ShowOrHideWebViewInitialUse.equals("show")) {
//            ShowOrHideWebViewInitialUse = "hide";
//            mWebView.setVisibility(View.VISIBLE);
//        }
        progressBar.setVisibility(View.GONE);
    }

    protected void actionRefresh() {
        String url = mWebView.getUrl();
        if (url != null) {
            mWebView.reload();
            //getContent(url);
            Utils.snack(getActivity(), /*url*/"Data updated.");
        }
    }


    @Override
    public void webClientError(int errorCode, String description, String failingUrl) {
        DLog.d("@@@@@@@@@@@@@@@@@@@@@");
        Toast.makeText(getActivity(), "" + description, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void mAcceptPressed(String url) {
        //makeScreen(new UIVisibleDataset(ScreenType.GAME_VIEW, NONENONE));
    }

    @Override
    public void eventRequest(BodyClass body) {
        presenter.event(body);
    }

    @Override
    public void setErrorPage() {

        //NOT USE ->> getActivity()

        //Toast.makeText(getContext(), "setErrorPage", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void removeErrorPage() {

        //NOT USE ->> getActivity()

        //Toast.makeText(getActivity(), "removeErrorPage", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Lecallback) {
            this.listener = (Lecallback) context;
        } else {
            throw new RuntimeException(context + " must implement Lecallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface Lecallback {
        void setActionBarTitle(String title);
    }
}
