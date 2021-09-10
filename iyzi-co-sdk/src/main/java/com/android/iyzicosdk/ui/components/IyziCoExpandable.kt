package com.android.iyzicosdk.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.LinearLayout
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.android.iyzicosdk.R
import com.android.iyzicosdk.util.extensions.gone
import com.android.iyzicosdk.util.extensions.show
import kotlinx.android.synthetic.main.iyzico_expandable.view.*


internal class IyziCoExpandable : LinearLayout {


    lateinit var root: View

    constructor(context: Context?) : super(context)

    private var text = ""
    private var imageSrc = -1

    var isOpened = false

    private var _expandableOpened: (isOpen: Boolean) -> Unit = { _ -> }

    private var allowExpanded = "1"

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {

        if (context != null) {
            val attributes = context.obtainStyledAttributes(attrs, R.styleable.IyziCoExpandable)

            attributes.apply {

                text = getString(R.styleable.IyziCoImageRightText_android_text) ?: ""
                imageSrc = getResourceId(R.styleable.IyziCoImageRightText_android_src, -1)
                allowExpanded = getString(R.styleable.IyziCoExpandable_iyzico_allow_expanded) ?: "1"

                recycle()
            }

        }

    }

    override fun onFinishInflate() {
        super.onFinishInflate()


        root = inflate(
            context,
            R.layout.iyzico_expandable,
            this
        )

        val container: ViewGroup =
            root.findViewById(R.id.iyzico_expandable_container)


        root.iyzico_expandable_textView.text = text


        if (imageSrc != -1) {
            root.iyzico_expandable_icon.setBackgroundResource(imageSrc)
        }
        root.iyzico_expandable_header_constraint.setOnClickListener {
            if (allowExpanded == "1") {
                isOpened = !isOpened

                if (isOpened) {
                    openContainer()
                    hideLine()
                    settoArrowForUp()
                } else {
                    closeContainer()
                    showLine()
                    setToArrowForDown()

                }
            }
            _expandableOpened(isOpened)
        }


        while (childCount > 1) {
            val child: View = getChildAt(0)
            removeView(child)
            container.addView(child, child.layoutParams)
        }

    }

    fun expandableOpened(f: (Boolean) -> Unit) {
        _expandableOpened = f
    }

    fun settoArrowForUp() {
        root.iyzico_expandable_arrow_button.setImageResource(R.drawable.iyzico_up_arrow)
    }

    fun setToArrowForDown() {
        root.iyzico_expandable_arrow_button.setImageResource(R.drawable.iyzico_down_arrow)

    }

    fun closeOtherItem() {
        if (isOpened) {
            isOpened = !isOpened
            showLine()
            closeContainer()
            setToArrowForDown()
        }
    }

    fun autoFocus() {
        root.iyzico_expandable_header_constraint.performClick()
    }

    fun closeContainer() {
        root.iyzico_expandable_container.gone()
    }

    fun openContainer() {
        root.iyzico_expandable_container.show()
    }

    fun showLine() {
        root.iyzico_expandable_line_view.show()
    }

    fun hideLine() {
        root.iyzico_expandable_line_view.gone()

    }
}