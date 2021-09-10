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
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.android.iyzicosdk.R
import com.redmadrobot.inputmask.MaskedTextChangedListener
import com.redmadrobot.inputmask.helper.AffinityCalculationStrategy
import kotlinx.android.synthetic.main.iyzico_card_cvv_edit_text.view.*
import kotlinx.android.synthetic.main.iyzico_card_no_edit_text.view.*
import kotlinx.android.synthetic.main.iyzico_primary_edit_text.view.*


internal class IyziCoCardNoEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs) {

    var root: View = LayoutInflater.from(context)
        .inflate(R.layout.iyzico_card_no_edit_text, this)
    val format = ArrayList<String>()

    init {
        clearEdittextFocusWhenDone()
        format.add("[0000] [0000] [0000] [0000]")
        MaskedTextChangedListener.installOn(
            root.iyzico_card_no_edit_text,
            "",
            affineFormats = format,
            affinityCalculationStrategy = AffinityCalculationStrategy.WHOLE_STRING,
            valueListener = object : MaskedTextChangedListener.ValueListener {


                override fun onTextChanged(
                    maskFilled: Boolean,
                    extractedValue: String,
                    formattedValue: String
                ) {

                }
            })

        root.iyzico_card_no_root_view.setCustomEditText(root.iyzico_card_no_edit_text)

    }

    fun setEditTextMaxLength(lenght: Int) {
        root.iyzico_card_no_edit_text.filters =
            arrayOf<InputFilter>(InputFilter.LengthFilter(lenght))
    }

    fun setAmex(number:String){
        format.find {
            it.subSequence(0,2).equals("34")
        }.apply {
            this?.let {

            }?: kotlin.run {
                if(number.substring(0,2).equals("34")){
                    format.clear()
                    format.add("34[00] [000000] [00000]")
                }
            }

        }

        format.find {
            it.subSequence(0,2).equals("37")
        }.apply {
            this?.let {

            }?: kotlin.run {
                if(number.substring(0,2).equals("37")){
                    format.clear()
                    format.add("37[00] [000000] [00000]")
                }
            }

        }



    }

    fun clearEdittextFocusWhenDone() {
        iyzico_card_no_edit_text.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                iyzico_card_no_edit_text.clearFocus()
            }
            false
        })
    }

    fun unfocusListener(f: (view: View) -> Unit) {
        root.iyzico_card_no_root_view.unFocusInput(f)
    }

    fun textListener(listener: (text: String) -> Unit) {

        root.iyzico_card_no_edit_text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0.toString().length == 0){
                    format.clear()
                    format.add("[0000] [0000] [0000] [0000]")
                }
                listener(p0.toString())

            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    fun clearBorder() {
        root.iyzico_card_no_root_view.forceUnfocus()
        root.iyzico_card_no_root_view.setBlackText()
    }

    fun errorBorder(errorMessage: String) {
        root.iyzico_card_no_root_view.errorBorder(errorMessage)
    }

    fun clearText() {
        clearFocus()
        root.iyzico_card_no_root_view.editText.setText("")
        clearBorder()
    }

    val text: String
        get() = root.iyzico_card_no_edit_text.text.toString()

}