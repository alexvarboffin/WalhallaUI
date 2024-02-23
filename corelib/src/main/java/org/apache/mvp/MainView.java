package org.apache.mvp;

import org.apache.cordova.domen.UIVisibleDataset;
import org.apache.mvp.presenter.DeviceCheck;

/*
 * Rotate function
 *
 * */
public interface MainView extends CompatView, DeepLinker {
    void dappend(String s);

    UIVisibleDataset data(); //Initial data values

    void makeScreen(UIVisibleDataset screen);

    boolean rotated();

    void hiDeRefreshLayout();

    //void wrapContentRequest();

    DeviceCheck checkDevice();


}
