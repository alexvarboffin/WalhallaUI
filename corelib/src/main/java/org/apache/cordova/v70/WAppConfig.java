package org.apache.cordova.v70;

public class WAppConfig {
    // list of file extensions for download,
    // if webview URL ends with this extension, that file will be downloaded via download manager,
    // keep this array empty if you do not want to use download manager
    public static final String[] DOWNLOAD_FILE_TYPES = {
            ".zip", ".rar", ".pdf", ".doc", ".xls",
            ".mp3", ".wma", ".ogg", ".m4a", ".wav",
            ".avi", ".mov", ".mp4", ".mpg", ".3gp"
    };
}
