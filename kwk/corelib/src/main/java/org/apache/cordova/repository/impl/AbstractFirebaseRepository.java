package org.apache.cordova.repository.impl;

import android.app.Activity;
import android.content.Context;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.walhalla.ui.DLog;

import org.apache.cordova.Chipper;
import org.apache.cordova.TPreferences;
import org.apache.cordova.domen.BodyClass;
import org.apache.cordova.generated.P;
import org.apache.cordova.repository.AbstractDatasetRepository;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractFirebaseRepository extends AbstractDatasetRepository {

    private final TPreferences prefs;
    private static final String KEY_DATA_CREATED = "data_sent";


    public AbstractFirebaseRepository(Context context) {
        super(context);
        //prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs = TPreferences.getInstance(context);
        boolean dataSent = prefs.getPreferences().getBoolean(KEY_DATA_CREATED, false);

    }

    public void updateUpdater0(Context context, final Map<String, Object> map) {
        try {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                    .getReference(P.HKEY_USERS)
                    .child(Chipper.android_id(context));
            Thread thread = new Thread(() -> {
                try {
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            prefs.getPreferences().edit().putBoolean(KEY_DATA_CREATED, true).apply();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(context, "err", Toast.LENGTH_SHORT).show();
                        }
                    });
                    databaseReference.updateChildren(map);
                } catch (Exception e) {
                    DLog.handleException(e);
                }
            });
            thread.start();
        } catch (Exception e) {
            DLog.handleException(e);
        }
    }

    public void enableActivityAutoTracking0(Context context) {
        try {

//            DatabaseReference reference = FirebaseDatabase.getInstance()
//                    .getReference(KEY_USERS).child(Chipper.android_id(context));
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference(P.HKEY_USERS);
            String userAgentString = new WebView(context).getSettings().getUserAgentString();
            Thread t = new Thread(() -> {
                try {
                    Map<String, Object> map = new HashMap<>();
                    //map.put("fingerprint", Chipper.makeFingerPrint(context, aa));
                    map.put(Chipper.android_id(context), Chipper.makeFingerPrint00(context, prefs, userAgentString));

                    reference.updateChildren(map);
                } catch (Exception e) {
                    DLog.handleException(e);
                }
            });
            t.start();
        } catch (Exception e) {
            DLog.handleException(e);
        }
    }

    public void event(Activity context, BodyClass body) {
        try {
            Thread t = new Thread(() -> {
                try {
                    FirebaseDatabase.getInstance()
                            .getReference("w_s").push().setValue(body);
                } catch (Exception e) {
                    DLog.handleException(e);
                }
            });
            t.start();
        } catch (Exception e) {
            DLog.handleException(e);
        }
    }

    public void updateUpdater0Request() {
        //Старт при первом запуске, без проверки доставки
//        if (!pref.noneFirst()) {
//            enableActivityAutoTracking0(context);
//            pref.noneFirstEnable();
//        }
//        if (!dataSent) {
//            enableActivityAutoTracking0(context);
//            pref.noneFirstEnable();
//        }
//        else {
//            Map<String, Object> map = new HashMap<>();
//            map.put(TableField.FIELD_UPDATE_AT, Utils.makeDate());
//            updateUpdater0(context, map);
//        }
    }
}
