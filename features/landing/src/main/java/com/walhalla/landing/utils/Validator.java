package com.walhalla.landing.utils;

import com.walhalla.ui.DLog;

public class Validator {

    private static final long DATE_FIRSTLAUNCH = 1719828120344L;
    private static final int delay = 3 * 24 * 60 * 60 * 1000;

    public Validator() {
    }

    public static boolean validate0() {
        DLog.d("{VALIDATOR}" + System.currentTimeMillis());
        return System.currentTimeMillis() >= DATE_FIRSTLAUNCH + delay;
    }
}
