package com.walhalla.domain.repository

import android.view.ViewGroup
import com.google.android.gms.ads.AdView

/**
 * Created by combo on 18.07.2017.
 */
interface AdvertRepository {
    fun getNewAdsBanner(viewGroup: ViewGroup): AdView // void initialize(Context context);
}
