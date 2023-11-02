package org.apache.cordova.v70.app;

import android.content.Context;
import android.net.ConnectivityManager;

import com.walhalla.ui.DLog;

public class HUtils {

    public static boolean isConnected(Context context) {
       try {
           ConnectivityManager service = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
           // Check for network connections
           if (service.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                   service.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                   service.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                   service.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
               // if connected with internet
               return true;

           } else if (
                   service.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                           service.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
               return false;
           }
       }catch (Exception e){
           DLog.handleException(e);
       }
        return false;
    }
}
