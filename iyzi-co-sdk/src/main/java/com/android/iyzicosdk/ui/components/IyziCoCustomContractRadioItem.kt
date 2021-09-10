package com.android.iyzicosdk.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.widget.NestedScrollView
import com.android.iyzicosdk.R
import com.android.iyzicosdk.util.enums.IyziCoSupportScreenType
import com.android.iyzicosdk.util.extensions.changeBackground
import com.android.iyzicosdk.util.extensions.spannableExtension
import kotlinx.android.synthetic.main.iyzico_contract_radio_item.view.*
import kotlinx.android.synthetic.main.iyzico_double_border.view.*
import kotlinx.android.synthetic.main.iyzico_fragment_login.view.*
import kotlinx.android.synthetic.main.iyzico_remittance_custom_view.view.*

internal class IyziCoCustomContractRadioItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs) {
    var startText = ""
    var endText = ""
    var isClicked = false
    var isErrorOpen = false
    private var _textOnClick: (view: View) -> Unit = { _ -> }
    private var _checkBoxClick: (view: View, Boolean) -> Unit = { _, _ -> }
    public var condition: String? = null

    var root: View = LayoutInflater.from(context)
        .inflate(R.layout.iyzico_contract_radio_item, this)


    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.IyziCoCustomContractItem)
        attributes.apply {

            startText = getString(R.styleable.IyziCoCustomContractItem_iyzico_start_text) ?: ""
            endText = getString(R.styleable.IyziCoCustomContractItem_iyzico_end_text) ?: ""
            combineTexts()
            radioClickListener()
            recycle()
        }
    }

    fun setError() {
        isErrorOpen = true
        contract_Radio_topLayout.changeBackground(R.color.iyzico_pink)
        root.iyzico_contract_radio_imageview.changeBackground(R.drawable.iyzico_error_radio_button_border)
    }

    fun clickListener(f: (View) -> Unit) {
        _textOnClick = f
    }

    fun checkBoxClick(f: (View, Boolean) -> Unit) {
        _checkBoxClick = f
    }

    private fun radioClickListener() {
        root.iyzico_contract_radio_imageview.setOnClickListener {
            isClicked = !isClicked
            _checkBoxClick(it, isClicked)
            if (isClicked) {
                checkToRadio()
                setCondition()
                if (isErrorOpen) {
                    isErrorOpen = false
                    contract_Radio_topLayout.changeBackground(R.color.iyzico_light_grey)
                }

            } else {
                unCheckToRadio()
                condition = null
            }
        }
    }

    private fun setCondition() {
        condition = "accepted"
    }

    private fun combineTexts() {
        root.iyzico_contract_radio_item_text.text = "$startText'$endText"
        root.iyzico_contract_radio_item_text.spannableExtension(
            0,
            startText.length,
            R.color.iyzico_secondary_dark_blue
        ) {
            root.iyzico_contract_radio_item_text.setOnClickListener {
                _textOnClick(it)
            }
        }

    }


    private fun checkToRadio() {
        root.iyzico_contract_radio_imageview.background = null
        root.iyzico_contract_radio_imageview.setImageResource(R.mipmap.iyzico_ic_square_check_button)
    }

    private fun unCheckToRadio() {
        root.iyzico_contract_radio_imageview.setImageResource(R.mipmap.iyzico_ic_square_check_clear_button)
        root.iyzico_contract_radio_imageview.changeBackground(R.drawable.iyzico_empty_radio_button_unchecked)
    }

}