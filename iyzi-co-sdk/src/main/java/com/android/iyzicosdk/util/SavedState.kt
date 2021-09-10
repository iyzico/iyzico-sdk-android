package com.android.iyzicosdk.util

import android.os.Parcel
import android.os.Parcelable
import android.util.SparseArray
import android.view.View

internal class SavedState(superState: Parcelable) : View.BaseSavedState(superState) {
    var childrenStates: SparseArray<Any>? = null

    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
        childrenStates?.let {
            out.writeSparseArray(it)
        }
    }

}