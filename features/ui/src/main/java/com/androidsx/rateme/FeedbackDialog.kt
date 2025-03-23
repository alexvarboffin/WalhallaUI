package com.androidsx.rateme

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.walhalla.ui.DLog.nonNull
import com.walhalla.ui.R
import com.walhalla.ui.utils.PackageUtils

class FeedbackDialog : DialogFragment() {
    // Views
    private lateinit var confirmDialogTitleView: View
    private lateinit var confirmDialogView: View
    private lateinit var cancel: Button
    private lateinit var yes: Button

    private var onActionListener: OnRatingListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        initializeUiFieldsDialogGoToMail()

        //Log.d(TAG, "All components were initialized successfully");
        cancel.setOnClickListener { v: View? ->
            dismiss()
            onActionListener!!.onRating(
                OnRatingListener.RatingAction.LOW_RATING_REFUSED_TO_GIVE_FEEDBACK,
                requireArguments().getFloat(EXTRA_RATING_BAR)
            )
        }

        yes.setOnClickListener { v: View? ->
            if (nonNull(arguments)) {
                goToMail(requireArguments().getString(EXTRA_APP_NAME))
            }
            onActionListener!!.onRating(
                OnRatingListener.RatingAction.LOW_RATING_GAVE_FEEDBACK,
                requireArguments().getFloat(EXTRA_RATING_BAR)
            )
            //Log.d(TAG, "Agreed to provide feedback");
            dismiss()
        }

