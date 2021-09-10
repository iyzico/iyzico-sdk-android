package com.android.iyzicosdk.ui.components.edit_text

import android.content.Context
import android.graphics.Typeface
import android.text.Editable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat
import com.android.iyzicosdk.R
import kotlinx.android.synthetic.main.iyzico_card_cvv_edit_text.view.*

internal class IyziCoCustomEditText : AppCompatEditText {

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    }

    override fun getText(): Editable {

        return super.getText()!!
    }



}