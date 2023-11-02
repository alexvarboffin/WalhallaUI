package org.apache.cordova.http;

import android.content.Context;
import android.os.Build;
import android.webkit.WebSettings;

import com.google.firebase.database.annotations.NotNull;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class MainInterceptor implements Interceptor {

    private final Context context;

    public MainInterceptor(Context context) {
        this.context = context;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
            /*   String uid = "0";
                    long timestamp = (int) (Calendar.getInstance().getTimeInMillis() / 1000);
                    String signature = MD5Util.crypt(timestamp + "" + uid + MD5_SIGN);
                    String base64encode = signature + ":" + timestamp + ":" + uid;
                    base64encode = Base64.encodeToString(base64encode.getBytes(), Base64.NO_WRAP | Base64.URL_SAFE);
*/
        Request request = chain.request();


        //String b = (request.body() == null) ? " - " : request.body().toString();
        //Log.d(String.format("\nrequest:\n%s\nheaders:\n%s", b, request.headers()));

        HttpUrl url = request.url()
                .newBuilder()
                //.addQueryParameter("apikey", signature)
                //.addQueryParameter("appid", this.appName)
                //.addQueryParameter("user_agent", this.ua)
                //.addQueryParameter("method", "getAliasList")
                .build();


        Request.Builder builder = request
                .newBuilder()
                //.addHeader("Authorization", "zui " + base64encode)
                .addHeader("Cache-Control", "max-age=0");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            builder.addHeader("User-Agent", WebSettings.getDefaultUserAgent(context));
        }
        //.addHeader("from_client", "sms-reg")
        request = builder.url(url)
                .build();

        //DLog.d("REST: " + url.toString());
        return chain.proceed(request);
    }
}
