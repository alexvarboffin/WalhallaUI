package com.walhalla.landing.presenter;


import android.content.Context;


import com.walhalla.landing.pagination.CatItem;

import java.util.List;

public interface MainContract {

    interface ViewSimple {
        void showLoading();

        void hideLoading();

        void displayUrl(String url);
    }

    interface View {
        void showSideNavigationMenu();

        void hideSideNavigationMenu();

        void showBottomNavigationMenu();

        void hideBottomNavigationMenu();

        void showToolbar();

        void hideToolbar();

        void loadUrlInWebView(String url);

        void setupNavigationMenus(List<CatItem> navMenuItems, List<CatItem> bottomNavMenuItems);
    }

    interface Presenter {
        void onNavigationItemSelected(String itemId);

        void onBottomNavigationItemSelected(String itemId);

        void fetchRemoteConfig(Context context);
    }
}
