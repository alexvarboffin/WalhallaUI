package org.apache.cordova.fragment

import android.content.Context
import androidx.appcompat.ViewModel
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    protected var mCallback: Callback? = null

    override fun onResume() {
        super.onResume()
        //        if (mCallback != null) {
//            //mCallback.hideTabLayout();
//        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Callback) {
            this.mCallback = context
        } else {
            throw RuntimeException("$context must implement Callback")
        }
    }

    //Callback
    interface Callback {
        fun onClick(navItem: ViewModel?)

        fun showToolbar(visible: Boolean)

        fun replaceFragment(preferencesFragment: Fragment?)
    }
}
