package com.android.iyzicosdk.ui.components.credit_card

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.TextView.OnEditorActionListener
import com.android.iyzicosdk.R
import kotlinx.android.synthetic.main.iyzico_add_card_layout.view.*
import kotlinx.android.synthetic.main.iyzico_card_cvv_edit_text.view.*
import kotlinx.android.synthetic.main.iyzico_card_no_edit_text.view.*
import kotlinx.android.synthetic.main.iyzico_my_cards_layout.view.*


internal class IyziCoCVV @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs) {

    var root: View = LayoutInflater.from(context)
        .inflate(R.layout.iyzico_card_cvv_edit_text, this)


    init {
        clearEdittextFocusWhenDone()
        root.iyzico_card_cvv_root_view.setCustomEditText(root.iyzico_card_cvv_edit_text)
        clearEdittextFocusWhenDone()
    }

    fun unfocusListener(f: (view: View) -> Unit) {
        root.iyzico_card_cvv_root_view.unFocusInput(f)
    }

    fun clearText() {
        clearFocus()
        root.iyzico_card_cvv_root_view.editText.setText("")
        clearBorder()
    }

    fun errorBorder(errorMessage: String) {
        root.iyzico_card_cvv_root_view.errorBorder(errorMessage)
    }

    fun clearBorder() {
        root.iyzico_card_cvv_root_view.forceUnfocus()
        root.iyzico_card_cvv_root_view.setBlackText()
    }

    fun clearEdittextFocusWhenDone() {
        iyzico_card_cvv_edit_text.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                clearBorder()
                clearFocus()
                clearFocus()
                root.iyzico_add_card_name?.let {
                    it.clearBorder()
                }
                root.iyzico_add_card_use_my_balance_button?.let {
                    it.setNormalBorder()
                }
            }
            false
        })
    }


    fun setEditTextMaxLength(lenght: Int) {
        root.iyzico_card_cvv_edit_text.filters =
            arrayOf<InputFilter>(InputFilter.LengthFilter(lenght))
        if (lenght == 4) {
            root.iyzico_card_cvv_edit_text.hint = "1234"
        } else {
            root.iyzico_card_cvv_edit_text.hint = "123"

        }
    }

    fun textListener(listener: (text: String) -> Unit) {
        root.iyzico_card_cvv_edit_text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                listener(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    val text: String
        get() = root.iyzico_card_cvv_edit_text.text.toString()

}