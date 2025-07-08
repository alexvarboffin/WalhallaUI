package com.walhalla.utils

import com.walhalla.library.BuildConfig
import com.walhalla.library.Const


//123-456

class AdmobAdsIds(admob_native_id: String?, ADMOB_INTER_ID: String?, admob_reward_ad_id: String?) :
    AdsIds(
        (if ((BuildConfig.DEBUG)) Const.TEST_NATIVE_ID else admob_native_id),
        (if ((BuildConfig.DEBUG)) Const.TEST_INTERSTITIAL_ID else ADMOB_INTER_ID),
        (if ((BuildConfig.DEBUG)) Const.TEST_REWARDED_VIDEO_ID else admob_reward_ad_id)
    )
