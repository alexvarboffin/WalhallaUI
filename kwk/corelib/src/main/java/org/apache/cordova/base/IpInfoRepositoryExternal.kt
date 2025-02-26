package org.apache.cordova.base

import com.walhalla.ui.DLog
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.net.UnknownHostException
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLSession

object IpInfoRepositoryExternal {
    const val URL_IP_INFO: String = "https://ipinfo.io/json"

    val remoteInfo: IPInfoRemote?
        get() {
            var json: String? = null
            var cnt = 2
            while (json == null && cnt > 0) {
                try {
                    json = getJSON(
                        URL_IP_INFO,
                        30000
                    )
                    //DLog.d("RemoteInfo: " + cnt + " " + json + "\t");
                    cnt--
                } catch (ex: Exception) {
                    DLog.d("Error when getting ip: " + ex.localizedMessage)
                    return null
                }
            }
            if (json == null) {
                return null
            }


            return fetch(json)
        }

    fun fetch(json: String?): IPInfoRemote {
        val infoRemote = IPInfoRemote()
        try {
            //DLog.d("getRemoteInfo: " + json);
            val `object` = JSONObject(JSONTokener(json))
            infoRemote.ip = `object`.getString("ip")
            try {
                infoRemote.hostname = `object`.getString("hostname")
            } catch (e: JSONException) {
                DLog.d("@ not hostname")
            }
            infoRemote.city = `object`.getString("city")
            infoRemote.region = `object`.getString("region")
            infoRemote.country = `object`.getString("country")
            infoRemote.loc = `object`.getString("loc")
            infoRemote.Netname = `object`.getString("org") //netname

            infoRemote.postal = `object`.getString("postal")
            infoRemote.timezone = `object`.getString("timezone")

            //            JSONArray jarr = object.getJSONArray("descr");
//            if (jarr != null && jarr.length() > 0) {
//                ArrayList<String> ls = new ArrayList<>();
//                for (int idx = 0; idx < jarr.length(); idx++) {
//                    ls.add(jarr.getString(idx));
//                }
//                infoRemote.description = ls.toArray(new String[0]);
//            }
            if ("null" == infoRemote.hostname) {
                infoRemote.hostname = null
            }
            if ("null" == infoRemote.country) {
                infoRemote.country = null
            }
            if ("null" == infoRemote.Netname) {
                infoRemote.Netname = null
            }
        } catch (e: Exception) {
            DLog.handleException(e)
        }
        return infoRemote
    }

    var hostnameVerifier: HostnameVerifier =
        HostnameVerifier { hostname: String?, session: SSLSession? ->
            val hv = HttpsURLConnection.getDefaultHostnameVerifier()
            hv.verify("ipinfo.io", session)
        }

    private fun getJSON(url: String, timeout: Int): String? {
        //        try {

        DLog.d("getJSON: @@@" + Thread.currentThread().name)
        //            TimeUnit.SECONDS.sleep(10);
        var connection: HttpsURLConnection? = null
        try {
            val url1 = URL(url)
            connection = url1.openConnection() as HttpsURLConnection
            //connection.setHostnameVerifier(hostnameVerifier);
            connection.requestMethod = "GET"
            //connection.setRequestProperty("Content-length", "0");
            connection!!.addRequestProperty(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/"
            )
            //connection.addRequestProperty("Authorization", "Bearer 85997aaf83a469");
            connection.useCaches = false
            //            connection.setAllowUserInteraction(false);
//            connection.setConnectTimeout(timeout);
//            connection.setReadTimeout(timeout);
            connection.connect()

            //if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
            val status = connection.responseCode

            when (status) {
                HttpsURLConnection.HTTP_OK, 201 -> {
                    val br = BufferedReader(
                        InputStreamReader(
                            connection.inputStream
                        )
                    )
                    val sb = StringBuilder()
                    var line: String?
                    while ((br.readLine().also { line = it }) != null) {
                        sb.append(line).append("\n")
                    }
                    br.close()
                    return sb.toString()
                }

                else -> DLog.d("getJSON: $status")
            }
        } catch (ex: UnknownHostException) {
            DLog.handleException(ex)
        } catch (ex: IOException) {
            DLog.handleException(ex)
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect()
                } catch (ex: Exception) {
                    DLog.handleException(ex)
                }
            }
        }

        //        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return null
    }
}
