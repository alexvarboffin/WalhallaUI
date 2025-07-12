package com.walhalla.intentresolver;

import android.content.Context;
import android.net.Uri;

import java.io.File;

public interface UIntent {
   void shareMp4Selector(Context context, File file, Uri uri);

    boolean isClientPackage(String packageName);

    void videoShare(Context context, String path);
}
