package com.androidsx.rateme

import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.LightingColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.RatingBar.OnRatingBarChangeListener
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.androidsx.rateme.FeedbackDialog.Companion.newInstance
import com.walhalla.ui.DLog.d
import com.walhalla.ui.DLog.handleException
import com.walhalla.ui.R
import com.walhalla.ui.UConst
import com.walhalla.ui.UConst.MARKET_CONSTANT
import com.walhalla.ui.observer.DefaultOnRatingListener
import com.walhalla.ui.observer.RateAppModule
import com.walhalla.ui.plugins.MimeType

class RateMeDialog : DialogFragment {
    // Views
    private lateinit var mView: View
    private lateinit var tView: View
    private lateinit var close: ImageButton
    private lateinit var ratingBar: RatingBar
    private lateinit var stars: LayerDrawable
    private lateinit var rateMe: Button
    private lateinit var noThanks: Button
    private lateinit var share: Button

    // Configuration. It all comes from the builder. On, on resume, from the saved bundle
    private var appPackageName: String? = null
    private var appName: String? = null
    private var headerBackgroundColor = 0
    private var headerTextColor = 0
    private var bodyBackgroundColor = 0
    private var bodyTextColor = 0
    private var feedbackByEmailEnabled = false
    private var feedbackEmail: String? = null
    private var showShareButton = false
    private var appIconResId = 0
    private var lineDividerColor = 0
    private var rateButtonBackgroundColor = 0
    private var rateButtonTextColor = 0
    private var rateButtonPressedBackgroundColor = 0
    private var defaultStarsSelected = 0
    private var iconCloseColor = 0
    private var iconShareColor = 0
    private var showOKButtonByDefault = false
    private var onRatingListener: OnRatingListener? = null

    private val new_rate_module = true
    private var activity: Activity? = null


    constructor()

    constructor(
        appPackageName: String?,
        appName: String?,
        headerBackgroundColor: Int,
        headerTextColor: Int,
        bodyBackgroundColor: Int,
        bodyTextColor: Int,
        feedbackByEmailEnabled: Boolean,
        feedbackEmail: String?,
        showShareButton: Boolean,
        appIconResId: Int,
        lineDividerColor: Int,
        rateButtonBackgroundColor: Int,
        rateButtonTextColor: Int,
        rateButtonPressedBackgroundColor: Int,
        defaultStarsSelected: Int,
        iconCloseColor: Int,
        iconShareColor: Int,
        showOKButtonByDefault: Boolean,
        onRatingListener: OnRatingListener?
    ) {
        this.appPackageName = appPackageName
        this.appName = appName
        this.headerBackgroundColor = headerBackgroundColor
        this.headerTextColor = headerTextColor
        this.bodyBackgroundColor = bodyBackgroundColor
        this.bodyTextColor = bodyTextColor
        this.feedbackByEmailEnabled = feedbackByEmailEnabled
        this.feedbackEmail = feedbackEmail
        this.showShareButton = showShareButton
        this.appIconResId = appIconResId
        this.lineDividerColor = lineDividerColor
        this.rateButtonBackgroundColor = rateButtonBackgroundColor
        this.rateButtonTextColor = rateButtonTextColor
        this.rateButtonPressedBackgroundColor = rateButtonPressedBackgroundColor
        this.defaultStarsSelected = defaultStarsSelected
        this.iconCloseColor = iconCloseColor
        this.iconShareColor = iconShareColor
        this.showOKButtonByDefault = showOKButtonByDefault
        this.onRatingListener = onRatingListener
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        initializeUiFields()
        d("All components were initialized successfully")

        val builder = AlertDialog.Builder(requireActivity())

        setIconsTitleColor(iconCloseColor, iconShareColor)

        stars!!.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP)
        ratingBar!!.onRatingBarChangeListener =
            OnRatingBarChangeListener { ratingBar: RatingBar?, rating: Float, fromUser: Boolean ->
                //DLog.d("@@@" + rating + " " + fromUser);
                if (rating >= RATING_LIMIT) {
                    rateMe!!.visibility = View.VISIBLE
                    noThanks!!.visibility = View.GONE
                } else if (rating > 0.0) {
                    noThanks!!.visibility = View.VISIBLE
                    rateMe!!.visibility = View.GONE
                } else {
                    noThanks!!.visibility = View.GONE
                    rateMe!!.visibility = View.GONE
                }
                defaultStarsSelected = rating.toInt()
            }
        ratingBar!!.stepSize = 1.0f
        ratingBar!!.rating = defaultStarsSelected.toFloat()
        configureButtons()

