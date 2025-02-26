package com.walhalla.landing;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.Menu;

import com.walhalla.landing.pagination.CatItem;


public class MultiplePagination {


    private CatItem[] categories;

    public void setupDrawer(Context context, Menu menu) {
        String[] urls = context.getResources().getStringArray(R.array.navigation_url_list);
        String[] titles = context.getResources().getStringArray(R.array.navigation_title_list);

        // icon list
        TypedArray iconTypedArray = context.getResources().obtainTypedArray(R.array.navigation_icon_list);
        Integer[] icons = new Integer[iconTypedArray.length()];
        for (int i = 0; i < iconTypedArray.length(); i++) {
            icons[i] = iconTypedArray.getResourceId(i, -1);
        }
        iconTypedArray.recycle();

        categories = new CatItem[titles.length];

        if (icons.length < urls.length) {
            for (int i = 0; i < titles.length; i++) {
                categories[i] = new CatItem(i, titles[i], urls[i], R.drawable.ic_link);
            }
        } else {
            for (int i = 0; i < titles.length; i++) {
                categories[i] = new CatItem(i, titles[i], urls[i], icons[i]);
            }
        }

        for (final CatItem category : categories) {
            menu.add(R.id.menu_container, category._id, Menu.NONE, category.title)
                    .setCheckable(true)
                    .setIcon(category.icon);
        }
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
