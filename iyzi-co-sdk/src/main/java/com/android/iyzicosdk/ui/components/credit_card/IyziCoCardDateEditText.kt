package com.android.iyzicosdk.ui.components.credit_card

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.android.iyzicosdk.R
import com.android.iyzicosdk.ui.components.IyziCoCustomBorder
import com.redmadrobot.inputmask.MaskedTextChangedListener
import com.redmadrobot.inputmask.helper.AffinityCalculationStrategy
import kotlinx.android.synthetic.main.iyzico_card_cvv_edit_text.view.*
import kotlinx.android.synthetic.main.iyzico_card_date_edit_text.view.*
import kotlinx.android.synthetic.main.iyzico_card_no_edit_text.view.*

internal class IyziCoCardDateEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs) {


    var root: View = LayoutInflater.from(context)
        .inflate(R.layout.iyzico_card_date_edit_text, this)

    init {
        val format = ArrayList<String>()
        format.add("[00]{/}[00]")

        MaskedTextChangedListener.installOn(
            root.iyzico_card_date_edit_text,
            "[00]{/}[00]",
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

        root.iyzico_card_date_root_view.setCustomEditText(root.iyzico_card_date_edit_text)
        /*root.iyzico_card_date_edit_text.addTextChangedListener(
            IyziCoMaskWatcher("##/##")
        )*/
    }

    fun clearEdittextFocusWhenDone() {
        iyzico_card_date_edit_text.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                clearBorder()
                clearFocus()
                clearFocus()
            }
            false
        })
    }

    fun unfocusListener(f: (view: View) -> Unit) {
        root.iyzico_card_date_root_view.unFocusInput(f)
    }

    fun textListener(listener: (text: String) -> Unit) {
        root.iyzico_card_date_edit_text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                listener(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    fun clearBorder() {
        root.iyzico_card_date_root_view.forceUnfocus()
        root.iyzico_card_date_root_view.setBlackText()
    }

    fun errorBorder(errorMessage: String) {
        root.iyzico_card_date_root_view.errorBorder(errorMessage)
    }

    fun clearText() {
        clearFocus()
        root.iyzico_card_date_root_view.editText.setText("")
        clearBorder()
    }

    val borderIyziCo: IyziCoCustomBorder get() = root.iyzico_card_date_root_view

    val text: String
        get() = root.iyzico_card_date_edit_text.text.toString()

}