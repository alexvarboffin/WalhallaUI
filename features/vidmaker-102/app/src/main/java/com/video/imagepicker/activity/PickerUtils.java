package com.video.imagepicker.activity;

import com.video.imagepicker.Constants;

import java.io.File;

public class PickerUtils {
    public static boolean checkFile(File file) {
        if (file == null) {
            return false;
        }
        if (!file.isFile()) {
            return true;
        }
        String name = file.getName();
        if (name.startsWith(".") || file.length() == 0) {
            return false;
        }
        boolean isCheck = false;
        for (int k = 0; k < Constants.FORMAT_IMAGE.size(); k++) {
            if (name.endsWith(Constants.FORMAT_IMAGE.get(k))) {
                isCheck = true;
                break;
            }
        }
        return isCheck;
    }
}
