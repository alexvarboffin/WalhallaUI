package com.walhalla.library.activity

import android.app.Activity
import androidx.annotation.Keep
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentInformation.OnConsentInfoUpdateFailureListener
import com.google.android.ump.ConsentInformation.OnConsentInfoUpdateSuccessListener
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.walhalla.wads.DLog.d

@Keep
class GDPR {
    private var consentInformation: ConsentInformation? = null
    private var mConsentForm: ConsentForm? = null

    fun init(activity: Activity) {
//        ConsentDebugSettings debugSettings = new ConsentDebugSettings.Builder(activity)
//                .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
//                .addTestDeviceHashedId("443D3BC0E32E203FC1EA78DAD213A128")
//                .addTestDeviceHashedId("8808EDEFBED3C7B6955EAA2FEF5417E0")
//                .build();


//        ConsentRequestParameters params = new ConsentRequestParameters
//                .Builder()
//                .setConsentDebugSettings(debugSettings)
//                .build();


        // Set tag for under age of consent. false means users are not under
        // age.


        val params = ConsentRequestParameters.Builder()
            .setTagForUnderAgeOfConsent(false)
            .build()



        consentInformation = UserMessagingPlatform.getConsentInformation(activity)
        //@@@@@@@@@@@
        //consentInformation.reset();
        consentInformation?.requestConsentInfoUpdate(
            activity,
            params, OnConsentInfoUpdateSuccessListener {
                // The consent information state was updated.
                // You are now ready to check if a form is available.
                //DLog.d("@isConsentFormAvailable@" + consentInformation.isConsentFormAvailable());

                consentInformation?.let {
                    if (it.isConsentFormAvailable) {
                        loadForm(activity)
                    }
                }


            },
            OnConsentInfoUpdateFailureListener { formError ->
                d(
                    "@@" + formError.message
                )
            })
    }


    private fun loadForm(activity: Activity) {
        // Loads a consent form. Must be called on the main thread.
        UserMessagingPlatform.loadConsentForm(
            activity,
            { consentForm ->
                mConsentForm = consentForm
                if (consentInformation!!.consentStatus == ConsentInformation.ConsentStatus.REQUIRED) {
                    mConsentForm!!.show(
                        activity
                    ) {
                        if (consentInformation!!.consentStatus == ConsentInformation.ConsentStatus.OBTAINED) {
                            // App can start requesting ads.
                        }
                        // Handle dismissal by reloading form.
                        loadForm(activity)
                    }
                }
            },
            {
                // Handle the error.
            }
        )
    }
}
