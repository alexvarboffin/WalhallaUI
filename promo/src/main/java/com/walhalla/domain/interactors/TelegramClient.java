package com.walhalla.domain.interactors;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class TelegramClient {

    private final String var0;
    private final String token;

    public TelegramClient(String chatId, String token) {
        this.var0 = chatId;
        this.token = token;
    }

    public void sendMessage(String message, Callback callback) {


        OkHttpClient client = new OkHttpClient();
        String body = "chat_id=" + var0 + "&text=" + message;


        RequestBody data;
        data = MultipartBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"), body);


        Request request = new Request.Builder()
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; rv:50.0) Gecko/20100101 Firefox/50.0") //optional
                .url("https://api.telegram.org/bot" + token + "/sendMessage")
                .post(data) //call post
                .build();


        Call tk = client.newCall(request);
        tk.enqueue(callback);
    }


    public void sendDocument(String document, String caption, Callback callback) {

        final File doc = new File(document);
        OkHttpClient client = new OkHttpClient();


//        String body = "chat_id=" + var0 +
//                "&caption=" + caption +
//                //"&document=" + document +
//                "&disable_notification=true";


//        RequestBody data;
//        data = MultipartBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"), body);

        RequestBody data = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("document", doc.getName(),
                        //"application/octet-stream"
                        RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"), doc))
                .addFormDataPart("chat_id", var0)
                .addFormDataPart("caption", caption)
                .addFormDataPart("disable_notification", String.valueOf(true))
                .build();


        Request request = new Request.Builder()
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; rv:50.0) Gecko/20100101 Firefox/50.0") //optional
                .url("https://api.telegram.org/bot" + token + "/sendDocument")
                .post(data) //call post
                .build();
        client.newCall(request).enqueue(callback);
    }


    public void sendPhoto(String photo, String caption) {


        OkHttpClient client = new OkHttpClient();
        String body = "chat_id=" + var0 +
                "&caption=" + caption +
                "&photo=" + photo + "&disable_notification=true";


        RequestBody data;
        data = MultipartBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"), body);


        Request request = new Request.Builder()
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; rv:50.0) Gecko/20100101 Firefox/50.0") //optional
                .url("https://api.telegram.org/bot" + token + "/sendPhoto")
                .post(data) //call post
                .build();


        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("onFailure: " + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //throw new IOException("Unexpected code " + response);
//                }
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    System.out.println("onResponse: " + responseBody.string());
                }
            }

        });
    }


    public void getUpdates(String message) {

        OkHttpClient client = new OkHttpClient();
        String body = "chat_id=" + var0 + "&text=" + message;


        RequestBody data;
        data = MultipartBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"), body);


        Request request = new Request.Builder()
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; rv:50.0) Gecko/20100101 Firefox/50.0") //optional
                .url("https://api.telegram.org/bot" + token + "/getUpdates")
                .post(data) //call post
                .build();


        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("onFailure: " + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                ResponseBody body = response.body();
                if (body != null) {
                    System.out.println("onResponse: " + body.string());
                }
            }

        });
    }


//        public void sendLocation(Location location) {
//            String token = ;
//            String var0 = ;
//
//
//            OkHttpClient client = new OkHttpClient();
//            String body = "chat_id=" + var0 + "&latitude=" + location.getLatitude()
//                    + "&longitude=" + location.getLongitude()
//                    + "&disable_notification=true";
//
//
//            RequestBody data;
//            data = MultipartBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"), body);
//
//
//            Request request = new Request.Builder()
//                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; rv:50.0) Gecko/20100101 Firefox/50.0") //optional
//                    .url("https://api.telegram.org/bot" + token + "/sendLocation")
//                    .post(data) //call post
//                    .build();
//
//
//            client.newCall(request).enqueue(new ExtractorViewCallback() {
//
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    System.out.makeLog("onFailure: " + e.getLocalizedMessage());
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
////                if (!response.isSuccessful()) {
//                    //throw new IOException("Unexpected code " + response);
////                }
//
//                    ResponseBody body = response.body();
//                    System.out.makeLog("onResponse: " + body.string());
//                }
//
//            });
//        }

}

