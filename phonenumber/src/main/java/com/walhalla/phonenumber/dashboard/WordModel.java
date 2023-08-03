package com.walhalla.phonenumber.dashboard;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public abstract class WordModel implements SortedListAdapter.ViewModel{
    public boolean isInstalled; // Новое поле

    @SerializedName("_id")
    @Expose
    public int _id;

    @SerializedName("_q")
    @Expose
    public String q;
    @SerializedName("color")
    @Expose
    public String color;
    public String getWord() {
        return q;
    }

    public int getRank() {
        return _id;
    }

    public WordModel(int _id, String q) {
        this._id = _id;
        this.q = q;
    }

    public WordModel() {
    }
}
