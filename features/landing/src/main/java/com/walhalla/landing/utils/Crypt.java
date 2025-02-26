package com.walhalla.landing.utils;

public class Crypt {
    public static int[] enco(String key) {
        String m = new StringBuilder((String.valueOf(key))).reverse().toString();
        char[] cc = m.toCharArray();
        int[] n = new int[cc.length];

        for (int i = cc.length - 1; i >= 0; i--) {
            n[i] = cc[i];
        }
        return n;
    }
}
