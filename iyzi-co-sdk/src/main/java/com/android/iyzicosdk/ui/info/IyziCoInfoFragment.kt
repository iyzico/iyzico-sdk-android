package com.android.iyzicosdk.ui.info

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Keep
import com.android.iyzicosdk.R
import com.android.iyzicosdk.core.IyziCoBaseFragment
import com.android.iyzicosdk.core.IyziCoActivity
import com.android.iyzicosdk.data.UIResponseCallBack
import com.android.iyzicosdk.ui.email.IyziCoLoginFragment
import com.android.iyzicosdk.util.config.IyziCoConfig
import com.android.iyzicosdk.util.constants.BundleConstans
import com.android.iyzicosdk.util.constants.IyziCoResourcesConstans
import com.android.iyzicosdk.util.constants.LottieConstans
import com.android.iyzicosdk.util.constants.TextMessages
import com.android.iyzicosdk.util.enums.IyziCoInfoScreenType
import com.android.iyzicosdk.util.enums.IyziCoLoginScreenType
import com.android.iyzicosdk.util.enums.IyziCoSupportScreenType
import com.android.iyzicosdk.util.enums.ResultCode
import com.android.iyzicosdk.util.extensions.*
import com.android.iyzicosdk.util.extensions.convertAmount
import com.android.iyzicosdk.util.extensions.gone
import com.android.iyzicosdk.util.extensions.setMargins
import com.android.iyzicosdk.util.extensions.show
import kotlinx.android.synthetic.main.iyzico_fragment_info.view.*
import kotlinx.android.synthetic.main.iyzico_fragment_information_transfer_layout.view.*
import kotlinx.android.synthetic.main.iyzico_lottie_animations_layout.view.*
import kotlinx.android.synthetic.main.iyzico_my_current_balance_layout.view.*
import kotlinx.android.synthetic.main.iyzico_payw_sticky.*
import java.io.Serializable

@Keep
internal class IyziCoInfoFragment : IyziCoBaseFragment() {

    override val layoutRes: Int = R.layout.iyzico_fragment_info
    private var screenType = IyziCoInfoScreenType.TRANSFER
    private var controller = IyziCoInfoFragmentController.newInstance(this)
    var balance: String = BundleConstans.ZERO_MONEY
    private var canTransferAmount = BundleConstans.ZERO_MONEY

    private var iban = ""
    private var explain = ""
    private var price = ""
    private var companyDetail = ""
    private var companyName = ""
    private var bankTransferId = 0
    private var htmlCode = ""
    override fun initUI(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ) {
        super.initUI(inflater, container, savedInstanceState)
        screenType =
            arguments?.let { it.getSerializable(BundleConstans.FROM) as IyziCoInfoScreenType }
                ?: IyziCoInfoScreenType.TRANSFER
        canTransferAmount =
            arguments?.let { it.getString(BundleConstans.BALANCE, BundleConstans.ZERO_MONEY) }
                ?: BundleConstans.ZERO_MONEY
        controller.setUi(screenType)
        explain = arguments?.getString(BundleConstans.EXPLAINING, "") ?: ""
        iban = arguments?.getString(BundleConstans.IBAN, "") ?: ""
        companyName = arguments?.getString(BundleConstans.COMPANY_NAME, "") ?: ""
        companyDetail = arguments?.getString(BundleConstans.COMPANY_DETAIL, "") ?: ""
        htmlCode = arguments?.getString(BundleConstans.HTML_CODE, "") ?: ""
        price = canTransferAmount
        bankTransferId = arguments?.getInt(BundleConstans.BANK_TRANSFER_PAYMENT_ID, 0) ?: 0



    }

    override fun initUIPayWithIyziCo() {
        super.initUIPayWithIyziCo()
        (activity as IyziCoActivity).iyzico_payw_sticky_brand_textView.setMargins(16, 17)
        removeTimer()
        hideWalletSticky()
        showStickLayout()
        hideTimerSticky()

    }

    override fun initUICashOut() {
        super.initUICashOut()
        balance = arguments?.let { it.getString(BundleConstans.BALANCE) }!!
        showIyziCoBalance(balance)
    }

    override fun initUITopup() {
        super.initUITopup()
        hideLoadingAnimation()
        iban = arguments?.getString(BundleConstans.IBAN, "") ?: ""
        explain = arguments?.getString(BundleConstans.EXPLAINING, "") ?: ""
        price = arguments?.getString(BundleConstans.BALANCE_TO_SEND, "") ?: ""
        companyDetail = arguments?.getString(BundleConstans.COMPANY_DETAIL, "") ?: ""
        companyName = arguments?.getString(BundleConstans.COMPANY_NAME, "") ?: ""
    }

    override fun initUIRefund() {
        super.initUIRefund()
        showIyziCoBalance(IyziCoResourcesConstans.IyziCoWalletPrice.toString())
    }


