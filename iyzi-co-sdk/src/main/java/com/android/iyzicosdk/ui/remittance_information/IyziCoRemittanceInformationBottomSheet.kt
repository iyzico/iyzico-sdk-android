package com.android.iyzicosdk.ui.remittance_information

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.Keep
import androidx.fragment.app.FragmentManager
import com.android.iyzicosdk.R
import com.android.iyzicosdk.core.IyziCoActivity
import com.android.iyzicosdk.data.UIResponseCallBack
import com.android.iyzicosdk.data.model.response.IyziCoPWIResponse
import com.android.iyzicosdk.ui.info.IyziCoInfoFragment
import com.android.iyzicosdk.ui.info.IyziCoInfoFragmentController
import com.android.iyzicosdk.util.IyziCoImageLoaderUtility
import com.android.iyzicosdk.util.config.IyziCoConfig
import com.android.iyzicosdk.util.constants.BundleConstans
import com.android.iyzicosdk.util.constants.BundleConstans.BALANCE
import com.android.iyzicosdk.util.constants.BundleConstans.BOTTOM_SHEET_TYPE
import com.android.iyzicosdk.util.constants.BundleConstans.COMPANY_DETAIL
import com.android.iyzicosdk.util.constants.BundleConstans.COMPANY_NAME
import com.android.iyzicosdk.util.constants.BundleConstans.EXPLAINING
import com.android.iyzicosdk.util.constants.BundleConstans.IBAN
import com.android.iyzicosdk.util.constants.IyziCoResourcesConstans
import com.android.iyzicosdk.util.enums.IyziCoButtonType
import com.android.iyzicosdk.util.enums.IyziCoBottomSheetType
import com.android.iyzicosdk.util.enums.IyziCoInfoScreenType
import com.android.iyzicosdk.util.enums.IyziCoSDKType
import com.android.iyzicosdk.util.extensions.addTlIcon
import com.android.iyzicosdk.util.extensions.gone
import com.android.iyzicosdk.util.extensions.show
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.iyzico_cell_bank_item.view.*
import kotlinx.android.synthetic.main.iyzico_layout_remittence_information_bottom_sheet.view.*

@Keep
internal class IyziCoRemittanceInformationBottomSheet : BottomSheetDialogFragment() {

    lateinit var root: View
    private var type = IyziCoBottomSheetType.TRANSFER_DETAIL
    private var iban = ""
    private var explain = ""
    private var companyDetail = ""
    private var controller = IyziCoRemittanceInformationBottomSheetController.newInstance(this)
    private var companyName = ""
    private var bankTransferPaymentId = "0"
    private var bankLogo: String = ""
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root =
            inflater.inflate(
                R.layout.iyzico_layout_remittence_information_bottom_sheet,
                container,
                false
            )
        arguments?.let {
            type = arguments?.getSerializable(BOTTOM_SHEET_TYPE) as IyziCoBottomSheetType
            iban = arguments?.getString(IBAN) ?: ""
            explain = arguments?.getString(EXPLAINING) ?: ""
            companyDetail = arguments?.getString(COMPANY_DETAIL) ?: ""
            companyName = arguments?.getString(COMPANY_NAME) ?: ""
            bankTransferPaymentId =
                arguments?.getString(BundleConstans.BANK_TRANSFER_PAYMENT_ID, "0") ?: "0"
            bankLogo = arguments?.getString(BundleConstans.BANK_LOGO_URL) ?: ""

        }

