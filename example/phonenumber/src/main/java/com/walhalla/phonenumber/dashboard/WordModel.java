package com.walhalla.phonenumber.dashboard;


import com.google.firebase.database.PropertyName;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public abstract class WordModel //implements SortedListAdapter.ViewModel
{
    public boolean isInstalled; // Новое поле

    @SerializedName("_id")
    @PropertyName("_id")
    @Expose
    public int _id;
    @PropertyName("_q")
    @SerializedName("_q")
    @Expose
    public String q;


    @SerializedName("color")
    @Expose
    public String color;
    public int time;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WordModel)) return false;
        WordModel wordModel = (WordModel) o;
        if (_id != wordModel._id) return false;
        return q != null ? q.equals(wordModel.q) : wordModel.q == null;
    }

    @Override
    public int hashCode() {
        int result = _id;
        result = 31 * result + (q != null ? q.hashCode() : 0);
        return result;
    }

    public <T> boolean isSameModelAs(@androidx.annotation.NonNull T model) {
        return equals(model);
    }

    public <T> boolean isContentTheSameAs(@androidx.annotation.NonNull T model) {
        return equals(model);
    }
}
