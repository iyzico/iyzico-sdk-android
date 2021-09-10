package com.android.iyzicosdk.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.res.TypedArray
import android.os.CountDownTimer
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.android.iyzicosdk.R
import kotlinx.android.synthetic.main.iyzico_remittance_custom_view.view.*


internal class IyzicoRemittanceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs) {
    lateinit var timer: CountDownTimer
    var isFirst = true
    var root: View = LayoutInflater.from(context)
        .inflate(R.layout.iyzico_remittance_custom_view, this)


    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.IyzicoRemittanceView)

        attributes.apply {

            val text = getString(R.styleable.IyzicoRemittanceView_android_text)
            val hint = getString(R.styleable.IyzicoRemittanceView_android_hint)

            root.iyzico_remittance_custom_view_title_textView.text = text
            root.iyzico_remittance_custom_view_textView.hint = hint

            clickButton()

            /*    val typedCount = this.indexCount

                for (i in 0 until typedCount) {
                    when (val attr = getIndex(i)) {
                        R.styleable.IyziCoPrimaryEditText_android_inputType -> setInputType(
                            this,
                            attr
                        )
                    }
                }
    */

            recycle()
        }

    }

    private fun clickButton() {
        root.iyzico_remittance_custom_view_copy_imageView.setOnClickListener {
            val clipboard = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText(
                "iyzico_remittance_custom_view_textView",
                root.iyzico_remittance_custom_view_textView.text
            )
            changeButtons()
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "Kopyalandı", Toast.LENGTH_SHORT).show()

        }
    }

    fun changeButtons() {

        root.iyzico_remittance_custom_view_copy_imageView.setImageResource(R.drawable.iyzico_ic_check_button)
        if(isFirst){
            isFirst=false
        }
        else{
            timer.cancel()
        }
         timer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                root.iyzico_remittance_custom_view_copy_imageView.setImageResource(R.drawable.iyzico_ic_copy_paste)
                timer.cancel()
            }
        }
        timer.start()

    }
    fun setTexts(text:String){
        root.iyzico_remittance_custom_view_textView.setText(text)
    }

    private fun setInputType(typedArray: TypedArray, attr: Int) {
        val inputType = typedArray.getInt(attr, InputType.TYPE_TEXT_VARIATION_NORMAL)
    }

}