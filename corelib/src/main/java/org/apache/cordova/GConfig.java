package org.apache.cordova;

import org.apache.cordova.enumer.UrlSaver;

import java.io.Serializable;

public class GConfig implements Serializable {


    public boolean isTOOLBAR_ENABLED() {
        return TOOLBAR_ENABLED;
    }

    public boolean isPROGRESSBAR_ENABLED() {
        return PROGRESSBAR_ENABLED;
    }

    public UrlSaver getSAVE_URL_LOCAL_TYPE() {
        return SAVE_URL_LOCAL_TYPE;
    }

    public boolean isWRAPenabled() {
        return wrapEnabled;
    }

    public boolean isENABLE_TRACKER() {
        return ENABLE_TRACKER;
    }

    private final boolean TOOLBAR_ENABLED;
    private final boolean PROGRESSBAR_ENABLED;
    private final UrlSaver SAVE_URL_LOCAL_TYPE;

    public void setWrapEnabled(boolean wrapEnabled) {
        this.wrapEnabled = wrapEnabled;
    }

    private boolean wrapEnabled;
    private final boolean ENABLE_TRACKER;

    private GConfig(Builder builder) {
        this.TOOLBAR_ENABLED = builder.TOOLBAR_ENABLED;
        this.PROGRESSBAR_ENABLED = builder.PROGRESSBAR_ENABLED;
        this.SAVE_URL_LOCAL_TYPE = builder.SAVE_URL_LOCAL_TYPE;
        this.wrapEnabled = builder.WRAP_ENABLED;
        this.ENABLE_TRACKER = builder.ENABLE_TRACKER;
    }

    public boolean isSwipeEnabled() {
        return true;
    }

    public static class Builder {
        private boolean TOOLBAR_ENABLED;
        private boolean PROGRESSBAR_ENABLED;
        private UrlSaver SAVE_URL_LOCAL_TYPE;
        private boolean WRAP_ENABLED;
        private boolean ENABLE_TRACKER;

        public Builder() {
            // Set default values if needed
            this.TOOLBAR_ENABLED = true;
            this.PROGRESSBAR_ENABLED = true;
            this.SAVE_URL_LOCAL_TYPE = UrlSaver.OH_NONE;
            this.WRAP_ENABLED = false;
            this.ENABLE_TRACKER = false;
        }

        public Builder toolbarEnabled(boolean toolbarEnabled) {
            this.TOOLBAR_ENABLED = toolbarEnabled;
            return this;
        }

        public Builder progressbarEnabled(boolean progressbarEnabled) {
            this.PROGRESSBAR_ENABLED = progressbarEnabled;
            return this;
        }

        public Builder saveUrlLocalType(UrlSaver saveUrlLocalType) {
            this.SAVE_URL_LOCAL_TYPE = saveUrlLocalType;
            return this;
        }

        public Builder wrapEnabled(boolean wrapEnabled) {
            this.WRAP_ENABLED = wrapEnabled;
            return this;
        }

        public Builder enableTracker(boolean enableTracker) {
            this.ENABLE_TRACKER = enableTracker;
            return this;
        }

        public GConfig build() {
            return new GConfig(this);
        }
    }
}
