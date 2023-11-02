package tk.alexapp.freestuffandcoupons;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.walhalla.ui.Module_U;

import java.io.File;

import tk.alexapp.freestuffandcoupons.adapter.ItemsArrayAdapter;
import tk.alexapp.freestuffandcoupons.domain.TabInfo;
import tk.alexapp.freestuffandcoupons.domain.TabsInfo;

import tk.alexapp.freestuffandcoupons.task.DownloadMainXmlTask;
import tk.alexapp.freestuffandcoupons.task.ResultListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "@@@";
    private static final String XML_URL = "http://topshop21.com/1_freestuffandcoupons.xml";
    private AdView mAdView;


    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FirebaseApp.initializeApp(this);
        try {
            FirebaseMessaging.getInstance().subscribeToTopic("news");
        }catch (Exception e){

        }
        //FirebaseInstanceId.getInstance().getToken();

        deleteDirectoryTree(getCacheDir());

        mAdView = findViewById(R.id.adView);
        //AdRequest adRequest = new AdRequest.Builder().addTestDevice("E25357E6C70450E27B61BDDF14DEF0A1").build();
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice("D835BCD2872E5FA7FB21AB05AB396F5C")
                .build();
        mAdView.loadAd(adRequest);
        //mAdView.setAdListener(new AdListener());
        downloadMainXml();
    }

    private void downloadMainXml() {
        //TODO: show loading
        final ProgressBar progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(ProgressBar.VISIBLE);
        new DownloadMainXmlTask(new ResultListener<TabsInfo>() {
            @Override
            public void success(TabsInfo tabsInfo) {
                //TODO: hide loading
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                initTabs(tabsInfo);
            }

            @Override
            public void error() {
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                //TODO1: show error dialog
            }
        }).execute(XML_URL);
    }

    private void initTabs(TabsInfo tabsInfo) {
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), tabsInfo);
        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_TAB_INFO = "tab_info";
        private TabInfo tabInfo;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, TabInfo tabInfo) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putSerializable(ARG_TAB_INFO, tabInfo);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            tabInfo = (TabInfo) getArguments().getSerializable(ARG_TAB_INFO);
            int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            createTabListView(rootView);
            return rootView;
        }

        private void createTabListView(View rootView) {
            ListView listView = rootView.findViewById(R.id.itemsList);
            ItemsArrayAdapter adapter = new ItemsArrayAdapter(getActivity(), tabInfo);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener((parent, view, position, id) -> {
                String link = tabInfo.getItemInfo(position).getLink();
                Log.v(TAG, "open link: " + link);
                final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(link));
                startActivity(intent);
            });
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private TabsInfo tabsInfo;

        public SectionsPagerAdapter(FragmentManager fm, TabsInfo tabsInfo) {
            super(fm);
            this.tabsInfo = tabsInfo;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1, tabsInfo.getTabInfo(position));
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.tab0Title);
                case 1:
                    return getResources().getString(R.string.tab1Title);
                case 2:
                    return getResources().getString(R.string.tab2Title);
                default:
                    return getResources().getString(R.string.unknownTabTitle);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_about:
                Module_U.aboutDialog(this);
                return true;

            case R.id.action_privacy_policy:
                //Module_U.openBrowser(this, getString(R.string.privacy_policy_url));
                return true;

            case R.id.action_rate_app:
                Module_U.rateUs(this);
                return true;

            case R.id.action_share_app:
                Module_U.shareThisApp(this);
                return true;

            case R.id.action_exit:
                this.finish();
                return true;

            case R.id.action_feedback:
                Module_U.feedback(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Deletes a directory tree recursively.
     */
    public static void deleteDirectoryTree(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteDirectoryTree(child);
            }
        }

        boolean b = fileOrDirectory.delete();
    }

}