package com.android.iyzicosdk.ui.sms

import IyziCoAmountTransferFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.iyzicosdk.R
import com.android.iyzicosdk.core.IyziCoBaseFragment
import com.android.iyzicosdk.data.UIResponseCallBack
import com.android.iyzicosdk.data.model.response.IyziCoLoginResponse
import com.android.iyzicosdk.data.model.response.IyziCoLoginResponseComplete
import com.android.iyzicosdk.ui.info.IyziCoInfoFragment
import com.android.iyzicosdk.ui.account.IyziCoAccountFragment
import com.android.iyzicosdk.ui.settlement.IyziCoSettlementFragment
import IyziCoSupportFragment
import android.os.Handler
import android.view.Gravity
import androidx.annotation.Keep
import com.android.iyzicosdk.ui.email.IyziCoLoginFragment
import com.android.iyzicosdk.ui.secondary_login.IyziCoSecondaryLoginFragment
import com.android.iyzicosdk.util.config.IyziCoConfig
import com.android.iyzicosdk.util.constants.BundleConstans
import com.android.iyzicosdk.util.enums.IyziCoInfoScreenType
import com.android.iyzicosdk.util.enums.IyziCoLoginScreenType
import com.android.iyzicosdk.util.enums.IyziCoLoginChannelType
import com.android.iyzicosdk.util.enums.IyziCoSupportScreenType
import com.android.iyzicosdk.util.extensions.*
import com.android.iyzicosdk.util.extensions.autoFocus
import com.android.iyzicosdk.util.extensions.show
import com.android.iyzicosdk.util.extensions.toTimer
import kotlinx.android.synthetic.main.iyzico_fragment_sms.view.*
import java.util.*
import kotlin.concurrent.fixedRateTimer

@Keep
internal class IyziCoSMSFragment : IyziCoBaseFragment() {

    override val layoutRes: Int = R.layout.iyzico_fragment_sms
    lateinit var timer: Timer
    private var phoneNumber = ""
    var controller = IyziCoSMSFragmentController.newInstance(this)
    private lateinit var referanceCode: String
    private var memberId: String = ""
    private var gsmVerified: Boolean = false
    private var canClickResend = true


    override fun initUI(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        super.initUI(inflater, container, savedInstanceState)
        controller.init()

        if (gsmVerified) {
            root.iyzico_fragment_sms_change_phone.gone()
        } else {
            root.iyzico_fragment_sms_change_phone.show()
        }
        root.iyzico_fragment_phonenumber_textView.text =
            phoneNumber
        root.iyzico_fragment_sms_sms_editText.autoFocus()

    }

    fun getSmsArguments() {
        arguments?.let {
            referanceCode = it.getString(BundleConstans.RESPONSE_USER_REFERANCE) ?: ""
            memberId = it.getString(BundleConstans.RESPONSE_USER_ID) ?: ""
            phoneNumber = it.getString(BundleConstans.PHONE_NUMBER) ?: ""
            gsmVerified = it.getBoolean(BundleConstans.GSM_VERIFIED) ?: false
        }
    }

    override fun initUITopup() {
        super.initUITopup()
        complateLogin {
            clearStack()
            navigate(IyziCoAmountTransferFragment.newInstance(), false)
        }
    }

    override fun initUIRefund() {
        super.initUIRefund()
        root.iyzico_fragment_sms_change_phone.gone()
        root.iyzico_fragment_sms_support.show()
        root.iyzico_fragment_sms_support.let {
            it.setOnClickListener {
                navigate(IyziCoSupportFragment.newInstance(IyziCoSupportScreenType.SUPPORT), true)
            }
        }
        complateLogin {
            clearStack()
            navigate(
                IyziCoInfoFragment.newInstance(IyziCoInfoScreenType.REFAUND_SUCCESS),
                false
            )
        }
    }

    override fun initUICashOut() {
        super.initUICashOut()
        supportOrBack()
        root.iyzico_fragment_sms_support.let {
            it.setOnClickListener {
                navigate(IyziCoSupportFragment.newInstance(IyziCoSupportScreenType.SUPPORT), true)
            }
        }
        complateLogin {
            clearStack()
            navigate(IyziCoAmountTransferFragment.newInstance(), false)
        }
    }

    override fun initUISettlement() {
        super.initUISettlement()
        supportOrBack()
        root.iyzico_fragment_sms_support.let {
            it.setOnClickListener {
                navigate(IyziCoSupportFragment.newInstance(IyziCoSupportScreenType.SUPPORT), true)
            }
        }
        complateLogin {
            clearStack()
            navigate(IyziCoSettlementFragment.newInstance(), false)
        }
    }

    override fun initUIPayWithIyziCo() {
        super.initUIPayWithIyziCo()
        with(root) {
            iyzico_fragment_sms_title.show()
            iyzico_fragment_sms_description.show()
            iyzico_fragment_sms_textView1.gone()
            iyzico_fragment_sms_textView2.gone()
            iyzico_fragment_sms_linerLayout1.gravity = Gravity.CENTER
        }
        complateLogin {
            clearStack()
            navigate(IyziCoAccountFragment.newInstance(), false)
        }
    }