        setButton(type)
        setTexts()
        clickListener()
        return root
    }


    private fun setButton(type: IyziCoBottomSheetType) {
        when (type) {
            IyziCoBottomSheetType.TRANSFER_DETAIL -> {
                root.iyzico_layout_remittance_information_accept.textView.text =
                    getString(R.string.iyzico_common_finish_order)
                root.iyzico_layout_remittance_information_accept.setButton(IyziCoButtonType.BLUE_FILL)
                root.iyzico_remittence_information_balance_to_transfer.setTexts(
                    IyziCoResourcesConstans.IYZICO_PAID_PRICE.toString().addTlIcon()
                )

            }
            IyziCoBottomSheetType.TRANSFER_SEE_INFO -> {
                root.iyzico_layout_remittance_information_accept.textView.text =
                    getString(R.string.iyzico_common_share)
                root.iyzico_layout_remittance_information_accept.setButton(IyziCoButtonType.EMPTY)
            }
            IyziCoBottomSheetType.TRANSFER_TOP_UP -> {
                root.iyzico_remittence_information_balance_to_transfer.gone()
                root.iyzico_layout_remittance_information_accept.textView.text =
                    getString(R.string.iyzico_common_share)
                root.iyzico_layout_remittance_back_app.show()
            }
        }
    }

    private fun clickListener() {
        when (type) {
            IyziCoBottomSheetType.TRANSFER_DETAIL -> {
                root.iyzico_layout_remittance_information_accept.clickListener {

                    controller.pwBankTransferNotifty(bankTransferPaymentId)

                }
            }
            IyziCoBottomSheetType.TRANSFER_SEE_INFO -> {
                share()
            }
            IyziCoBottomSheetType.TRANSFER_TOP_UP -> {
                share()
                root.iyzico_layout_remittance_back_app.clickListener {
                    activity?.finish()
                }
            }
        }
    }

    fun openInformationPage() {
        (activity as IyziCoActivity).openFragment(
            IyziCoInfoFragment.newInstance(
                IyziCoInfoScreenType.PAYMENT,
                IyziCoResourcesConstans.IYZICO_PAID_PRICE.toString().addTlIcon(),
                iban,
                companyDetail,
                explain,
                companyName
            )
        )
        dismiss()
    }

    fun share() {
        root.iyzico_layout_remittance_information_accept.clickListener {
            var information =
                getString(R.string.iyzico_bank_information_iban) + iban + "\n" + getString(R.string.iyzico_bank_information_referance_no) + explain + "\n" + getString(
                    R.string.iyzico_bank_information_account_holder
                ) + companyDetail

            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    information
                )
                type = "text/plain"

            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

    private fun setTexts() {
        root.iyzico_remittence_information_iban.setTexts(iban)
        root.iyzico_remittence_information_aboutAccount.setTexts(companyDetail)
        root.iyzico_remittence_information_comment.setTexts(explain)
        root.iyzico_remittence_info_bank_name.text = companyName
        IyziCoImageLoaderUtility.setImageForSvg(
            requireContext(), bankLogo,
            root.iyzico_fragment_remittance_eft_bank_imageView
        )
    }

    companion object {
        fun show(
            fm: FragmentManager?,
            bottomSheetType: IyziCoBottomSheetType,
            iban: String,
            companyDetail: String,
            explain: String,
            companyName: String,
            bankTransferaymentId: String = "0",
            bankLogo: String = ""
        ) {
            fm?.let {
                var bundle: Bundle = Bundle()
                bundle.putSerializable(BundleConstans.BOTTOM_SHEET_TYPE, bottomSheetType)
                bundle.putString(BundleConstans.COMPANY_NAME, companyName)
                bundle.putString(BundleConstans.EXPLAINING, explain)
                bundle.putString(BundleConstans.IBAN, iban)
                bundle.putString(BundleConstans.COMPANY_DETAIL, companyDetail)
                bundle.putString(BundleConstans.BANK_TRANSFER_PAYMENT_ID, bankTransferaymentId)
                bundle.putString(BundleConstans.BANK_LOGO_URL, bankLogo)
                val bottomSheetFragment = IyziCoRemittanceInformationBottomSheet()
                bottomSheetFragment.arguments = bundle
                bottomSheetFragment.show(it, bottomSheetFragment.tag)
            }
        }
    }
}