package com.android.iyzicosdk.ui.have_member_login

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Keep
import com.android.iyzicosdk.R
import com.android.iyzicosdk.core.IyziCoBaseFragment
import com.android.iyzicosdk.data.UIResponseCallBack
import com.android.iyzicosdk.data.model.response.IyziCoLoginResponse
import com.android.iyzicosdk.ui.email.IyziCoLoginFragment
import com.android.iyzicosdk.ui.sms.IyziCoSMSFragment
import com.android.iyzicosdk.util.config.IyziCoConfig
import com.android.iyzicosdk.util.constants.IyziCoResourcesConstans
import com.android.iyzicosdk.util.enums.IyziCoLoginScreenType
import com.android.iyzicosdk.util.enums.IyziCoSDKType
import com.android.iyzicosdk.util.enums.IyziCoLoginChannelType
import com.android.iyzicosdk.util.extensions.convertServiceMaskedPhoneNumber
import com.android.iyzicosdk.util.extensions.gone
import com.android.iyzicosdk.util.extensions.show
import kotlinx.android.synthetic.main.iyzico_fragment_member_login.view.*
import kotlinx.android.synthetic.main.iyzico_login_continue_this_mail_button.view.*

@Keep
internal class IyziCoMemberLoginFragment : IyziCoBaseFragment() {

    override val layoutRes: Int = R.layout.iyzico_fragment_member_login

    var controller = IyziCoMemberLoginFragmentController.newInstance(this)

    override fun initUI(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        super.initUI(inflater, container, savedInstanceState)
        hideKeyboard()
        removeTimer()
        root.iyzico_login_continue_acount_mail_textView.text = IyziCoResourcesConstans.IyziCoEmail
        when (IyziCoConfig.IYZI_CO_SDK_TYPE) {
            IyziCoSDKType.CASH_OUT -> {
                root.iyzico_fragment_member_login_title.text =
                    getString(R.string.iyzico_fragment_login_settlement_title)
                root.iyzico_fragment_login_change_acount_textView.gone()

            }
            IyziCoSDKType.SETTLEMENT -> {
                root.iyzico_fragment_member_login_title.text =
                    getString(R.string.iyzico_fragment_login_settlement_title)
                root.iyzico_fragment_login_change_acount_textView.gone()

            }
            IyziCoSDKType.REFUND -> {
                root.iyzico_fragment_member_login_title.text =
                    getString(R.string.iyzico_fragment_login_refund_title)
                root.iyzico_fragment_login_change_acount_textView.gone()
            }
            else -> {
                root.iyzico_fragment_login_change_acount_textView.show()
            }
        }
    }

    override fun reCreatePayWithIyziCo() {
        super.reCreatePayWithIyziCo()
        removeTimer()
        hideKeyboard()
    }

    override fun clickListener() {
        super.clickListener()
        root.iyzico_login_continue_this_mail_button.setOnClickListener {
            loginInitialize()

        }

        root.iyzico_fragment_login_change_acount_textView.setOnClickListener {
            navigate(IyziCoLoginFragment.newInstance(IyziCoLoginScreenType.CHANGE_MAIL), false)
            //navigateUp()
        }
    }

    private fun loginInitialize() {
        controller.loginUser(IyziCoConfig.CLIENT_IP,
            IyziCoResourcesConstans.IyziCoEmail,
            IyziCoLoginChannelType.THIRD_PARTY_APP.type,
            object : UIResponseCallBack<IyziCoLoginResponse>(this) {
                override fun onSuccess(response: IyziCoLoginResponse?) {
                    super.onSuccess(response)
                    response?.let {
                        goOtp(
                            it.referenceCode,
                            it.memberUserId,
                            it.maskedGsmNumber,
                            it.gsmVerified
                        )
                    }
                }
            })
    }

    fun goOtp(referanceCode: String, memberId: String, phoneNumber: String, gsmVerified: Boolean) {
        if (IyziCoConfig.IYZI_CO_SDK_TYPE == IyziCoSDKType.PAY_WITH_IYZI_CO) {
            startPayWithIyziCoTimer()
        }
        navigate(
            IyziCoSMSFragment.newInstance(
                referanceCode,
                memberId,
                phoneNumber = phoneNumber.convertServiceMaskedPhoneNumber(),
                gsmVerified = gsmVerified
            ), true
        )
    }

    companion object {
        fun newInstance(): IyziCoMemberLoginFragment {
            return IyziCoMemberLoginFragment()
        }
    }
}