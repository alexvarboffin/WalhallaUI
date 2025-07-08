package com.walhalla.domain.repository.from_internet

import com.walhalla.library.Const.TEST_BANNER_ADS

class AdvertConfig {
    var app_id: String? = null
    var interstitial_ad_unit_id: String? = null

    val banner_ad_unit_id: android.util.SparseArray<String> = android.util.SparseArray()

    inner class Builder {
        fun build(): AdvertConfig {
            return this@AdvertConfig
        }

        fun setAppId(app_id: String?): Builder {
            this@AdvertConfig.app_id = app_id
            return this
        }

        fun setIntersitial(app_id: String?): Builder {
            this@AdvertConfig.interstitial_ad_unit_id = app_id
            return this
        }

        fun setBannerId(app_id: String?): Builder {
            banner_ad_unit_id.put(
                0,
                if (com.walhalla.library.BuildConfig.DEBUG) TEST_BANNER_ADS else app_id
            )
            return this
        }
    }

    //    String[] banner_ad_unit_id();
    //    Interface
    //    Class
    //    final String application_id;
    //    final String[] banner_ad_unit_id;
    //    final String interstitial_ad_unit_id;
    //
    //
    //    public AdvertConfig(String application_id, String[] banner_ad_unit_id) {
    //        this(application_id, banner_ad_unit_id, null);
    //    }
    //
    //    public AdvertConfig(String application_id, String[] banner_ad_unit_id, String interstitial_ad_unit_id) {
    //        this.banner_ad_unit_id = banner_ad_unit_id;
    //        this.application_id = application_id;
    //        this.interstitial_ad_unit_id = interstitial_ad_unit_id;
    //    }
    //
    //    public boolean isAdsEnable() {
    //        return (this.banner_ad_unit_id != null
    //                && !(this.banner_ad_unit_id.length == 0));
    //    }


    companion object {
        @JvmStatic
        fun newBuilder(): Builder {
            return AdvertConfig().Builder()
        }
    }
}