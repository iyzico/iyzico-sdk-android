package com.android.iyzicosdk.util.extensions

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.android.iyzicosdk.R
import com.android.iyzicosdk.core.IyziCoActivity
import com.android.iyzicosdk.ui.components.buttons.IyziCoCustomButton
import com.android.iyzicosdk.util.enums.IyziCoButtonType
import com.android.iyzicosdk.util.enums.IyziCoPopupType

internal fun createPopup(
    context: Context,
    type: IyziCoPopupType,
    imageId: Int,
    message: String,
    negativeButtonText: String = "",
    positiveButtonText: String,
    backButtonCallback: (() -> Unit?)? = null,
    closeButtonCallback: (() -> Unit?)? = null,
    continueButtonCallback: () -> Unit
) {

    val dialog = Dialog(context, R.style.IyziCoCustomDialogTheme)
    dialog.setCanceledOnTouchOutside(false)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(R.layout.iyzico_custom_popup)
    dialog.window?.let {
        it.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); // This flag is required to set otherwise the setDimAmount method will not show any effect
        it.setDimAmount(0.85f); //0 for no dim to 1 for full dim
    }


    dialog.run {

        var imageView = findViewById<AppCompatImageView>(R.id.iyzico_custom_popup_imageview)
        var descriptionTextView = findViewById<TextView>(R.id.iyzico_custom_popup_warning_textview)
        var closeButton = findViewById<AppCompatImageView>(R.id.iyzico_custom_popup_close_popup)
        var backButton =
            findViewById<IyziCoCustomButton>(R.id.iyzico_custom_popup_close_page_button)
        var continueButton =
            findViewById<IyziCoCustomButton>(R.id.iyzico_custom_popup_continue_button)
        var exitButton = findViewById<AppCompatTextView>(R.id.iyzico_custom_button_exit_textview)

        when (type) {
            IyziCoPopupType.CLOSE_OR_CONTINUE_PAGE -> {
                backButton.text = negativeButtonText
                backButton.setButton(IyziCoButtonType.EMPTY_GRAY_BORDER)
                continueButton.text = positiveButtonText
                descriptionTextView.text = message
                imageView.background = getDrawable(context, imageId)
                exitButton.text = context.getString(R.string.iyzico_close)
            }
            IyziCoPopupType.ERROR_POPUP -> {
                continueButton.textView.setText(positiveButtonText)
                descriptionTextView.text = message
                imageView.background = getDrawable(context, imageId)
                exitButton.text = context.getString(R.string.iyzico_close)
                backButton.gone()
            }
            IyziCoPopupType.TIMEOUT_POPUP -> {

                backButton.layoutParams.width =220*((context as IyziCoActivity).resources.displayMetrics.density).toInt()
                backButton.textView.setText(positiveButtonText)
                backButton.setButton(IyziCoButtonType.EMPTY)
                descriptionTextView.text = message
                imageView.background = getDrawable(context, imageId)
                exitButton.text = context.getString(R.string.iyzico_close)
                continueButton.gone()
            }
        }
        closeButton.run {
            setOnClickListener {
                closeButtonCallback?.let {
                    it.invoke()
                }
                dismiss()
            }
        }
        backButton.run {

            clickListener {
                backButtonCallback?.let {
                    backButtonCallback()
                }
                dismiss()
            }
        }
        exitButton.run {
            setOnClickListener {
                closeButtonCallback?.let {
                    it.invoke()
                }
                dismiss()
            }
        }
        continueButton.run {
            clickListener {
                continueButtonCallback()
                dismiss()
            }
        }
        try {
            show()
        } catch (e: Exception) {

        }
    }

}

internal fun Dialog.animationLoading(): Dialog {
    this.setCancelable(false)
    this.requestWindowFeature(Window.FEATURE_NO_TITLE)
    this.setContentView(R.layout.iyzico_loading_animation)
    this.window?.let {
        it.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); // This flag is required to set otherwise the setDimAmount method will not show any effect
        it.setDimAmount(0.85f); //0 for no dim to 1 for full dim
    }
    this.show()
    return this
}


