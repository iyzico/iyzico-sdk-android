package com.android.iyzicosdk.ui.components.edit_text

import android.content.Context
import android.content.res.TypedArray
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.InputType.TYPE_TEXT_VARIATION_NORMAL
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.android.iyzicosdk.R
import com.android.iyzicosdk.core.IyziCoActivity
import com.android.iyzicosdk.util.extensions.gone
import com.android.iyzicosdk.util.extensions.show
import kotlinx.android.synthetic.main.iyzico_card_cvv_edit_text.view.*
import kotlinx.android.synthetic.main.iyzico_card_date_edit_text.view.*
import kotlinx.android.synthetic.main.iyzico_component_border.view.*
import kotlinx.android.synthetic.main.iyzico_phone_edit_text.view.*
import kotlinx.android.synthetic.main.iyzico_primary_edit_text.view.*
import java.lang.Exception


internal class IyziCoPrimaryEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs) {

    var root: View = LayoutInflater.from(context)
        .inflate(R.layout.iyzico_primary_edit_text, this)

    private var headText = ""
    private var hintText = ""
    private var size = "normal"
    private var myInputType = 0
    private var beforeText = ""
    private var cursorPosition = 0
    private var beforeTextFlag = true
    private var isUpper = false
    private var nameEdittextFlag = true
    private var type: String = ""
    private var _imageOnClick: (view: View) -> Unit = { _ -> }


