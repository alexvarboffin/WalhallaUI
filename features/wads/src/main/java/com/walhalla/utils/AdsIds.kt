package com.walhalla.utils;


abstract class AdsIds {

    public String admob_native_id;
    public String ADMOB_INTER_ID;
    public String admob_reward_ad_id;


    public AdsIds(String admob_native_id, String ADMOB_INTER_ID, String admob_reward_ad_id) {
        this.admob_native_id = admob_native_id;
        this.ADMOB_INTER_ID = ADMOB_INTER_ID;
        this.admob_reward_ad_id = admob_reward_ad_id;
    }
}
