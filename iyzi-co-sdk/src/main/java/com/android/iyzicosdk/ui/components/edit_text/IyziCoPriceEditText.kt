package com.android.iyzicosdk.ui.components.edit_text

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.View.OnKeyListener
import android.widget.LinearLayout
import com.android.iyzicosdk.R
import com.android.iyzicosdk.util.constants.BundleConstans
import com.android.iyzicosdk.util.extensions.addTlIcon
import com.redmadrobot.inputmask.MaskedTextChangedListener
import com.redmadrobot.inputmask.helper.AffinityCalculationStrategy
import kotlinx.android.synthetic.main.iyzico_price_cell_layout.view.*


internal class IyziCoPriceEditText @kotlin.jvm.JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs) {
    var root: View = LayoutInflater.from(context)
        .inflate(R.layout.iyzico_price_cell_layout, this)

    internal var firstEditText = root.iyzico_price_cell_first_layout_edittext
    internal var secondEditText = root.iyzico_price_cell_layout_edittext
    private val PRICE_FORMAT = "[000].[000].[000].[000]"


    init {
        listenFirstEdittext()
        listenSecondEdittext()
        listenFirstEdittextHasFocus()
    }


    private fun listenFirstEdittext() {

        val format = ArrayList<String>()
        format.add(PRICE_FORMAT)


        var lastText = ""
        firstEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                Log.i(
                    "this_log",
                    secondEditText.text.toString() + " - " + secondEditText.text.length
                )
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (s != null && s.length >= 1) {
                    if (s[s.length - 1].toString() == "." || s[s.length - 1].toString() == ",") {
                        secondEditText.requestFocus()
                        setTextColors(firstEditText.text.toString(), s.toString())

                    }
                }
            }

        })


        MaskedTextChangedListener.installOn(
            firstEditText,
            PRICE_FORMAT,
            format,
            AffinityCalculationStrategy.CAPACITY,
            object : MaskedTextChangedListener.ValueListener {
                override fun onTextChanged(
                    maskFilled: Boolean,
                    extractedValue: String,
                    formattedValue: String
                ) {

                    if (maskFilled) {
                        secondEditText.requestFocus()
                    }

                    lastText = formattedValue
                    setTextColors(lastText, secondEditText.text.toString())

                }

            }
        ).rightToLeft = true
    }

    fun listenSecondEdittext() {

        secondEditText.setOnKeyListener(OnKeyListener { v, keyCode, event -> //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                //this is for backspace

                secondEditText.clearFocus()
                firstEditText.requestFocus()
                firstEditText.setSelection(firstEditText.text.toString().length)

            }
            false
        })


        var firstTime = true

        secondEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s != null) {

                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (firstTime) {
                    setTextColors(firstEditText.text.toString(), s.toString())


                } else {
                    firstTime = true
                }


            }

        })
    }

    fun listenFirstEdittextHasFocus() {
        firstEditText.setOnFocusChangeListener(OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                if (secondEditText.text.length == 1) {
                    secondEditText.setText("")
                    setTextColors(firstEditText.text.toString(), secondEditText.text.toString())
                }
            }
        })
    }

    fun setTextColors(firstString: String, secondString: String) {
        when {
            firstString.equals("0") && secondString.equals("00") -> {
                firstEditText.setText("")
                secondEditText.setText("")

            }

            firstString.isNullOrEmpty() != true -> {
                firstEditText.setTextColor(context.resources.getColor(R.color.iyzico_primary_text_color))
                root.iyzico_price_cell_layout_dot_textView.setTextColor(context.resources.getColor(R.color.iyzico_primary_text_color))
                if (secondString.isNullOrEmpty()) {
                    secondEditText.setHintTextColor(context.resources.getColor(R.color.iyzico_primary_text_color))
                } else {
                    root.iyzico_price_cell_layout_dot_textView.setTextColor(
                        context.resources.getColor(
                            R.color.iyzico_primary_text_color
                        )
                    )
                    secondEditText.setTextColor(context.resources.getColor(R.color.iyzico_primary_text_color))
                }
            }
            firstString.isNullOrEmpty() == true -> {
                if (secondString.isNullOrEmpty()) {
                    firstEditText.setHintTextColor(context.resources.getColor(R.color.iyzico_grey))
                    root.iyzico_price_cell_layout_dot_textView.setTextColor(
                        context.resources.getColor(
                            R.color.iyzico_grey
                        )
                    )
                    secondEditText.setHintTextColor(context.resources.getColor(R.color.iyzico_grey))
                } else {
                    secondEditText.setTextColor(context.resources.getColor(R.color.iyzico_primary_text_color))
                    firstEditText.setHintTextColor(context.resources.getColor(R.color.iyzico_primary_text_color))
                    root.iyzico_price_cell_layout_dot_textView.setTextColor(
                        context.resources.getColor(
                            R.color.iyzico_primary_text_color
                        )
                    )
                }
            }

        }

    }


    fun setText(text: String) {
        var beforeDot = text.substringBefore(",").replace("₺", "").replace(" ", "")
        firstEditText.setText(beforeDot)
        var afterDot = text.substringAfter(",")
        if (afterDot.toInt() != 0) {
            secondEditText.setText(afterDot)
        }
        else{
            secondEditText.setText("")

        }
    }

    fun getPrice(): String {
        var price =
            firstEditText.text.toString().replace(",", ".") + "." + secondEditText.text.toString()
        if (price.equals(".")) {
            return BundleConstans.ZERO_MONEY.addTlIcon()
        } else if (firstEditText.text.toString().isNullOrEmpty()) {
            if (price.substringAfter(".").length == 1) {
                return "0.${price.substringAfter(".")}0"
            } else {
                return "0${price}"
            }
        }
        if (price.get(price.length - 1) == '.') {
            price = price.replace(".", "")
            return price + ".00"
        } else if (price.get(price.length - 2) == '.') {
            price = price.replace(".", "")
            var temp = price.substring(0, price.length - 1) + "." + price.get(price.length - 1)
            return temp + "0"
        } else {
            var temp = price.substring(0, price.length - 3)
            temp = temp.replace(".", "")

            return temp + price.substring(price.length - 3, price.length)
        }
    }
}
