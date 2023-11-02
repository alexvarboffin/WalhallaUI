package org.apache.cordova;

import org.apache.cordova.enumer.UrlSaver;

import java.io.Serializable;

public class GConfig implements Serializable {

    public final UrlSaver SAVE_URL_LOCAL_TYPE;
    public final boolean TOOLBAR_ENABLED;
    public final boolean PROGRESSBAR_ENABLED;

    public final boolean WRAP_ENABLED;
    public final boolean ENABLE_TRACKER;
    public GConfig(boolean en_toolbar, boolean en_progressbar, UrlSaver saver, //Save url or connect to tracker
                   boolean en_wrap, boolean en_tracker) {
        this.TOOLBAR_ENABLED = en_toolbar;
        this.PROGRESSBAR_ENABLED = en_progressbar;

        this.SAVE_URL_LOCAL_TYPE = saver;
        this.WRAP_ENABLED = en_wrap;
        this.ENABLE_TRACKER=en_tracker;
    }

}