        return builder.setCustomTitle(confirmDialogTitleView).setView(confirmDialogView).create()
    }

    private fun initializeUiFieldsDialogGoToMail() {
        confirmDialogTitleView =
            View.inflate(activity, R.layout.rateme__feedback_dialog_title, null)
        confirmDialogView = View.inflate(activity, R.layout.rateme__feedback_dialog_message, null)
        confirmDialogTitleView.setBackgroundColor(requireArguments().getInt(EXTRA_DIALOG_TITLE_COLOR))
        confirmDialogView.setBackgroundColor(requireArguments().getInt(EXTRA_DIALOG_COLOR))
        if (requireArguments().getInt(EXTRA_LOGO) == 0) {
            confirmDialogView.findViewById<View>(R.id.app_icon_dialog_mail).visibility =
                View.GONE
        } else {
            (confirmDialogView.findViewById<View>(R.id.app_icon_dialog_mail) as ImageView).setImageResource(
                requireArguments().getInt(EXTRA_LOGO)
            )
            confirmDialogView.findViewById<View>(R.id.app_icon_dialog_mail).visibility =
                View.VISIBLE
        }
        (confirmDialogTitleView.findViewById<View>(R.id.confirmDialogTitle) as TextView).setTextColor(
            requireArguments().getInt(EXTRA_HEADER_TEXT_COLOR)
        )
        (confirmDialogView.findViewById<View>(R.id.mail_dialog_message) as TextView).setTextColor(
            requireArguments().getInt(EXTRA_TEXT_COLOR)
        )
        cancel = confirmDialogView.findViewById(R.id.buttonCancel)
        yes = confirmDialogView.findViewById(R.id.buttonYes)
        cancel.setTextColor(requireArguments().getInt(EXTRA_RATE_BUTTON_TEXT_COLOR))
        yes.setTextColor(requireArguments().getInt(EXTRA_RATE_BUTTON_TEXT_COLOR))
        cancel.setBackgroundColor(requireArguments().getInt(EXTRA_RATE_BUTTON_BG_COLOR))
        yes.setBackgroundColor(requireArguments().getInt(EXTRA_RATE_BUTTON_BG_COLOR))
        onActionListener = requireArguments().getParcelable(EXTRA_ON_ACTION_LISTENER)
    }

    private fun goToMail(appName: String?) {
        val subject = resources.getString(com.walhalla.shared.R.string.rateme__email_subject, appName)
        try {
            if (PackageUtils.isPackageInstalled(requireContext(), GMAIL_PACKAGE_NAME)) {
                val intent = Intent(Intent.ACTION_SEND)
                intent.setType("plain/text")
                intent.putExtra(
                    Intent.EXTRA_EMAIL, arrayOf(
                        requireArguments().getString(EXTRA_EMAIL)
                    )
                )
                intent.putExtra(Intent.EXTRA_SUBJECT, subject)

                val pm = requireContext().packageManager
                val matches = pm.queryIntentActivities(intent, 0)
                var className: String? = null
                for (info in matches) {
                    if (info.activityInfo.packageName == GMAIL_PACKAGE_NAME) {
                        className = info.activityInfo.name
                        if (!TextUtils.isEmpty(className)) {
                            break
                        }
                    }
                }
                if (className != null) {
                    intent.setClassName(GMAIL_PACKAGE_NAME, className)
                } else {
                    //Ok, try this Activity
                    intent.setClassName(
                        GMAIL_PACKAGE_NAME,
                        "com.google.android.gm.ComposeActivityGmail"
                    )
                }
                startActivity(Intent.createChooser(intent, ""))
            } else {
                sendGenericMail(subject)
            }
        } catch (ex: ActivityNotFoundException) {
            sendGenericMail(subject)
        }
    }

    override fun onStart() {
        super.onStart()
        val titleDividerId = resources.getIdentifier("titleDivider", "id", "android")
        val titleDivider = dialog!!.findViewById<View>(titleDividerId)
        titleDivider?.setBackgroundColor(requireArguments().getInt(EXTRA_TITLE_DIVIDER))
    }

    private fun sendGenericMail(subject: String) {
        //Log.w(TAG, "Cannot send the email with Gmail. Will use the generic chooser");
        val sendGeneric = Intent(Intent.ACTION_SEND)
        sendGeneric.setType("plain/text")
        sendGeneric.putExtra(
            Intent.EXTRA_EMAIL, arrayOf(
                requireArguments().getString(EXTRA_EMAIL)
            )
        )
        sendGeneric.putExtra(Intent.EXTRA_SUBJECT, subject)
        startActivity(Intent.createChooser(sendGeneric, ""))
    }

    companion object {
        private const val EXTRA_EMAIL = "email"
        private const val EXTRA_APP_NAME = "app-name"
        private const val EXTRA_DIALOG_TITLE_COLOR = "dialog-title-color"
        private const val EXTRA_DIALOG_COLOR = "dialog-color"
        private const val EXTRA_TEXT_COLOR = "text-color"
        private const val EXTRA_HEADER_TEXT_COLOR = "header-text-color"
        private const val EXTRA_LOGO = "icon"
        private const val EXTRA_RATE_BUTTON_TEXT_COLOR = "button-text-color"
        private const val EXTRA_RATE_BUTTON_BG_COLOR = "button-bg-color"
        private const val EXTRA_TITLE_DIVIDER = "color-title-divider"
        private const val EXTRA_RATING_BAR = "get-rating"
        private const val EXTRA_ON_ACTION_LISTENER = "on-action-listener"

        private const val GMAIL_PACKAGE_NAME = "com.google.android.gm"

        @JvmStatic
        fun newInstance(
            email: String?,
            appName: String?,
            titleBackgroundColor: Int,
            dialogColor: Int,
            headerTextColor: Int,
            textColor: Int,
            logoResId: Int,
            lineDividerColor: Int,
            rateButtonTextColor: Int,
            rateButtonBackgroundColor: Int,
            getRatingBar: Float,
            onRatingListener: OnRatingListener?
        ): FeedbackDialog {
            val feedbackDialog = FeedbackDialog()
            val args = Bundle()
            args.putString(EXTRA_EMAIL, email)
            args.putString(EXTRA_APP_NAME, appName)
            args.putInt(EXTRA_DIALOG_TITLE_COLOR, titleBackgroundColor)
            args.putInt(EXTRA_DIALOG_COLOR, dialogColor)
            args.putInt(EXTRA_HEADER_TEXT_COLOR, headerTextColor)
            args.putInt(EXTRA_TEXT_COLOR, textColor)
            args.putInt(EXTRA_LOGO, logoResId)
            args.putInt(EXTRA_RATE_BUTTON_TEXT_COLOR, rateButtonTextColor)
            args.putInt(EXTRA_RATE_BUTTON_BG_COLOR, rateButtonBackgroundColor)
            args.putInt(EXTRA_TITLE_DIVIDER, lineDividerColor)
            args.putFloat(EXTRA_RATING_BAR, getRatingBar)
            args.putParcelable(EXTRA_ON_ACTION_LISTENER, onRatingListener)

            feedbackDialog.arguments = args
            return feedbackDialog
        }
    }
}