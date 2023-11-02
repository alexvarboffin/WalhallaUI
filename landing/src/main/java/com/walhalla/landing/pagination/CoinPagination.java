package com.walhalla.landing.pagination;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.Menu;

import com.walhalla.landing.R;

public class CoinPagination {


    final char[] v;


    private CatItem[] categories;

    public CoinPagination(char[] v) {
        this.v = v;
    }

    public void setupDrawer(Context context, Menu menu) {
        //String[] urls = context.getResources().getStringArray(R.array.navigation_url_list);

        String[] titles = context.getResources().getStringArray(R.array.navigation_title_list);

        // icon list
        TypedArray iconTypedArray = context.getResources().obtainTypedArray(R.array.navigation_icon_list);
        Integer[] icons = new Integer[iconTypedArray.length()];
        for (int i = 0; i < iconTypedArray.length(); i++) {
            icons[i] = iconTypedArray.getResourceId(i, R.drawable.ic_home);
        }
        iconTypedArray.recycle();

        categories = new CatItem[titles.length];
        for (int i = 0; i < titles.length; i++) {
            categories[i] = new CatItem(i, titles[i], dec0(v), icons[i]);

            menu.add(R.id.menu_container, categories[i]._id,
                    Menu.NONE, categories[i].title).setCheckable(true).setIcon(categories[i].icon);

        }
    }

    private String dec0(char[] v) {
        return new StringBuilder((String.valueOf(v))).reverse().toString();
    }

    public String getUrl(int c_id) {
        for (CatItem obj : categories) {
            if (c_id == obj._id) {
                return obj.url;
            }
        }
        return "";
    }
}
