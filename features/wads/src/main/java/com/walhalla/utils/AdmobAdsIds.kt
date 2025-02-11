package com.walhalla.utils;

import static com.walhalla.library.Const.TEST_INTERSTITIAL_ID;
import static com.walhalla.library.Const.TEST_NATIVE_ID;
import static com.walhalla.library.Const.TEST_REWARDED_VIDEO_ID;

import com.walhalla.library.BuildConfig;

public class AdmobAdsIds extends AdsIds {


    public AdmobAdsIds(String admob_native_id, String ADMOB_INTER_ID, String admob_reward_ad_id) {
        super(
                (BuildConfig.DEBUG) ? TEST_NATIVE_ID : admob_native_id,
                (BuildConfig.DEBUG) ? TEST_INTERSTITIAL_ID : ADMOB_INTER_ID,
                (BuildConfig.DEBUG) ? TEST_REWARDED_VIDEO_ID : admob_reward_ad_id
        );
    }
}
