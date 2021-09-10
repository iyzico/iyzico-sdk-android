package com.android.iyzicosdk.util.extensions
import android.os.SystemClock
import android.view.View
import android.view.animation.AlphaAnimation

internal class OnSafeClickListener(
    private var defaultInterval: Int = 1000,
    private val onSafeCLick: (View) -> Unit
) : View.OnClickListener {
    private var lastTimeClicked: Long = 0
    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeCLick(v)
/*
        val animation = AlphaAnimation(1F, 0.092f)
        animation.duration = 100
        v.startAnimation(animation)*/
    }
}


internal fun View.setOnSafeClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = OnSafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}

/*
class OnSafeClickListenerWithoutAnimate(
    private var defaultInterval: Int = 1000,
    private val onSafeCLick: (View) -> Unit
) : View.OnClickListener {
    private var lastTimeClicked: Long = 0
    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeCLick(v)
    }
}


fun View.clickListenerWithoutAnimate(onSafeClick: (View) -> Unit) {
    val safeClickListener = OnSafeClickListenerWithoutAnimate {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}*/
