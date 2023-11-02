package org.apache.mvp;

import org.apache.cordova.domen.Dataset;
import org.apache.mvp.presenter.DeviceCheck;

/*
 * Rotate function
 *
 * */
public interface MainView extends CompatView, DeepLinker {
    void dappend(String s);

    Dataset data(); //Initial data values

    void makeScreen(Dataset screen);

    boolean rotated();

    void hiDeRefreshLayout();

    //void wrapContentRequest();

    DeviceCheck checkDevice();


}