    override fun clickListener() {
        super.clickListener()
        root.iyzico_fragment_info_custom_button.clickListener {
            when (screenType) {
                IyziCoInfoScreenType.ERROR -> {
                    IyziCoConfig.IYZI_CO_AUTHORIZATION_KEY = ""
                    navigate(
                        IyziCoLoginFragment.newInstance(
                            IyziCoLoginScreenType.TRY_AGAIN
                        ), false
                    )
                }
                else -> {
                    callbackError(ResultCode.OK, TextMessages.SUCCESS)
                    finish()
                }
            }
        }
        root.iyzico_fragment_secondary_info_custom_button.clickListener {
            controller.clickSecondary(
                screenType,
                iban,
                companyDetail,
                price,
                explain,
                companyName,
                bankTransferId
            )

        }
        root.iyzico_fragment_info_transfer_confirmation_google_play_icon.setOnClickListener {
            controller.goPlayStore()
        }
    }

    //setlement sayfasını gösterir
    fun initforRefaundSuccess() {
        setIsSuccess()
        initUISettlementSuccess()
        root.iyzico_fragment_info_add_your_account_text_textview.setText(R.string.iyzico_refaund_payment)
        root.iyzico_fragment_info_infotext_textView.setText(R.string.iyzico_refaund_received)
        root.iyzico_fragment_info_title_top_textView.show()
        root.iyzico_fragment_views_line1.show()
        root.iyzico_fragment_views_line2.show()
        root.iyzico_fragment_info_title_top_textView.setText(R.string.iyzico_fragment_infto_transfer_assurance_of_iyzico)
    }

    fun initUISettlementSuccess() {
        setIsSuccess()
        initForTransferConfirmation()
        root.iyzico_fragment_info_settlement_success_layout.show()
    }

    //normal para transferinde kullanılır
    fun initForTransfer() {
        setIsSuccess()
        root.iyzico_fragment_info_animation?.setAnimation(LottieConstans.success)
        root.iyzico_fragment_info_animation_icon.setImageResource(R.drawable.iyzico_ic_check)
        root.iyzico_fragment_info_item_transfer.show()
        root.iyzico_fragment_info_custom_button.textView.text =
            getString(R.string.iyzico_common_button_back_app)
    }

    //ödeme sayfasıdır
    fun initForPayment() {
        setIsSuccess()
        hideWalletSticky()
        root.iyzico_fragment_info_animation?.setAnimation(LottieConstans.info)
        root.iyzico_fragment_info_animation_icon.setImageResource(R.drawable.iyzico_ic_info_i)
        root.iyzico_fragment_info_item_transfer.show()
        root.iyzico_fragment_info_custom_button.textView.text =
            getString(R.string.iyzico_common_button_back_app)
        root.iyzico_fragment_secondary_info_custom_button.show()
        root.iyzico_fragment_secondary_info_custom_button.textView.text =
            getString(R.string.iyzico_see_bank_Details)
        root.iyzico_fragment_information_transfer_layout_second_title.text =
            getString(R.string.iyzico_fragment_info_transfer_details_second_title)
        root.iyzico_fragment_information_transfer_layout_title.text =
            getString(R.string.iyzico_fragment_info_transfer_details_title)
    }

    //ödeme talebidir cashout
    fun initForTransferConfirmation() {
        setIsSuccess()
        root.iyzico_fragment_info_animation?.setAnimation(LottieConstans.success)
        root.iyzico_fragment_info_animation_icon.setImageResource(R.drawable.iyzico_ic_check)
        root.iyzico_fragment_info_title_top_textView.show()
        root.iyzico_fragment_info_title_top_textView.text =
            getString(R.string.iyzico_fragment_infto_transfer_assurance_of_iyzico)
        root.iyzico_fragment_info_infotext_textView.text =
            getString(R.string.iyzico_fragment_info_transfer_success)
        root.iyzico_fragment_info_infotext_textView.show()
        root.iyzico_fragment_info_secondary_infotext_textView.text =
            getString(R.string.iyzico_fragment_info_transfer_secondary_info)
        root.iyzico_fragment_info_secondary_infotext_textView.show()
        showPlayStore()
    }

    fun initCashoutWait() {
        initForTransferConfirmation()
        root.iyzico_fragment_info_animation?.setAnimation(LottieConstans.info)
        root.iyzico_fragment_info_animation_icon.setImageResource(R.drawable.iyzico_icn_white_clock)
        root.iyzico_fragment_info_infotext_textView.text =
            getString(R.string.iyzico_success_cashout_waiting_title)
    }

    fun initTopupWait() {
        initforTransferConfirmationTopup()
        root.iyzico_fragment_info_animation?.setAnimation(LottieConstans.info)
        root.iyzico_fragment_info_animation_icon.setImageResource(R.drawable.iyzico_icn_white_clock)
        root.iyzico_fragment_info_infotext_textView.text =
            getString(R.string.iyzico_success_top_up_waiting_title)
    }

