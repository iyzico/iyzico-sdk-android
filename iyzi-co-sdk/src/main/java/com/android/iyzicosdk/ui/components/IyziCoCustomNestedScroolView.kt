package com.android.iyzicosdk.ui.components

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.android.iyzicosdk.R
import com.android.iyzicosdk.core.IyziCoActivity
import com.google.android.material.internal.ContextUtils.getActivity

internal class IyziCoCustomNestedScroolView : androidx.core.widget.NestedScrollView {
    private var isHide = false
    private var isThisOverride = false

    constructor(context: Context) : super(context) {
        startFonk(context)

    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        if (context != null) {

            val attributes =
                context.obtainStyledAttributes(attrs, R.styleable.IyziCoCustomNestedScroolView)

            attributes.apply {
                isHide = getBoolean(R.styleable.IyziCoCustomNestedScroolView_iyziCo_hide, false)
            }
            startFonk(context)
        }
    }

    fun doThis(callback: () -> Unit) {
        isThisOverride = true
        setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (isHide) {
                callback()
            }
        }
    }

    fun startFonk(context:Context) {
        setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (isHide) {
                if (!isThisOverride) {
                    (context as IyziCoActivity).hideSoftKeyboard()
                }
            }
        }
    }
}