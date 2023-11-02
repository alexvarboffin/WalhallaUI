package org.apache.cordova.fragment;

import android.content.Context;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.ViewModel;
import androidx.fragment.app.Fragment;

import org.apache.cordova.R;


public abstract class BaseFragment extends Fragment {

    protected Callback mCallback;

    @Override
    public void onResume() {
        super.onResume();
//        if (mCallback != null) {
//            //mCallback.hideTabLayout();
//        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            this.mCallback = (Callback) context;
        } else {
            throw new RuntimeException(context + " must implement Callback");
        }
    }

    //Callback
    public interface Callback {

        void onClick(ViewModel navItem);

        void showToolbar(boolean visible);

        void replaceFragment(Fragment preferencesFragment);
    }
}
