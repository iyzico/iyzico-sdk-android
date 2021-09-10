package com.android.iyzicosdk.ui.components.edit_text

import android.content.ClipboardManager
import android.content.Context
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.util.SparseArray
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.text.isDigitsOnly
import androidx.core.view.get
import com.android.iyzicosdk.R
import com.android.iyzicosdk.util.KeyboardUtils
import com.android.iyzicosdk.util.SavedState
import com.android.iyzicosdk.util.extensions.autoFocus
import com.android.iyzicosdk.util.extensions.dp
import com.android.iyzicosdk.util.extensions.limitLength
import kotlinx.android.synthetic.main.iyzico_sms_pin.view.*

internal class IyziCoSmsPin @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs) {
    var root: View = LayoutInflater.from(context)
        .inflate(R.layout.iyzico_sms_pin, this)


    var pinCount = 6

    var _id = -1

    private var fullCode = ""

    private var _fullCode: (fullCode: String) -> Unit = { _ -> }

    private var pinList = ArrayList<EditText>()

    private var detect = false

    init {

        repeat(pinCount) { index ->
            val params: ViewGroup.LayoutParams = if (index == pinCount - 1) {
                LayoutParams(50.dp, LayoutParams.WRAP_CONTENT)
            } else {
                LayoutParams(55.dp, LayoutParams.WRAP_CONTENT)
            }

            val singlePinEditText = IyziCoSinglePinEditText(context)
            singlePinEditText.layoutParams = params
            singlePinEditText.id = index
            singlePinEditText.getEditText()
                .addTextChangedListener(textWatcher(singlePinEditText.id))
            root.iyzico_sms_pin_container.addView(singlePinEditText)
            pinList.add(singlePinEditText.getEditText())
        }

        pinList.forEachIndexed { index, editText ->
            if (index != 0) {
                editText.isFocusable = false
            }
        }
    }

    fun clearEdittexts() {
        for (i in pinCount - 1 downTo 0) {
            pinList[i].apply {
                isFocusableInTouchMode = true
                isFocusable = true
                requestFocus()
                autoFocus()
            }
        }
        pinList.forEachIndexed { index, editText ->
            if (index != 0) {
                editText.isFocusable = false
            }
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        if (event?.keyCode == KeyEvent.KEYCODE_DEL && _id > 0 && event.action == KeyEvent.ACTION_UP) {
            detect = true
            pinList[_id].setText("")
        }
        return super.dispatchKeyEvent(event)
    }

    private fun textWatcher(id: Int) = object : TextWatcher {

        override fun afterTextChanged(s: Editable?) {
            /*if (id == pinCount - 1 && !s.isNullOrEmpty()) {
                finished()
            } else {
                if (s != null && s.length == 1) {
                    enabled(id + 1)
                    pinList[id + 1].requestFocus()
                }

            }*/
            if (s!!.length > 2) {
                var copyText =
                    (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).getPrimaryClip()
                        ?.getItemAt(0)?.getText()
                copyText?.let {
                    if (copyText.length == 6) {
                        setCopyText(copyText.toString())
                    } else {
                        pinList[0].setText(copyText.get(0).toString())
                    }
                }
            } else if (s!!.length == 1) {
                if (id == pinCount - 1 && !s.isNullOrEmpty()) {
                    setMaxLengthPingList(1)
                    finished()
                } else {
                    if (s != null && s.length == 1) {
                        enabled(id + 1)
                        pinList[id + 1].requestFocus()
                    }
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            try {
                if (s?.length == 0 && detect) {
                    enabled(id - 1)
                    pinList[id - 1].requestFocus()
                }
                detect = false
            } catch (exception: Exception) {

            }
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }
    }


    private fun setCopyText(copyText: String) {
        pinList.forEachIndexed { index, editText ->
            pinList[index].setText(copyText.get(index).toString())
        }
    }

    private fun setMaxLengthPingList(length: Int) {
        pinList.forEachIndexed { index, editText ->
            pinList[index].limitLength(length)
        }
    }

    private fun enabled(pos: Int) {
        _id = pos
        pinList.forEachIndexed { index, editText ->
            editText.isFocusableInTouchMode = index == pos
            editText.isFocusable = index == pos
            editText.limitLength(1)
        }
    }

    private fun finished() {
        fullCode = ""
        for (i in 0 until pinCount) {
            (root.iyzico_sms_pin_container[i] as? IyziCoSinglePinEditText)?.let {
                fullCode += it.getEditText().text.toString()
            }
        }
        (root.iyzico_sms_pin_container[pinCount - 1] as? IyziCoSinglePinEditText)?.apply {
            lastPin()
            clearFocus()
            KeyboardUtils.forceCloseKeyboard(this)
        }
        _fullCode(fullCode)
    }

    fun getFullCode(f: (String) -> Unit) {
        setMaxLengthPingList(6)
        _fullCode = f
    }

    fun getFirstPinArea(): EditText = pinList[0]

    @Suppress("UNCHECKED_CAST")
    public override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val ss = superState?.let { SavedState(it) }
        ss?.childrenStates = SparseArray()
        for (i in 0 until childCount) {
            getChildAt(i).saveHierarchyState(ss?.childrenStates as SparseArray<Parcelable>)
        }
        return ss
    }

    @Suppress("UNCHECKED_CAST")
    public override fun onRestoreInstanceState(state: Parcelable) {
        val ss = state as SavedState
        super.onRestoreInstanceState(ss.superState)
        for (i in 0 until childCount) {
            getChildAt(i).restoreHierarchyState(ss.childrenStates as SparseArray<Parcelable>)
        }
    }

    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable>) {
        dispatchFreezeSelfOnly(container)
    }

    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable>) {
        dispatchThawSelfOnly(container)
    }

}
