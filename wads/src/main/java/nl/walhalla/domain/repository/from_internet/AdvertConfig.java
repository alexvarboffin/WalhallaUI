package nl.walhalla.domain.repository.from_internet;

import android.util.SparseArray;

public interface AdvertConfig {


    //    String[] banner_ad_unit_id();
//    Interface
    String application_id();

    SparseArray<String> banner_ad_unit_id();

    String interstitial_ad_unit_id();


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
}