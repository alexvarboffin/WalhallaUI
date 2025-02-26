package org.apache.cordova.repository.impl;

import android.app.Activity;
import android.content.Context;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.cordova.Chipper;

import org.apache.cordova.domen.BodyClass;

import org.apache.cordova.domen.Dataset;
import org.apache.cordova.domen.UIVisibleDataset;
import org.apache.cordova.repository.AbstractDatasetRepository;

import com.walhalla.ui.DLog;

import org.apache.cordova.utility.TextU;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Get Url from Firebase
 */
public class FirebaseRepository extends AbstractFirebaseRepository {

    private final String KEY_DATASET;

    private static String __e(String aaa) {
        return aaa;
    }

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
                callback.successResponse(new UIVisibleDataset(false, "", false, ""));
            }
        }

    };

    private final ValueEventListener _vel = new ValueEventListener() {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            //DLog.d("! @@-->: [" + dataSnapshot.getKey() + "] " + dataSnapshot.toString());

            // Get Post object and use the values to update the UI
            //Post post = dataSnapshot.getValue(Post.class);
            if (KEY_DATASET.equals(dataSnapshot.getKey())) {
                try {
                    Dataset raw = dataSnapshot.getValue(Dataset.class);
                    if (callback != null && raw != null) {
                        //DLog.d("! @@-->: " + raw.toString());
                        //value.setEnabled(true);
                        //value.url = "https://twitter.com/";

                        UIVisibleDataset value = new UIVisibleDataset(raw);
                        if (value != null) {
                            callback.successResponse(value);
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
                callback.successResponse(new UIVisibleDataset(false, "", false, ""));
            }
        }
    };


//    public FirebaseRepository(RepoCallback mCallback) {
//        super(null, mCallback);
//    }
//
//    public FirebaseRepository(Context context, RepoCallback mCallback) {
//        super(context, mCallback);
//    }

    public FirebaseRepository(Context context) {
        super(context);
        this.KEY_DATASET = TextU.md5(context.getPackageName());
    }

//    public void write() {
//        // Write a message to the database
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        database.setPersistenceEnabled(true);
//        DatabaseReference myRef = database.getReference();
//        myRef.setValue(new UIVisibleDataset(true, "http://ya.ru", true, "ru|ua|de|ch"));
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
//        mDatabase.child(Cst.KEY_USERS).child(userId).setValue(user);
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
}