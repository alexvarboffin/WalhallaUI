package org.apache.mvp

import org.apache.cordova.domen.UIVisibleDataset
import org.apache.mvp.presenter.DeviceCheck

/*
* Rotate function
*
* */
interface MainView : CompatView, DeepLinker, ReferrerAdapter.Callback {
    fun data(): UIVisibleDataset? //Initial data values

    fun makeScreen(screen: UIVisibleDataset)

    fun rotated(): Boolean

    fun hiDeRefreshLayout()

    //void wrapContentRequest();
    fun checkDevice(): DeviceCheck?
}