    //ödeme talebidir. topup
    fun initforTransferConfirmationTopup() {
        setIsSuccess()
        root.iyzico_fragment_info_animation?.setAnimation(LottieConstans.success)
        root.iyzico_fragment_info_animation_icon.setImageResource(R.drawable.iyzico_ic_check)
        root.iyzico_fragment_info_title_top_textView.show()
        root.iyzico_fragment_info_title_top_textView.text = canTransferAmount.addTlIcon()
        root.iyzico_fragment_info_infotext_textView.text =
            getString(R.string.iyzico_added_your_account)
        root.iyzico_fragment_info_infotext_textView.show()
        root.iyzico_fragment_info_secondary_infotext_textView.text =
            "${IyziCoResourcesConstans.IyziCoBrand} ${getString(R.string.iyzico_direct_lidyana)}"
        root.iyzico_fragment_info_secondary_infotext_textView.show()
        showPlayStore()
    }

    //hata döndürür
    fun initForError() {
        root.iyzico_fragment_info_animation?.setAnimation(LottieConstans.error)
        root.iyzico_fragment_info_animation_icon.setImageResource(R.drawable.iyzico_ic_white_close)
        root.iyzico_fragment_info_infotext_textView.show()
        root.iyzico_fragment_info_infotext_textView.text =
            getString(R.string.iyzico_not_complate_payment)
        root.iyzico_fragment_secondary_info_custom_button.show()
        root.iyzico_fragment_secondary_info_custom_button.textView.text =
            getString(R.string.iyzico_common_button_back_app)
        root.iyzico_fragment_info_custom_button.textView.text =
            getString(R.string.iyzico_common_button_try_again)
    }

    //3D ödeme için kullanılır.
    fun initForThreeD() {
        root.iyzico_fragment_info_animation?.setAnimation(LottieConstans.info)
        root.iyzico_fragment_info_infotext_textView.show()
        root.iyzico_fragment_info_infotext_textView.text =
            getString(R.string.iyzico_fragment_info_three_d_payment_title)
        root.iyzico_fragment_info_custom_button.gone()
        root.iyzico_fragment_info_secondary_infotext_textView.show()
        root.iyzico_fragment_info_secondary_infotext_textView.text =
            getString(R.string.iyzico_directed_three_d_payment)
        root.iyzico_fragment_info_animation_icon.setImageResource(R.drawable.iyzico_ic_redirect)
        Handler().postDelayed(Runnable {
            navigate(
                IyziCoSupportFragment.newInstance(IyziCoSupportScreenType.THREED_PAYMENT, htmlCode), false
            )
        }, 1500)

    }

    fun showPlayStore() {
        root.iyzico_fragment_info_transfer_confirmation_google_play_icon.show()
        root.iyzico_fragment_info_transfer_confirmation_info.text =
            getString(R.string.iyzico_fragment_info_transfer_confirm_message)
        root.iyzico_fragment_info_custom_button.textView.text =
            getString(R.string.iyzico_common_button_back_app)
        root.iyzico_fragment_info_transfer_confirmation_info.show()
    }

    fun getRetrieverMemberBalance() {
        controller.getRetrieverMemberBalance(object : UIResponseCallBack<String>(this) {
            override fun onSuccess(response: String?) {
                super.onSuccess(response)
                showIyziCoBalance(response?.convertAmount() ?: BundleConstans.ZERO_MONEY)
            }
        })
    }

    companion object {
        //default değerler atanmış olan iban price vb değerler eft ile ödeme yapıldığı durumda success sayfasında gösterilecek değerlerdir. sadece bu durum için kullanılacağı için
        //zorunlu tutulmamıştır.
        fun newInstance(
            type: IyziCoInfoScreenType,
            balance: String = BundleConstans.ZERO_MONEY,
            iban: String = "",
            companyDetail: String = "",
            explain: String = "",
            companyName: String = "",
            bankTransferId: Int = 0,
            htmlCode:String = ""
        ): IyziCoInfoFragment {
            val fragment = IyziCoInfoFragment()
            val bundle = Bundle()
            bundle.putSerializable(BundleConstans.FROM, type as Serializable)
            bundle.putString(BundleConstans.COMPANY_NAME, companyName)
            bundle.putString(BundleConstans.EXPLAINING, explain)
            bundle.putString(BundleConstans.IBAN, iban)
            bundle.putString(BundleConstans.COMPANY_DETAIL, companyDetail)
            bundle.putInt(BundleConstans.BANK_TRANSFER_PAYMENT_ID, bankTransferId)
            bundle.putString(BundleConstans.HTML_CODE, htmlCode)
            bundle.putString(BundleConstans.BALANCE, balance)
            fragment.arguments = bundle
            return fragment
        }
    }
}