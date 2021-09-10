package com.android.iyzicosdk.util.extensions

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

internal fun Int.dpToPx(context: Context): Int {
    val metrics = context.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), metrics).toInt()
}

internal val Int.px: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

internal val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

internal fun Int.intToBoolean(): Boolean {
    return this == 1
}