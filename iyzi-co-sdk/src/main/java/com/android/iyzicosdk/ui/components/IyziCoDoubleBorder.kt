package com.android.iyzicosdk.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.android.iyzicosdk.R
import com.android.iyzicosdk.util.extensions.changeBackground
import com.android.iyzicosdk.util.extensions.dp
import com.android.iyzicosdk.util.extensions.gone
import com.android.iyzicosdk.util.extensions.show
import kotlinx.android.synthetic.main.iyzico_double_border.view.*
import kotlinx.android.synthetic.main.iyzico_my_cards_layout.view.*

internal class IyziCoDoubleBorder : LinearLayout {

    lateinit var root: View


    var isClicked = false


    private var _onClick: (view: View, isClicked: Boolean) -> Unit = { _, _ -> }

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {

    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        root = inflate(
            context,
            R.layout.iyzico_double_border,
            this
        )

        val container: ViewGroup =
            root.findViewById(R.id.iyzico_double_border_container)

        while (childCount > 1) {
            val child: View = getChildAt(0)
            removeView(child)
            container.addView(child, child.layoutParams)
        }

        root.iyzico_double_border_root.setOnClickListener {
            isClicked = !isClicked

            if (isClicked) {
                focus()
            } else {
                unFocus()
            }

            _onClick(it, isClicked)
        }

    }

     fun focus() {
        isClicked = true
        root.iyzico_double_border_square_check_imageview.background = null
        root.iyzico_double_border_root.apply {
            changeBackground(R.drawable.iyzico_border_corner_radius_fill_color)
            // setPadding(2.dp, 2.dp, 2.dp, 2.dp)
        }
        root.iyzico_double_border_container.changeBackground(R.drawable.iyzico_border_corner_radius)
        root.iyzico_double_border_check_imageView.setImageResource(R.drawable.iyzico_ic_check_button)
        root.iyzico_double_border_square_check_imageview.setImageResource(R.mipmap.iyzico_ic_square_check_button)
    }

    fun unFocus() {
        isClicked = false
        root.iyzico_double_border_root.apply {
            background = null
            //   setPadding(0, 0, 0, 0)
        }
        root.iyzico_double_border_container.changeBackground(R.drawable.iyzico_add_new_card_background)
        root.iyzico_double_border_check_imageView.setImageResource(R.drawable.iyzico_ic_empty_radio)
        root.iyzico_double_border_square_check_imageview.setImageResource(R.mipmap.iyzico_ic_square_check_clear_button)
        root.iyzico_double_border_square_check_imageview.changeBackground(R.drawable.iyzico_empty_radio_button_unchecked)

    }

    fun clickListener(f: (View, Boolean) -> Unit) {
        _onClick = f
    }

    fun setFocus() {
        focus()
    }

    fun setToBalance() {
        iyzico_double_border_square_check_imageview.show()
        iyzico_double_border_check_imageView.gone()
    }

    fun setToAmount() {
        iyzico_double_border_square_check_imageview.show()
        iyzico_double_border_check_imageView.gone()
        root.iyzico_double_border_container.changeBackground(R.color.iyzico_light_grey)
    }

    fun clearBorderFocusforCashout() {
        root.iyzico_double_border_root.background = null
        root.iyzico_double_border_container.changeBackground(R.color.iyzico_light_grey)
    }

    fun clearBorderFocus() {
        root.iyzico_double_border_root.background = null
        root.iyzico_double_border_container.changeBackground(R.drawable.iyzico_border_corner_radius_new_card_cardview)
    }

    fun setNormalBorder() {
        root.iyzico_double_border_root.background = null
        root.iyzico_double_border_container.changeBackground(R.drawable.iyzico_my_account_items_background)
    }
}