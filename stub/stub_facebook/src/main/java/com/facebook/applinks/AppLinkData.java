package com.facebook.applinks;

import android.content.Context;

import androidx.annotation.Nullable;

public class AppLinkData {

    public static void fetchDeferredAppLinkData(Context x, CompletionHandler c) {
    }

    public String getPromotionCode() {
        return "";
    }

    public Object getAppLinkData() {
        return "";
    }

    public Object getTargetUri() {
        return "";
    }

    public String getRef() {
        return null;
    }

    public Object getRefererData() {
        return "";
    }

    public interface CompletionHandler {
        /**
         * This method is called when deferred app link data has been fetched. If no app link data was
         * found, this method is called with null
         *
         * @param appLinkData The app link data that was fetched. Null if none was found.
         */
        void onDeferredAppLinkDataFetched(@Nullable AppLinkData appLinkData);
    }
}