    override fun reCreate(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        super.reCreate(inflater, container, savedInstanceState)
        showTopItem()
    }

    fun supportOrBack() {
        with(root) {
            if (gsmVerified) {
                iyzico_fragment_sms_change_phone.gone()
                iyzico_fragment_sms_support.show()
            } else {
                iyzico_fragment_sms_change_phone.show()
                iyzico_fragment_sms_support.gone()
            }
        }
    }

    override fun clickListener() {
        root.iyzico_fragment_sms_change_phone.setOnClickListener {
            clearStack()
            navigate(
                IyziCoSecondaryLoginFragment.newInstance(
                    IyziCoLoginScreenType.CHANGE_PHONE, referanceCode, memberId, gsmVerified
                ), true
            )
        }

        root.iyzico_fragment_sms_try_again_textView.apply {
            setOnClickListener {
                if (canClickResend) {
                    reSend()
                    canClickResend = false
                    Handler().postDelayed(Runnable { /* Create an Intent that will start the Menu-Activity. */
                        canClickResend = true
                        root.iyzico_fragment_sms_try_again_textView.apply {
                            this.setText(R.string.iyzico_send_again)
                            this.setTextColor(resources.getColor(R.color.iyzico_secondary_dark_blue))
                            this.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.iyzico_ic_sms_refresh,
                                0,
                                0,
                                0
                            )
                        }
                    }, 10000)
                }
            }
        }
    }

    fun initRemainingTime() {

        var finishTime = 1 * 1000 * 60 * 5
        timer = fixedRateTimer("timer", false, 0, 1000) {
            activity?.runOnUiThread {
                finishTime = finishTime - 1000
                setclock(finishTime)
                root.iyzico_fragment_sms_clock_textview.text = finishTime.toLong().toTimer {
                    timer.cancel()
                    root.iyzico_fragment_sms_sms_editText.clearEdittexts()
                    initRemainingTime()
                }
            }
        }
    }

    fun setclock(time: Int) {
        if (time == (1000 * 60 * 5 - 10 * 1000)) {
            root.iyzico_fragment_sms_try_again_textView.show()
        }
        if (time < 1000 * 60) {
            root.iyzico_fragment_sms_hour_icon_image_view.setImageResource(R.drawable.iyzico_ic_basic_clock)
        }
    }

    fun complateLogin(
        continueButtonCallback: () -> Unit
    ) {
        root.iyzico_fragment_sms_sms_editText.getFullCode {
            controller.complateLogin(
                IyziCoConfig.CLIENT_IP,
                IyziCoLoginChannelType.THIRD_PARTY_APP.type, memberId,
                referanceCode,
                it,
                object : UIResponseCallBack<IyziCoLoginResponseComplete>(this) {
                    override fun onSuccess(response: IyziCoLoginResponseComplete?) {
                        super.onSuccess(response)
                        continueButtonCallback()
                    }

                    override fun onError(errorCode: Int, errorMessage: String) {
                        super.onError(errorCode, errorMessage)
                        root.iyzico_fragment_sms_sms_editText.clearEdittexts()
                    }
                })
        }
    }

    private fun reSend() {
        controller.reSendSms(
            IyziCoLoginChannelType.THIRD_PARTY_APP.type,
            memberId,
            referanceCode, object : UIResponseCallBack<IyziCoLoginResponse>(this) {
                override fun onSuccess(response: IyziCoLoginResponse?) {
                    super.onSuccess(response)
                    root.iyzico_fragment_sms_try_again_textView.apply {
                        this.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.iyzico_icn_send,
                            0,
                            0,
                            0
                        );
                        this.setText(R.string.iyzico_send_sms)
                        this.setTextColor(resources.getColor(R.color.iyzico_dark_green))
                        root.iyzico_fragment_sms_sms_editText.clearEdittexts()
                        timer.cancel()
                        initRemainingTime()
                    }
                }
            })
    }

    fun clearEdittexs() {
        root.iyzico_fragment_sms_sms_editText.clearEdittexts()

    }

    companion object {
        fun newInstance(
            referanceCode: String,
            memberId: String,
            phoneNumber: String,
            gsmVerified: Boolean
        ): Fragment {
            val fragment = IyziCoSMSFragment()
            val bundle = Bundle()
            bundle.putString(BundleConstans.PHONE_NUMBER, phoneNumber)
            bundle.putString(BundleConstans.RESPONSE_USER_ID, memberId)
            bundle.putString(BundleConstans.RESPONSE_USER_REFERANCE, referanceCode)
            bundle.putBoolean(BundleConstans.GSM_VERIFIED, gsmVerified)
            fragment.arguments = bundle
            return fragment
        }
    }
}