package com.walhalla.phonenumber.apps.adapter.appitem;

public class AppObj {

    private int id;
    public String url;
    private String title;
    private String rate;
    private String count;
    public boolean isInstalled; // Новое поле

    public AppObj(int id, String url, String title, String rate, String count, boolean isInstalled) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.rate = rate;
        this.count = count;
        this.isInstalled = isInstalled;
    }

    public int getId() {
        return id;
    }


    public String getTitle() {
        return title;
    }

    public String getRate() {
        return rate;
    }

    public String getCount() {
        return count;
    }


}
