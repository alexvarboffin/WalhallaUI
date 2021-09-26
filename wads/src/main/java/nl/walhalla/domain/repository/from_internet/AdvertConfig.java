package nl.walhalla.domain.repository.from_internet;

import android.util.SparseArray;

public class AdvertConfig {

    public String app_id;
    public String interstitial_ad_unit_id;
    public final SparseArray<String> banner_ad_unit_id = new SparseArray<>();

    public static Builder newBuilder() {
        return new AdvertConfig().new Builder();
    }

    public class Builder {

        public Builder() {
        }

        public AdvertConfig build() {
            return AdvertConfig.this;
        }

        public Builder setAppId(String app_id) {
            AdvertConfig.this.app_id = app_id;
            return this;
        }

        public Builder setIntersitial(String app_id) {
            AdvertConfig.this.interstitial_ad_unit_id = app_id;
            return this;
        }

        public Builder setBannerId(String app_id) {
            AdvertConfig.this.banner_ad_unit_id.put(0, app_id);
            return this;
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
}