        try {
            close!!.setOnClickListener { v: View? ->
                RateAppModule.appRated(getActivity(), false) //not rated
                dismiss()
                RateMeDialogTimer.clearSharedPreferences(getActivity())
                d("Clear the shared preferences")
                RateMeDialogTimer.setOptOut(getActivity(), true)
                onRatingListener!!.onRating(
                    OnRatingListener.RatingAction.DISMISSED_WITH_CROSS,
                    ratingBar!!.rating
                )
            }
        } catch (e: Exception) {
            handleException(e)
            dismiss()
        }

        try {
            share!!.visibility = if (showShareButton) View.VISIBLE else View.GONE
            share!!.setOnClickListener { v: View? ->
                startActivity(shareApp(appPackageName))
                d("Share the application")
                onRatingListener!!.onRating(
                    OnRatingListener.RatingAction.SHARED_APP,
                    ratingBar!!.rating
                )
            }
        } catch (e: Exception) {
            handleException(e)
            dismiss()
        }

        return builder.setView(mView).setCustomTitle(tView).setCancelable(false).create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            this.appPackageName = savedInstanceState.getString("appPackageName")
            this.appName = savedInstanceState.getString("appName")
            this.headerBackgroundColor = savedInstanceState.getInt("headerBackgroundColor")
            this.headerTextColor = savedInstanceState.getInt("headerTextColor")
            this.bodyBackgroundColor = savedInstanceState.getInt("bodyBackgroundColor")
            this.bodyTextColor = savedInstanceState.getInt("bodyTextColor")
            this.feedbackByEmailEnabled = savedInstanceState.getBoolean("feedbackByEmailEnabled")
            this.feedbackEmail = savedInstanceState.getString("feedbackEmail")
            this.showShareButton = savedInstanceState.getBoolean("showShareButton")
            this.appIconResId = savedInstanceState.getInt("appIconResId")
            this.lineDividerColor = savedInstanceState.getInt("lineDividerColor")
            this.rateButtonBackgroundColor = savedInstanceState.getInt("rateButtonBackgroundColor")
            this.rateButtonTextColor = savedInstanceState.getInt("rateButtonTextColor")
            this.rateButtonPressedBackgroundColor =
                savedInstanceState.getInt("rateButtonPressedBackgroundColor")
            this.defaultStarsSelected = savedInstanceState.getInt("defaultStarsSelected")
            this.iconCloseColor = savedInstanceState.getInt("iconCloseColor")
            this.iconShareColor = savedInstanceState.getInt("iconShareColor")
            this.showOKButtonByDefault = savedInstanceState.getBoolean("showOKButtonByDefault")
            this.onRatingListener = savedInstanceState.getParcelable("onRatingListener")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("appPackageName", appPackageName)
        outState.putString("appName", appName)
        outState.putInt("headerBackgroundColor", headerBackgroundColor)
        outState.putInt("headerTextColor", headerTextColor)
        outState.putInt("bodyBackgroundColor", bodyBackgroundColor)
        outState.putInt("bodyTextColor", bodyTextColor)
        outState.putBoolean("feedbackByEmailEnabled", feedbackByEmailEnabled)
        outState.putString("feedbackEmail", feedbackEmail)
        outState.putBoolean("showShareButton", showShareButton)
        outState.putInt("appIconResId", appIconResId)
        outState.putInt("lineDividerColor", lineDividerColor)
        outState.putInt("rateButtonBackgroundColor", rateButtonBackgroundColor)
        outState.putInt("rateButtonTextColor", rateButtonTextColor)
        outState.putInt("rateButtonPressedBackgroundColor", rateButtonPressedBackgroundColor)
        outState.putInt("defaultStarsSelected", defaultStarsSelected)
        outState.putInt("iconCloseColor", iconCloseColor)
        outState.putInt("iconShareColor", iconShareColor)
        outState.putBoolean("showOKButtonByDefault", showOKButtonByDefault)
        outState.putParcelable("onRatingListener", onRatingListener)
    }

    override fun onStart() {
        super.onStart()
        val titleDividerId =
            resources.getIdentifier(RESOURCE_NAME, DEFAULT_TYPE_RESOURCE, DEFAULT_PACKAGE)
        val titleDivider = dialog!!.findViewById<View>(titleDividerId)
        titleDivider?.setBackgroundColor(lineDividerColor)
    }

    private fun initializeUiFields() {
        // Main Dialog
        mView = View.inflate(getActivity(), R.layout.rateme__dialog_message, null)
        tView = View.inflate(getActivity(), R.layout.rateme__dialog_title, null)
        close = tView.findViewById(R.id.buttonClose)
        share = tView.findViewById(R.id.buttonShare)
        val txt0 = mView.findViewById<TextView>(R.id.rating_dialog_message)
        if (context != null) {
            val res = requireContext().resources
            val message = String.format(
                res.getString(com.walhalla.shared.R.string.rateme__dialog_first_message),
                res.getString(R.string.app_name)
            )
            txt0.text = message
        }

        rateMe = mView.findViewById(R.id.buttonRateMe)
        noThanks = mView.findViewById(R.id.buttonThanks)

        ratingBar = mView.findViewById(R.id.ratingBar)
        stars = ratingBar.getProgressDrawable() as LayerDrawable
        mView.setBackgroundColor(bodyBackgroundColor)
        tView.setBackgroundColor(headerBackgroundColor)
        (tView.findViewById<View>(R.id.dialog_title) as TextView).setTextColor(headerTextColor)
        val iconImage = mView.findViewById<View>(R.id.app_icon_dialog_rating)
        if (appIconResId == 0) {
            iconImage.visibility = View.GONE
        } else {
            (iconImage as ImageView).setImageResource(appIconResId)
            iconImage.setVisibility(View.VISIBLE)
        }
        txt0.setTextColor(bodyTextColor)

        rateMe.setBackgroundColor(rateButtonBackgroundColor)
        noThanks.setBackgroundColor(rateButtonBackgroundColor)
        rateMe.setTextColor(rateButtonTextColor)
        noThanks.setTextColor(rateButtonTextColor)
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        this.activity = activity
    }

    private fun configureButtons() {
        rateMe!!.setOnClickListener { v: View? ->
            RateAppModule.appRated(getActivity(), true) //rated
            //            if (new_rate_module) {
//                //ReviewManager manager = ReviewManagerFactory.create(this.getContext());
//                ReviewManager manager = new FakeReviewManager(activity);
//
//                Task<ReviewInfo> request = manager.requestReviewFlow();
//                request.addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        // We can get the ReviewInfo object
//                        ReviewInfo reviewInfo = task.getResult();
//                        Task<Void> flow = manager.launchReviewFlow(activity, reviewInfo);
//                        flow.addOnCompleteListener(task0 -> {
//                            if (task0.isSuccessful()) {
//                                DLog.d("@@@" + task0.getResult());
//                                //rateApp0();
//                            } else {
//                                rateApp0();
//                            }
//                        });
//                    } else {
//                        // There was some problem, continue regardless of the result.
//                        rateApp0();
//                    }
//                });
//
//            } else {
//                rateApp0();
//            }
            onRatingListener!!.onRating(
                OnRatingListener.RatingAction.HIGH_RATING_WENT_TO_GOOGLE_PLAY,
                ratingBar!!.rating
            )
            dismiss()
        }

        noThanks!!.setOnClickListener { v: View? ->
            RateAppModule.appRated(getActivity(), true) //rated
            dismiss()

            if (feedbackByEmailEnabled) {
                val dialogMail: DialogFragment = newInstance(
                    feedbackEmail,
                    appName,
                    headerBackgroundColor,
                    bodyBackgroundColor,
                    headerTextColor,
                    bodyTextColor,
                    appIconResId,
                    lineDividerColor,
                    rateButtonTextColor,
                    rateButtonBackgroundColor,
                    ratingBar.rating,
                    onRatingListener
                )

                if (null != fragmentManager) {
                    dialogMail.show(requireFragmentManager(), "feedbackByEmailEnabled")
                }
                d("No: open the feedback dialog")
            } else {
                onRatingListener!!.onRating(
                    OnRatingListener.RatingAction.LOW_RATING,
                    ratingBar.rating
                )
            }
            RateMeDialogTimer.setOptOut(getActivity(), true)
        }
    }

    private fun shareApp(appPackageName: String?): Intent {
        val shareApp = Intent()
        shareApp.setAction(Intent.ACTION_SEND)
        try {
            shareApp.putExtra(Intent.EXTRA_TEXT, MARKET_CONSTANT + appPackageName)
        } catch (anfe: ActivityNotFoundException) {
            shareApp.putExtra(Intent.EXTRA_TEXT, UConst.GOOGLE_PLAY_CONSTANT + appPackageName)
        }
        shareApp.setType(MimeType.TEXT_PLAIN)
        return shareApp
    }

    private fun setIconsTitleColor(colorClose: Int, colorShare: Int) {
        ContextCompat.getDrawable(requireContext(), android.R.drawable.ic_menu_close_clear_cancel)
            ?.setColorFilter(LightingColorFilter(colorClose, colorClose))
        ContextCompat.getDrawable(requireContext(), android.R.drawable.ic_menu_share)
            ?.setColorFilter(LightingColorFilter(colorShare, colorShare))
    }

    class Builder
    /**
     * @param appPackageName package name of the application. Available in `Context.getPackageName()`.
     * @param appName        name of the application. Typically `getResources().getString(R.string.app_name)`.
     */(private val appPackageName: String, private val appName: String) {
        private var headerBackgroundColor = C_B
        private var headerTextColor = C_W
        private var bodyBackgroundColor = Color.DKGRAY
        private var bodyTextColor = C_W
        private var feedbackByEmailEnabled = false
        private var feedbackEmail: String? = null
        private var showShareButton = false
        private var appIconResId = 0
        private var lineDividerColor = LINE_DIVIDER_COLOR_UNSET
        private var rateButtonBackgroundColor = C_B
        private var rateButtonTextColor = C_W
        private var rateButtonPressedBackgroundColor = Color.GRAY
        private var defaultStarsSelected = 0
        private var iconCloseColor = C_W
        private var iconShareColor = C_W
        private var showOKButtonByDefault = true
        private var onRatingListener: OnRatingListener = DefaultOnRatingListener()

        fun setHeaderBackgroundColor(headerBackgroundColor: Int): Builder {
            this.headerBackgroundColor = headerBackgroundColor
            return this
        }

        fun setHeaderTextColor(headerTextColor: Int): Builder {
            this.headerTextColor = headerTextColor
            return this
        }

        fun setBodyBackgroundColor(bodyBackgroundColor: Int): Builder {
            this.bodyBackgroundColor = bodyBackgroundColor
            return this
        }

        fun setBodyTextColor(bodyTextColor: Int): Builder {
            this.bodyTextColor = bodyTextColor
            return this
        }

        /**
         * Enables a second dialog that opens if the rating is low, from which the user can send
         * an e-mail to the provided e-mail address.
         */
        fun enableFeedbackByEmail(email: String?): Builder {
            this.feedbackByEmailEnabled = true
            this.feedbackEmail = email
            return this
        }

        fun setShowShareButton(showShareButton: Boolean): Builder {
            this.showShareButton = showShareButton
            return this
        }

        fun setLineDividerColor(lineDividerColor: Int): Builder {
            this.lineDividerColor = lineDividerColor
            return this
        }

        /**
         * Sets an icon to be placed on the left-hand side of the dialog. No icon will show up
         * otherwise.
         *
         *
         * Careful: before 3.0.0, there was a default icon.
         */
        fun showAppIcon(appIconResId: Int): Builder {
            this.appIconResId = appIconResId
            return this
        }

        fun setRateButtonBackgroundColor(rateButtonBackgroundColor: Int): Builder {
            this.rateButtonBackgroundColor = rateButtonBackgroundColor
            return this
        }

        fun setRateButtonTextColor(rateButtonTextColor: Int): Builder {
            this.rateButtonTextColor = rateButtonTextColor
            return this
        }

        fun setRateButtonPressedBackgroundColor(rateButtonPressedBackgroundColor: Int): Builder {
            this.rateButtonPressedBackgroundColor = rateButtonPressedBackgroundColor
            return this
        }

        fun setDefaultNumberOfStars(numStars: Int): Builder {
            this.defaultStarsSelected = numStars
            return this
        }

        fun setIconCloseColorFilter(iconColor: Int): Builder {
            this.iconCloseColor = iconColor
            return this
        }

        fun setIconShareColorFilter(iconColor: Int): Builder {
            this.iconShareColor = iconColor
            return this
        }

        fun setShowOKButtonByDefault(visible: Boolean): Builder {
            this.showOKButtonByDefault = visible
            return this
        }

        /**
         * @see com.androidsx.rateme.OnRatingListener
         */
        fun setOnRatingListener(onRatingListener: OnRatingListener): Builder {
            this.onRatingListener = onRatingListener
            return this
        }

        fun build(): RateMeDialog {
            if (lineDividerColor == LINE_DIVIDER_COLOR_UNSET) {
                lineDividerColor = headerBackgroundColor
            }
            return RateMeDialog(
                appPackageName,
                appName,
                headerBackgroundColor,
                headerTextColor,
                bodyBackgroundColor,
                bodyTextColor,
                feedbackByEmailEnabled,
                feedbackEmail,
                showShareButton,
                appIconResId,
                lineDividerColor,
                rateButtonBackgroundColor,
                rateButtonTextColor,
                rateButtonPressedBackgroundColor,
                defaultStarsSelected,
                iconCloseColor,
                iconShareColor,
                showOKButtonByDefault,
                onRatingListener
            )
        }

        companion object {
            private const val LINE_DIVIDER_COLOR_UNSET = -1

            private const val C_W = -0x1 //Color white
            private const val C_B = -0x1000000 //black
        }
    }

    companion object {
        private const val RESOURCE_NAME = "titleDivider"
        private const val DEFAULT_TYPE_RESOURCE = "id"
        private const val DEFAULT_PACKAGE = "android"
        private const val RATING_LIMIT = 5.0
    }
}
