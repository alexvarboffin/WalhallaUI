package com.walhalla.phonenumber.dashboard;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppModel extends WordModel {


    //    public String name;
    @SerializedName("project")
    @Expose
    public String projectName;
    @SerializedName("privacy_policy")
    @Expose
    public String privacy_policy;


    @SerializedName("features")
    @Expose
    public String features;


    @SerializedName("name")
    @Expose
    public String name;
    public String app_id;

    @Override
    public <T> boolean isSameModelAs(@NonNull T model) {
        return false;
    }

    @Override
    public <T> boolean isContentTheSameAs(@NonNull T model) {
        return false;
    }
}
