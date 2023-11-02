package org.apache.mvp;

import org.apache.cordova.repository.AbstractDatasetRepository;


public interface CompatView {

    Integer orientation404();//orientationGame

    Integer orientationWeb();

    //boolean checkUpdate();

    boolean webTitle();

    AbstractDatasetRepository makeTracker();
}