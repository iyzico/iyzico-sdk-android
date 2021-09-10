package com.android.iyzicosdk.ui.support

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.Keep
import androidx.fragment.app.FragmentManager
import com.android.iyzicosdk.R
import com.android.iyzicosdk.util.extensions.clearFunNumber
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.iyzico_bottom_support_fragment.view.*

@Keep
internal class IyziCoBottomSupportScreen : BottomSheetDialogFragment() {
    lateinit var root: View

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NORMAL, R.style.IyziCoCustomBottomSheetDialogTheme)
        val bottomSheetDialog =
            super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener { dialog: DialogInterface ->
            val dialogc = dialog as BottomSheetDialog

            val bottomSheet =
                dialogc.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            val bottomSheetBehavior: BottomSheetBehavior<*> =
                BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!)
            bottomSheetBehavior.peekHeight = Resources.getSystem().displayMetrics.heightPixels
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        }
        return bottomSheetDialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.IyziCoCustomBottomSheetDialogTheme)
    }

    fun makePhoneCall(number: String): Boolean {
        try {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:$number"))
            startActivity(intent)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun sendMail(mail:String) {
        val intent = Intent(Intent.ACTION_SEND)
        val addressees = arrayOf(mail)
        intent.putExtra(Intent.EXTRA_EMAIL, addressees)
        intent.setType("message/rfc822")
        startActivity(Intent.createChooser(intent, "Send Email using:"));
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.iyzico_bottom_support_fragment, container, false)
        root.iyzico_bottom_support_screen_phone_layout.setOnClickListener {
            makePhoneCall(getString(R.string.iyzico_phone_number_text).clearFunNumber())
        }
        root.iyzico_bottom_support_screen_mail_layout.setOnClickListener{
            sendMail(getString(R.string.iyzico_mail))
        }
        return root
    }

    companion object {
        fun showSupportBottom(fm: FragmentManager?) {
            fm?.let {
                val bottomFragment = IyziCoBottomSupportScreen()
                bottomFragment.show(it, bottomFragment.tag)
            }
        }
    }
}