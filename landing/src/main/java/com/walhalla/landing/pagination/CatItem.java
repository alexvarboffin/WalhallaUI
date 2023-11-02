package com.walhalla.landing.pagination;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Keep
public class CatItem implements Serializable {
    @SerializedName("_id")
    @Expose
    public Integer _id = 0;

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("url")
    @Expose
    public String url;

    @SerializedName("icon")
    @Expose
    public Integer icon = 0;

    public CatItem(int id, String tag, String url, Integer icon) {
        this._id = id;
        this.title = tag;
        this.icon = icon;
        this.url = url;
    }

    public CatItem() {
        //keep
    }

//        @Override
//        public String toString() {
//            return "CatItem{" +
//                    "_id=" + _id +
//                    ", title='" + title + '\'' +
//                    ", url='" + url + '\'' +
//                    ", icon=" + icon +
//                    '}';
//        }
}