    init {

        root.iyzico_primary_edittext_root_view.setCustomEditText(root.iyzico_primary_edit_text)
        disableEmoji()
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.IyziCoPrimaryEditText)
        attributes.apply {

            headText = getString(R.styleable.IyziCoPrimaryEditText_iyzico_headText) ?: ""
            hintText = getString(R.styleable.IyziCoPrimaryEditText_iyzico_hintText) ?: ""
            size = getString(R.styleable.IyziCoPrimaryEditText_iyzico_border_size_from_edit_text)
                ?: "normal"
            type = (TypeofUse.values()[this.getInt(R.styleable.IyziCoPrimaryEditText_iyzico_type_of_use,0)]).toString()
            //digits = getString(R.styleable.IyziCoPrimaryEditText_android_digits) ?: ""
            when(type){
                TypeofUse.GENERAL_MODE.type -> {
                    root.iyzico_primary_edittext_root_view.changeTitleProgrammatically(
                        headText,
                        hintText,
                        size
                    )
                }
                TypeofUse.CARD_NAME.type -> {
                    root.iyzico_primary_edittext_root_view.changeTitleProgrammaticallyforCardName(
                        headText,
                        hintText,
                        size
                    )
                }
            }


            val typedCount = this.indexCount

            for (i in 0 until typedCount) {
                when (val attr = getIndex(i)) {
                    R.styleable.IyziCoPrimaryEditText_android_inputType -> setInputType(
                        this,
                        attr
                    )
                }
            }
            if (myInputType == 33) {
                initForEmail()
            }
            recycle()
        }
        listenImage()
    }

    fun listenImage() {
        root.iyziCo_secondary_lagin_change_mail.setOnClickListener {
            _imageOnClick(it)

        }

    }

    fun focus() {
        (context as IyziCoActivity).openKeyboard()
        editText.isFocusableInTouchMode = true;
        editText.requestFocus()
    }

    private fun disableEmoji() {
        val emojiFilter = InputFilter { source, start, end, dest, dstart, dend ->
            for (index in start until end) {
                val type = Character.getType(source[index])

                when (type) {
                    '*'.toInt(),
                    Character.OTHER_SYMBOL.toInt(),
                    Character.SURROGATE.toInt() -> {
                        return@InputFilter ""
                    }
                    Character.LOWERCASE_LETTER.toInt() -> {
                        val index2 = index + 1
                        if (index2 < end && Character.getType(source[index + 1]) == Character.NON_SPACING_MARK.toInt())
                            return@InputFilter ""
                    }
                    Character.DECIMAL_DIGIT_NUMBER.toInt() -> {
                        val index2 = index + 1
                        val index3 = index + 2
                        if (index2 < end && index3 < end &&
                            Character.getType(source[index2]) == Character.NON_SPACING_MARK.toInt() &&
                            Character.getType(source[index3]) == Character.ENCLOSING_MARK.toInt()
                        )
                            return@InputFilter ""
                    }
                    Character.OTHER_PUNCTUATION.toInt() -> {
                        val index2 = index + 1

                        if (index2 < end && Character.getType(source[index2]) == Character.NON_SPACING_MARK.toInt()) {
                            return@InputFilter ""
                        }
                    }
                    Character.MATH_SYMBOL.toInt() -> {
                        val index2 = index + 1
                        if (index2 < end && Character.getType(source[index2]) == Character.NON_SPACING_MARK.toInt())
                            return@InputFilter ""
                    }
                }
            }
            return@InputFilter null
        }
        root.iyzico_primary_edit_text.filters = arrayOf(emojiFilter)
    }

    val editText: EditText
        get() = root.iyzico_primary_edit_text

    val text: String
        get() = root.iyzico_primary_edit_text.text.toString()

    private fun setInputType(typedArray: TypedArray, attr: Int) {
        myInputType = typedArray.getInt(attr, TYPE_TEXT_VARIATION_NORMAL)
        root.iyzico_primary_edit_text.inputType = myInputType
    }

    fun errorBorder(errorMessage: String) {
        root.iyzico_primary_edittext_root_view.errorBorder(errorMessage)
    }

    fun clearText() {
        clearFocus()
        root.iyzico_primary_edittext_root_view.editText.setText("")
        clearBorder()
    }

    fun disableBorder(text: String) {
        editText.setText(text)
        root.iyzico_primary_edittext_root_view.disableBorder()

    }

    fun clearBorder() {
        root.iyzico_primary_edittext_root_view.forceUnfocus()
        root.iyzico_primary_edittext_root_view.setBlackText()
    }

    fun setDigits(digits: String) {
        val digits = digits // or any characters you want to allow
        editText.keyListener = DigitsKeyListener.getInstance(digits)
        editText.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME)
    }

    fun setIsUpper() {
        isUpper = true
    }

    private fun setUpperCaseFirst() {
        if (isUpper) {
            var temp = ""
            iyzico_primary_edit_text.let {

                if (it.length() >= 2) {
                    if (it.text[it.length() - 2] == ' ' && nameEdittextFlag) {
                        nameEdittextFlag = false
                        editTextForUpperCase(temp, it)

                    } else if (!nameEdittextFlag)
                        nameEdittextFlag = true

                } else if (it.length() == 1 && nameEdittextFlag) {
                    nameEdittextFlag = false
                    temp = it.text[0].toString().toUpperCase()
                    it.setText(temp)
                    it.setSelection(it.length())

                } else {
                    nameEdittextFlag = true
                }
            }
        }
    }

    private fun editTextForUpperCase(
        temp: String,
        it: IyziCoCustomEditText
    ) {
        var temp1 = temp
        temp1 = it.text[it.length() - 1].toUpperCase().toString()
        temp1 = it.text.substring(0, it.text.length - 1) + temp1
        it.setText(temp1)
        it.setSelection(it.length())
    }

    fun editTextValueListener(f: (value: String) -> Unit) {
        root.iyzico_primary_edittext_root_view.editTextValueListener(f)
    }

    fun setBlackText() {
        root.iyzico_primary_edittext_root_view.setBlackText()
    }

    fun unfocusListener(f: (view: View) -> Unit) {
        root.iyzico_primary_edittext_root_view.unFocusInput(f)
    }

    fun fillEditTextDesign() {
        root.iyzico_primary_edittext_root_view.fillEditTextDesign()
    }

    fun initForEmail() {
        var tempString = ""
        var firstTime = true

        root.iyzico_primary_edit_text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (beforeTextFlag) {
                    beforeText = root.iyzico_primary_edit_text.text.toString()
                    cursorPosition = root.iyzico_primary_edit_text.selectionStart
                    beforeTextFlag = false

                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                tempString = ""
                var areThereUpperCase = false
                if (firstTime) {
                    firstTime = false
                    if (p0.toString() != "") {
                        for (index in 0..p0.toString().length - 1) {
                            if (p0?.get(index).toString() != " ") {
                                tempString += p0?.get(index).toString()
                            }
                            if (p0?.get(index)?.isUpperCase()!!) {
                                areThereUpperCase = true
                            }
                        }
                    }
                    root.iyzico_primary_edit_text.let {
                        if (areThereUpperCase) {
                            setToLower(tempString)
                        } else {
                            it.setText(tempString)

                            try {
                                if (beforeText.length > root.iyzico_primary_edit_text.text.length) {
                                    it.setSelection(cursorPosition - 1)
                                } else {
                                    it.setSelection(cursorPosition + 1)
                                }
                                beforeTextFlag = true
                            } catch (ex: Exception) {
                                beforeTextFlag = true

                            }


                        }
                    }
                } else {
                    firstTime = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    fun textListener(listener: (text: String) -> Unit) {
        root.iyzico_primary_edit_text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                listener(p0.toString())
                setUpperCaseFirst()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    fun clickListenerImg(f: (View) -> Unit) {
        _imageOnClick = f
    }

    fun setToLower(string: String) {
        Handler(Looper.getMainLooper()).postDelayed({
            root.iyzico_primary_edit_text.let {
                it.setText(string.toLowerCase())
                try {
                    if (beforeText.length > root.iyzico_primary_edit_text.text.length) {
                        it.setSelection(cursorPosition - 1)
                    } else {
                        it.setSelection(cursorPosition + 1)
                    }
                    beforeTextFlag = true
                } catch (ex: Exception) {
                    beforeTextFlag = true

                }
            }
        }, 250)

    }

    fun showImageView() {
        root.iyziCo_secondary_lagin_change_mail.show()
    }

    fun goneImageView() {
        root.iyziCo_secondary_lagin_change_mail.gone()
    }

    enum class TypeofUse(var type: String) {
        GENERAL_MODE("GENERAL_MODE"),
        CARD_NAME("CARD_NAME")
    }
}