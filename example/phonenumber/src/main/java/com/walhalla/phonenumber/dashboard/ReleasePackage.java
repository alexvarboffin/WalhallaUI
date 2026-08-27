package com.walhalla.phonenumber.dashboard;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Apps published together as one Play release package (sms excluded).
 */
public final class ReleasePackage {

    /** Shared tag shown on every item in this group. */
    public static final String TAG = "RP-1";
    public static final String LABEL = "ЕДИНЫЙ ПАКЕТ · RP-1";

    private static final Set<String> PACKAGES = Collections.unmodifiableSet(new LinkedHashSet<>(Arrays.asList(
            "com.walhalla.meccamedina",
            "ai.meccamedinatv.mekkalive.online",
            "a.bubblelevel.spiritpro",
            "com.walhalla.vibro",
            "com.walhalla.qrcode"
    )));

    private ReleasePackage() {
    }

    public static boolean contains(String packageName) {
        return packageName != null && PACKAGES.contains(packageName);
    }

    public static Set<String> packages() {
        return PACKAGES;
    }
}
