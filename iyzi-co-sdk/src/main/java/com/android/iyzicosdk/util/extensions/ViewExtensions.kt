package com.android.iyzicosdk.util.extensions

import android.content.Context
import android.graphics.Typeface
import android.os.Handler
import android.text.InputFilter
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.android.iyzicosdk.R
import com.android.iyzicosdk.ui.components.edit_text.IyziCoPhoneEditText
import com.android.iyzicosdk.ui.components.edit_text.IyziCoPriceEditText
import com.android.iyzicosdk.ui.components.edit_text.IyziCoPrimaryEditText
import com.android.iyzicosdk.ui.components.edit_text.IyziCoSmsPin
import kotlinx.android.synthetic.main.iyzico_my_account_sub_layout.view.*
import kotlinx.android.synthetic.main.iyzico_my_current_balance_layout.view.*


internal fun View.show() {
    this.visibility = View.VISIBLE
}

internal fun View.gone() {
    this.visibility = View.GONE
}

internal fun View.invisible() {
    this.visibility = View.INVISIBLE
}

internal fun View.setVisible(isVisible: Boolean) {
    if (isVisible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

internal fun View.fieldFocusListener(focus: (view: View) -> Unit, unFocus: (view: View) -> Unit) {
    this.setOnFocusChangeListener { v, hasFocus ->
        if (hasFocus) {
            focus(this)
        } else {
            unFocus(this)
        }
    }
}

internal fun View.changeTintColor(@ColorRes color: Int) {
    this.backgroundTintList = ContextCompat.getColorStateList(context, R.color.iyzico_grey)
}

internal fun View.changeBackground(@DrawableRes resId: Int) {
    this.background =
        ContextCompat.getDrawable(context, resId)
}

internal fun TextView.changeTextSize(@DimenRes dimenId: Int) {
    this.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(dimenId))
}

/**
 * Spannable exteions
 * */
internal fun TextView.spannableExtension(
    startIndex: Int,
    endIndex: Int,
    color: Int,
    clickSpan: () -> Unit
) {

    val spannableString = SpannableString(this.text)

    val spannable = object : ClickableSpan() {
        override fun onClick(textView: View) {
            clickSpan()
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.color = ContextCompat.getColor(
                this@spannableExtension.context,
                color
            )
            ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            ds.isUnderlineText = false
        }
    }
    var mStartIndex = startIndex
    if (mStartIndex==-1){
        mStartIndex = 0
    }

    spannableString.setSpan(
        StyleSpan(Typeface.BOLD),
        mStartIndex,
        endIndex,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    );
    spannableString.setSpan(
        spannable,
        mStartIndex,
        endIndex,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    this.movementMethod = LinkMovementMethod.getInstance()
    this.text = spannableString
}


/**
 * Fragment açıldığı gibi ilgili edittexte otomatik focus olmak için yazıldı
 */
internal fun IyziCoPrimaryEditText.autoFocus() {
    this.requestFocus()
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
        InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY
    );
}

/**
 * Fragment açıldığı gibi ilgili edittexte otomatik focus olmak için yazıldı
 */
internal fun IyziCoPriceEditText.autoFocus() {
    this.requestFocus()
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
        InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY
    );
}

internal fun EditText.phoneSelect() {
    this.setSelection(this.length())
}


/**
 * Fragment açıldığı gibi ilgili edittexte otomatik focus olmak için yazıldı
 */
internal fun IyziCoPhoneEditText.autoFocus() {
    this.requestFocus()
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
        InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY
    );
}

/**
 * Fragment açıldığı gibi ilgili edittexte otomatik focus olmak için yazıldı
 */
internal fun IyziCoSmsPin.autoFocus() {
    this.getFirstPinArea().requestFocus()
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
        InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY
    );
}

/**
 * View'a margin vermek için kullanılıyor
 */
internal fun View.setMargins(
    leftMarginDp: Int? = null,
    topMarginDp: Int? = null,
    rightMarginDp: Int? = null,
    bottomMarginDp: Int? = null
) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val params = layoutParams as ViewGroup.MarginLayoutParams
        leftMarginDp?.run { params.leftMargin = this.dp }
        topMarginDp?.run { params.topMargin = this.dp }
        rightMarginDp?.run { params.rightMargin = this.dp }
        bottomMarginDp?.run { params.bottomMargin = this.dp }
        requestLayout()
    }
}

internal fun View.rotateAnimation(finishHandler: () -> Unit?, duration: Long? = 1200) {
    var rotate = RotateAnimation(
        0.0f,
        -360.0f,
        Animation.RELATIVE_TO_SELF,
        0.5f,
        Animation.RELATIVE_TO_SELF,
        0.5f
    )

    rotate.duration = duration ?: 1500
    this.startAnimation(rotate)
    rotate.repeatCount = Animation.ABSOLUTE
    rotate.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation?) {
            if (duration != null) {
                if (duration > 1500L) {
                    rotate.cancel()
                    finishHandler.invoke()
                    return
                } else {
                    //this@rotateAnimation.rotateAnimation(duration = duration?.plus(450),finishHandler = finishHandler)
                }
            }
        }

        override fun onAnimationEnd(animation: Animation?) {
            finishHandler.invoke()
        }

        override fun onAnimationStart(animation: Animation?) {
        }
    })
}


internal fun View.animationFadeIn(
    doIt: (() -> Unit?)? = null
) {
    val animation = AnimationUtils.loadAnimation(context, R.anim.iyzico_fade_in_animation)
    this.startAnimation(animation)
    Handler().postDelayed({
        if (doIt != null) {
            doIt()
        }

    }, 1500)
}

internal fun View.animationFadeOut(
    doIt: (() -> Unit?)? = null
) {
    val animation = AnimationUtils.loadAnimation(context, R.anim.iyzico_fade_out_animation)
    this.startAnimation(animation)
    Handler().postDelayed(
        Runnable {
            if (doIt != null) {
                doIt()
            }
        }, 1200
    )
}

/**
 * Edittext'e maxlenght vermek amacıyla kullanılır
 */
internal fun EditText.limitLength(maxLength: Int) {
    filters = arrayOf(InputFilter.LengthFilter(maxLength))
}