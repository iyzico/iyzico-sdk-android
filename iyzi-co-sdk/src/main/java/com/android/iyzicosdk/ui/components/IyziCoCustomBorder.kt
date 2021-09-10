package com.android.iyzicosdk.ui.components

import android.content.Context
import android.graphics.Color
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.android.iyzicosdk.R
import com.android.iyzicosdk.util.SavedState
import com.android.iyzicosdk.util.enums.InputEnum
import com.android.iyzicosdk.util.extensions.*
import kotlinx.android.synthetic.main.iyzico_card_cvv_edit_text.view.*
import kotlinx.android.synthetic.main.iyzico_component_border.view.*


internal class IyziCoCustomBorder : LinearLayout {

    private var borderText = ""
    private var hint = ""
    private var type = ""
    private var borderSize = "normal"

    lateinit var root: View

    lateinit var editText: EditText

    private var clearPadding = "0"

    private var _focusInput: (view: View) -> Unit = { _ -> }
    private var _unFocusInput: (view: View) -> Unit = { _ -> }

    private var editTextValueListener: (value: String) -> Unit = { _ -> }

    constructor(context: Context?) : super(context)


    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {

        if (context != null) {
            val attributes = context.obtainStyledAttributes(attrs, R.styleable.IyziCoCustomBorder)

            attributes.apply {

                borderText = getString(R.styleable.IyziCoCustomBorder_iyzico_borderText) ?: ""
                hint = getString(R.styleable.IyziCoCustomBorder_iyzico_borderHintText) ?: ""
                type = getString(R.styleable.IyziCoCustomBorder_iyzico_type)
                    ?: InputEnum.NORMAL.type
                borderSize =
                    getString(R.styleable.IyziCoCustomBorder_iyzico_border_size) ?: "normal"

                clearPadding = getString(R.styleable.IyziCoCustomBorder_iyzico_clear_padding) ?: "0"


                recycle()
            }

        }

    }


    override fun onFinishInflate() {
        super.onFinishInflate()

        root = inflate(
            context,
            R.layout.iyzico_component_border,
            this
        )

        val container: ViewGroup =
            root.findViewById(R.id.component_iyzico_border_edit_text_container)
        root.findViewById<TextView>(R.id.component_iyzico_border_title_text).text = borderText

        setHeight(borderSize)

        when (type) {
            InputEnum.PHONE.type -> {
                root.component_iyzico_border_country_image_side.show()
            }
            InputEnum.PIN.type -> {
                root.component_iyzico_border_title_text.gone()
                root.component_iyzico_border_error_img.gone()
                root.component_iyzico_border_view.gone()
                root.component_iyzico_border_country_image_side.gone()
                root.component_iyzico_border_title_text.changeTextSize(R.dimen.iyzico_input_text_primary_size)
            }
            else -> {
                root.component_iyzico_border_country_image_side.gone()
            }
        }


        while (childCount > 1) {
            val child: View = getChildAt(0)
            removeView(child)
            container.addView(child, child.layoutParams)
        }


        if (clearPadding == "1") {
            root.component_iyzico_border_edit_text_container.apply {
                setMargins(0, 0, 0, 0)
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                requestLayout()
            }
        }
    }

    fun setCustomEditText(editText: EditText) {

        this.editText = editText
        this.hint = hint
        editText.setHintTextColor(Color.TRANSPARENT)

        this.editText.fieldFocusListener(focus = {
            if (type == InputEnum.PIN.type) {
                pinSpecificFocus()
            } else {
                focus()
            }
            _focusInput(it)
        }, unFocus = {
            when (type) {
                /*   InputEnum.PHONE.type -> {
                       phoneSpecific()
                   }*/
                InputEnum.PIN.type -> {
                    pinSpecificUnFocus()
                }
                else -> {
                    unFocus()
                }
            }

            _unFocusInput(it)
        })

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.isNotEmpty()) {
                    /**
                     * Bazı text alanları disable olduğu zaman geri dönüşte soruna sebep olabiliyor bunu engellemek için if koşulu koyuldu
                     */
                    if (editText.isEnabled) {
                        //focus()
                    }
                }

