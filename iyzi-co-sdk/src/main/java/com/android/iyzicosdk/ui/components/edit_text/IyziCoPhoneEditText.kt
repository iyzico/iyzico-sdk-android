package com.android.iyzicosdk.ui.components.edit_text

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import com.android.iyzicosdk.R
import com.android.iyzicosdk.core.IyziCoActivity
import com.android.iyzicosdk.util.extensions.gone
import com.android.iyzicosdk.util.extensions.show
import com.redmadrobot.inputmask.MaskedTextChangedListener
import com.redmadrobot.inputmask.helper.AffinityCalculationStrategy
import kotlinx.android.synthetic.main.iyzico_phone_edit_text.view.*
import kotlinx.android.synthetic.main.iyzico_primary_edit_text.view.*

internal class IyziCoPhoneEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs) {

    var root: View = LayoutInflater.from(context)
        .inflate(R.layout.iyzico_phone_edit_text, this)

    init {
        val format = ArrayList<String>()
        format.add("[000] [000] [00] [00]")

        MaskedTextChangedListener.installOn(
            root.iyzico_phone_edit_text,
            "[000] [000] [00] [00]",
            format,
            AffinityCalculationStrategy.PREFIX,
            object : MaskedTextChangedListener.ValueListener {
                override fun onTextChanged(
                    maskFilled: Boolean,
                    extractedValue: String,
                    formattedValue: String
                ) {
                }
            }
        )
        root.iyzico_phone_edittext_root_view.setCustomEditText(root.iyzico_phone_edit_text)
        /*root.iyzico_phone_edit_text.addTextChangedListener(
            IyziCoMaskWatcher("### ### ## ##")
        )*/

        root.iyzico_phone_edittext_root_view.focusInput {
            root.iyzico_phone_country_code_textView.show()
        }

        root.iyzico_phone_edittext_root_view.unFocusInput {
            if (text.isEmpty()) {
                root.iyzico_phone_country_code_textView.gone()
            }
        }
    }

    val editText: EditText
        get() = root.iyzico_phone_edit_text

    val text: String
        get() = root.iyzico_phone_edit_text.text.toString()

    fun errorBorder(errorMessage: String) {
        root.iyzico_phone_edittext_root_view.errorBorder(errorMessage)
    }
    fun editTextValueListener(f: (value: String) -> Unit) {
        root.iyzico_phone_edittext_root_view.editTextValueListener(f)
    }
    fun clearBorder() {
        root.iyzico_phone_edittext_root_view.forceUnfocus()
    }

    fun focus() {
        (context as IyziCoActivity).openKeyboard()
        editText.isFocusableInTouchMode = true;
        editText.requestFocus()
    }

    fun fillEditTextDesign() {
        root.iyzico_phone_country_code_textView.show()
        root.iyzico_phone_edittext_root_view.fillEditTextDesign()
    }
}