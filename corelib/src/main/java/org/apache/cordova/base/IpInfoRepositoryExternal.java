package org.apache.cordova.base;

import android.content.Context;

import com.walhalla.ui.DLog;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;


public class IpInfoRepositoryExternal {

    public static final String URL_IP_INFO = "https://ipinfo.io/json";

    public static IPInfoRemote getRemoteInfo() {
        String json = null;
        int cnt = 2;
        while (json == null && cnt > 0) {
            try {
                json = getJSON(URL_IP_INFO, 30000);
                //DLog.d("RemoteInfo: " + cnt + " " + json + "\t");
                cnt--;
            } catch (Exception ex) {
                DLog.d("Error when getting ip: " + ex.getLocalizedMessage());
                return null;
            }
        }
        if (json == null) {
            return null;
        }


        return fetch(json);
    }

    public static IPInfoRemote fetch(String json) {
        IPInfoRemote infoRemote = new IPInfoRemote();
        try {
            //DLog.d("getRemoteInfo: " + json);
            JSONObject object = new JSONObject(new JSONTokener(json));
            infoRemote.ip = object.getString("ip");
            try {
                infoRemote.hostname = object.getString("hostname");
            } catch (JSONException e) {
                DLog.d("@ not hostname");
            }
            infoRemote.city = object.getString("city");
            infoRemote.region = object.getString("region");
            infoRemote.country = object.getString("country");
            infoRemote.loc = object.getString("loc");
            infoRemote.Netname = object.getString("org");//netname

            infoRemote.postal = object.getString("postal");
            infoRemote.timezone = object.getString("timezone");

//            JSONArray jarr = object.getJSONArray("descr");
//            if (jarr != null && jarr.length() > 0) {
//                ArrayList<String> ls = new ArrayList<>();
//                for (int idx = 0; idx < jarr.length(); idx++) {
//                    ls.add(jarr.getString(idx));
//                }
//                infoRemote.description = ls.toArray(new String[0]);
//            }
            if ("null".equals(infoRemote.hostname)) {
                infoRemote.hostname = null;
            }
            if ("null".equals(infoRemote.country)) {
                infoRemote.country = null;
            }
            if ("null".equals(infoRemote.Netname)) {
                infoRemote.Netname = null;
            }
        } catch (Exception e) {
            DLog.handleException(e);
        }
        return infoRemote;
    }

    static HostnameVerifier hostnameVerifier = (hostname, session) -> {
        HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
        return hv.verify("ipinfo.io", session);
    };

    private static String getJSON(String url, int timeout) {

//        try {
            DLog.d( "getJSON: @@@" + Thread.currentThread().getName());
//            TimeUnit.SECONDS.sleep(10);
        HttpsURLConnection connection = null;
        try {
            URL url1 = new URL(url);
            connection = (HttpsURLConnection) url1.openConnection();
            //connection.setHostnameVerifier(hostnameVerifier);
            connection.setRequestMethod("GET");
            //connection.setRequestProperty("Content-length", "0");
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/");
            //connection.addRequestProperty("Authorization", "Bearer 85997aaf83a469");
            connection.setUseCaches(false);
//            connection.setAllowUserInteraction(false);
//            connection.setConnectTimeout(timeout);
//            connection.setReadTimeout(timeout);
            connection.connect();

            //if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
            int status = connection.getResponseCode();

            switch (status) {
                case HttpsURLConnection.HTTP_OK:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    br.close();
                    return sb.toString();

                default:
                    DLog.d("getJSON: " + status);
                    break;
            }

        } catch (UnknownHostException ex) {
            DLog.handleException(ex);
        } catch (IOException ex) {
            DLog.handleException(ex);
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    DLog.handleException(ex);
                }
            }
        }

//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return null;
    }
}
