package org.apache.cordova;

import org.apache.cordova.enumer.UrlSaver;

import java.io.Serializable;

public class Gfg implements Serializable {


    public boolean isToolbarEnabled() {
        return toolbarEnabled;
    }

    public boolean isProgressbarEnabled() {
        return progressbarEnabled;
    }

    public UrlSaver getSaver() {
        return saver;
    }

    public boolean isWRAPenabled() {
        return wrapEnabled;
    }

    public boolean isEnableTracker() {
        return enableTracker;
    }

    private final boolean toolbarEnabled;
    private final boolean progressbarEnabled;
    private final UrlSaver saver;

    public void setWrapEnabled(boolean wrapEnabled) {
        this.wrapEnabled = wrapEnabled;
    }

    private boolean wrapEnabled;
    private final boolean enableTracker;

    private Gfg(Builder builder) {
        this.toolbarEnabled = builder.TOOLBAR_ENABLED;
        this.progressbarEnabled = builder.PROGRESSBAR_ENABLED;
        this.saver = builder.SAVE_URL_LOCAL_TYPE;
        this.wrapEnabled = builder.WRAP_ENABLED;
        this.enableTracker = builder.ENABLE_TRACKER;
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

        public Gfg build() {
            return new Gfg(this);
        }
    }
}
