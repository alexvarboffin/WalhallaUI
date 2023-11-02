package com.appsflyer;

import java.util.Map;

public interface AppsFlyerConversionListener {
    void onConversionDataSuccess(Map<String, Object> conversionData);

    void onConversionDataFail(String errorMessage);

    void onAppOpenAttribution(Map<String, String> attributionData);

    void onAttributionFailure(String errorMessage);
}
