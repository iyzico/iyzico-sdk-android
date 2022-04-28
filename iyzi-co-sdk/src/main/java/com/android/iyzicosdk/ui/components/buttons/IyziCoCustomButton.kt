package com.android.iyzicosdk.ui.components.buttons

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.android.iyzicosdk.R
import com.android.iyzicosdk.util.KeyboardUtils
import com.android.iyzicosdk.util.enums.IyziCoButtonType
import com.android.iyzicosdk.util.extensions.setMargins


internal class IyziCoCustomButton : LinearLayout {

    lateinit var root: View
    lateinit var text: String
    lateinit var type: IyziCoButtonType
    lateinit var sticky: String
    lateinit var textView: TextView
    lateinit var linearLayout: LinearLayout

    var buttonEnabled = true

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {

        if (context != null) {
            val attributes = context.obtainStyledAttributes(attrs, R.styleable.IyziCoCustomButton)

            attributes.apply {
                text = getString(R.styleable.IyziCoCustomButton_android_text) ?: ""
                type = IyziCoButtonType.values()[this.getInt(
                    R.styleable.IyziCoCustomButton_iyzico_buttonType,
                    0
                )] ?: IyziCoButtonType.BLUE_FILL
                sticky = getString(R.styleable.IyziCoCustomButton_iyzico_sticky) ?: "0"
                recycle()

            }


        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        root = inflate(
            context,
            R.layout.iyzico_component_button,
            this
        )

        linearLayout = root.findViewById<LinearLayout>(R.id.component_button_linearLayout)
        textView = root.findViewById<TextView>(R.id.component_button_textView)
        textView.text = text

        when (type) {
            IyziCoButtonType.BLUE_FILL -> {
                textView.setTextColor(context.resources.getColor(R.color.iyzico_white))
                linearLayout.background =
                    context.resources.getDrawable(R.drawable.iyzico_selector_blue_button)

            }
            IyziCoButtonType.EMPTY -> {
                textView.setTextColor(context.resources.getColor(R.color.iyzico_dark_blue))
                linearLayout.background =
                    context.resources.getDrawable(R.drawable.iyzico_selector_empty_button)
            }
            IyziCoButtonType.EMPTY_GRAY_BORDER -> {
                textView.setTextColor(context.resources.getColor(R.color.iyzico_secondary_grey))
                linearLayout.background =
                    context.resources.getDrawable(R.drawable.iyzico_selector_empty_gray_button)

            }
            IyziCoButtonType.RED_FILL -> {
                textView.setTextColor(context.resources.getColor(R.color.iyzico_dark_red))
                linearLayout.background =
                    context.resources.getDrawable(R.drawable.iyzico_selector_red_button)
            }

        }

        if (sticky == "1") {
            keyboardHandler()
        }


    }


    private fun keyboardHandler() {
        (context as? Activity)?.let {
            KeyboardUtils.addKeyboardToggleListener(it,
                object : KeyboardUtils.SoftKeyboardToggleListener {
                    override fun onToggleSoftKeyboard(isVisible: Boolean) {
                        textView.setTextColor(ContextCompat.getColor(context, R.color.iyzico_white))
                        if (isVisible) {
                            this@IyziCoCustomButton.setMargins(
                                0,
                                rightMarginDp = 0,
                                bottomMarginDp = 0
                            )
                            linearLayout.background = ContextCompat.getDrawable(
                                context,
                                R.drawable.iyzico_selector_rectangle_button
                            )


                        } else {
                            this@IyziCoCustomButton.setMargins(
                                24,
                                rightMarginDp = 24,
                                bottomMarginDp = 24
                            )

                            linearLayout.background = ContextCompat.getDrawable(
                                context,
                                R.drawable.iyzico_selector_blue_button
                            )
                        }
                    }

                })
        }
    }


    fun clickListener(listener: (IyziCoCustomButton) -> Unit) {
        linearLayout.setOnClickListener {
            listener(this)
        }
    }


    fun showLoading() {

    }

    fun hideLoading() {

    }

    val buttonGetEnabled: Boolean get() = buttonEnabled


    fun setButtonText(text:String){
        textView.text = text
    }

    fun buttonEnabled() {
        buttonEnabled = true
        textView.setTextColor(context.resources.getColor(R.color.iyzico_white))
        linearLayout.backgroundTintList =
            ContextCompat.getColorStateList(context, R.color.iyzico_secondary_dark_blue)
    }

    fun buttonDisable() {
        buttonEnabled = false
        textView.setTextColor(context.resources.getColor(R.color.iyzico_dark_grey))
        linearLayout.backgroundTintList =
            ContextCompat.getColorStateList(context, R.color.iyzico_gray30)
    }

    fun drawableTextStart(id: Int) {
        textView.setCompoundDrawablesWithIntrinsicBounds(id, 0, 0, 0)
        textView.setPadding(30, 0, 50, 0)
    }

    fun setButton(type: IyziCoButtonType) {
        this.type = type
        when (type) {
            IyziCoButtonType.BLUE_FILL -> {
                textView.setTextColor(context.resources.getColor(R.color.iyzico_white))
                linearLayout.background =
                    context.resources.getDrawable(R.drawable.iyzico_selector_blue_button)

            }
            IyziCoButtonType.EMPTY -> {
                textView.setTextColor(context.resources.getColor(R.color.iyzico_dark_blue))
                linearLayout.background =
                    context.resources.getDrawable(R.drawable.iyzico_selector_empty_button)
            }
            IyziCoButtonType.EMPTY_GRAY_BORDER -> {
                textView.setTextColor(context.resources.getColor(R.color.iyzico_secondary_grey))
                linearLayout.background =
                    context.resources.getDrawable(R.drawable.iyzico_selector_empty_gray_button)

            }
            IyziCoButtonType.RED_FILL -> {
                textView.setTextColor(context.resources.getColor(R.color.iyzico_dark_red))
                linearLayout.background =
                    context.resources.getDrawable(R.drawable.iyzico_selector_red_button)
            }

        }
    }


}