package com.android.iyzicosdk.ui.components.edit_text

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.android.iyzicosdk.R
import com.android.iyzicosdk.util.SavedState
import com.android.iyzicosdk.util.extensions.setMargins
import kotlinx.android.synthetic.main.iyzico_single_pin_edit_text.view.*

internal class IyziCoSinglePinEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs) {
    var root: View = LayoutInflater.from(context)
        .inflate(R.layout.iyzico_single_pin_edit_text, this)

    init {
        root.iyzico_pin_edittext_root_view.setCustomEditText(root.iyzico_pin_edit_text)
    }

    fun lastPin() {
        root.iyzico_pin_edittext_root_view.lastPin()
    }


    fun getEditText(): IyziCoCustomEditText = root.iyzico_pin_edit_text

    fun removeMargin() {
        root.iyzico_pin_edittext_root_view.setMargins(rightMarginDp = 0)
    }

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