                editTextValueListener(s?.toString() ?: "")
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })


        root.component_iyzico_border_root.setOnClickListener {
            this.editText.requestFocus()
            openKeyboard()
        }

        if (type == InputEnum.PHONE.type) {
            phoneSpecific()
        }

    }

    fun editTextValueListener(f: (value: String) -> Unit) {
        editTextValueListener = f
    }

    private fun openKeyboard() {
        val imm: InputMethodManager? =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    /*  override fun onDetachedFromWindow() {
          editText.apply {
              onFocusChangeListener = null
              addTextChangedListener(null)
          }
          super.onDetachedFromWindow()
      }*/

    fun fillEditTextDesign() {
        root.component_iyzico_border_view.show()
        root.iyzico_component_border_text_contraint_bottom_view.gone()
        root.component_iyzico_border_title_text.apply {
            text = borderText
            changeTextSize(R.dimen.iyzico_input_text_secondary_size)
            //   typeface = fontType
        }

    }

    private fun focus() {
        root.component_iyzico_border_view.show()
        root.component_iyzico_border_root.changeBackground(R.drawable.iyzico_border_corner_radius_fill_color)
        root.component_iyzico_border_container.changeBackground(R.drawable.iyzico_border_corner_radius)
        root.component_iyzico_border_title_text.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.iyzico_black
            )
        )
        root.component_iyzico_border_error_img.gone()
        root.iyzico_component_border_text_contraint_bottom_view.gone()

        // val fontType = ResourcesCompat.getFont(context, R.font.markpro)

        root.component_iyzico_border_title_text.apply {
            text = borderText
            changeTextSize(R.dimen.iyzico_input_text_secondary_size)
            //   typeface = fontType
        }

        editText.setHintTextColor(ContextCompat.getColor(context, R.color.iyzico_hint_color))
    }

    fun setBlackText() {
        root.component_iyzico_border_title_text.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.iyzico_secondary_text_color
            )
        )
    }

    private fun unFocus() {
        //  editText.hint = ""

        editText.setHintTextColor(Color.TRANSPARENT)

        root.component_iyzico_border_root.changeBackground(R.drawable.iyzico_border_corner_radius_un_focus_fill_color)
        root.component_iyzico_border_container.changeBackground(R.drawable.iyzico_border_corner_un_focus_radius)
        if (editText.text.toString().isEmpty()) {
            root.component_iyzico_border_view.gone()
            root.component_iyzico_border_error_img.gone()
            root.iyzico_component_border_text_contraint_bottom_view.show()
            //  val fontType = ResourcesCompat.getFont(context, R.font.markpromedium)

            root.component_iyzico_border_title_text.apply {
                text = borderText
                changeTextSize(R.dimen.iyzico_input_text_primary_size)
                //   typeface = fontType
            }
        }
    }

    fun forceUnfocus() {
        unFocus()
    }

    fun forceFocus() {
        focus()
    }

    fun errorBorder(errorMessage: String) {
        focus()
        root.component_iyzico_border_error_img.show()
        root.component_iyzico_border_title_text.text = "$borderText - $errorMessage"
        root.component_iyzico_border_title_text.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.iyzico_red
            )
        )
        root.component_iyzico_border_root.background = null
        root.component_iyzico_border_container.changeBackground(R.drawable.iyzico_border_corner_error_radius)
    }

    fun disableBorder() {
        root.component_iyzico_border_container.changeBackground(R.drawable.iyzico_border_corner_disable_radius)
        root.component_iyzico_border_title_text.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.iyzico_secondary_text_color
            )
        )
        root.component_iyzico_border_root.changeBackground(R.drawable.iyzico_border_corner_radius_disable_fill_color)
        editText.isFocusable = false
        editText.isEnabled = false
    }

    fun changeTitleProgrammatically(title: String, hint: String, size: String = "normal") {
        borderText = title
        root.component_iyzico_border_title_text.text = title
        editText.hint = hint
        root.component_iyzico_border_title_text.setMargins(7, 0,12, 15)
        setHeight(size)
    }

    private fun setHeight(size: String = "normal") {
        if (size == "small") {
            root.component_iyzico_border_root.layoutParams.height = 59.dp
            root.component_iyzico_border_root.requestLayout()
            root.iyzico_component_border_bottom_view.show()
        }
    }

    private fun phoneSpecific() {
        editText.setHintTextColor(Color.TRANSPARENT)
        root.component_iyzico_border_root.changeBackground(R.drawable.iyzico_border_corner_radius_un_focus_fill_color)
        root.component_iyzico_border_container.changeBackground(R.drawable.iyzico_border_corner_un_focus_radius)
        root.component_iyzico_border_view.show()
        root.component_iyzico_border_title_text.text = borderText
        root.component_iyzico_border_error_img.gone()
        root.component_iyzico_border_title_text.changeTextSize(R.dimen.iyzico_input_text_secondary_size)

    }

    private fun pinSpecificFocus() {
        root.component_iyzico_border_root.changeBackground(R.drawable.iyzico_border_corner_radius_fill_color)
        root.component_iyzico_border_container.changeBackground(R.drawable.iyzico_border_corner_radius)

        editText.setTextColor(ContextCompat.getColor(context, R.color.iyzico_primary_text_color))

        root.component_iyzico_border_root.backgroundTintList =
            ContextCompat.getColorStateList(context, R.color.iyzico_light_blue)

        editText.setText("")

    }


    private fun pinSpecificUnFocus() {

        // if (editText.text.toString().isEmpty()) {
        root.component_iyzico_border_root.changeBackground(R.drawable.iyzico_border_corner_radius_un_focus_fill_color)
        root.component_iyzico_border_container.changeBackground(R.drawable.iyzico_border_corner_un_focus_radius)

        editText.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.iyzico_primary_text_color
            )
        )
        /* } else {
             lastPin()
         }
 */

        /*       val colorFrom = ContextCompat.getColor(context, R.color.iyzico_white)
               val colorTo = ContextCompat.getColor(context, R.color.iyzico_blur_blue)

               val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
               colorAnimation.apply {
                   duration = 1000
                   addUpdateListener {
                       // root.component_iyzico_border_container.setBackgroundColor(it.animatedValue as Int)
                   }
               }.start()*/
    }


    fun lastPin() {
        // Handler(Looper.getMainLooper()).postDelayed({
        root.component_iyzico_border_root.backgroundTintList =
            ContextCompat.getColorStateList(context, R.color.iyzico_white)

        root.component_iyzico_border_container.changeBackground(R.drawable.iyzico_border_corner_un_focus_radius)


    }

    fun focusInput(f: (view: View) -> Unit) {
        _focusInput = f
    }

    fun unFocusInput(f: (view: View) -> Unit) {
        _unFocusInput = f
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

