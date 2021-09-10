package com.android.iyzicosdk.ui.email

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Keep
import com.android.iyzicosdk.R
import com.android.iyzicosdk.core.IyziCoBaseFragment
import com.android.iyzicosdk.data.UIResponseCallBack
import com.android.iyzicosdk.data.model.response.IyziCoInitResponse
import com.android.iyzicosdk.data.model.response.IyziCoLoginResponse
import com.android.iyzicosdk.data.model.response.IyziCoPWIResponse
import com.android.iyzicosdk.ui.have_member_login.IyziCoMemberLoginFragment
import com.android.iyzicosdk.ui.secondary_login.IyziCoSecondaryLoginFragment
import com.android.iyzicosdk.ui.sms.IyziCoSMSFragment
import com.android.iyzicosdk.util.config.IyziCoConfig
import com.android.iyzicosdk.util.constants.BundleConstans
import com.android.iyzicosdk.util.constants.IyziCoResourcesConstans
import com.android.iyzicosdk.util.enums.IyziCoCurrentType
import com.android.iyzicosdk.util.enums.IyziCoLoginScreenType
import com.android.iyzicosdk.util.enums.IyziCoLoginType
import com.android.iyzicosdk.util.enums.IyziCoSDKType
import com.android.iyzicosdk.util.enums.IyziCoLoginChannelType
import com.android.iyzicosdk.util.enums.IyziCoSupportScreenType
import com.android.iyzicosdk.util.extensions.*
import kotlinx.android.synthetic.main.iyzico_fragment_login.*
import kotlinx.android.synthetic.main.iyzico_fragment_login.view.*

@Keep
internal class IyziCoLoginFragment : IyziCoBaseFragment() {

    override val layoutRes: Int = R.layout.iyzico_fragment_login

    private var screenType = IyziCoLoginType.NORMAL
    var memberExist = false
    var firstTime = true
    var controller = IyziCoLoginFragmentController.newInstance(this)

    override fun initUI(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        super.initUI(inflater, container, savedInstanceState)
        iyziCoLoginScreenType =
            (arguments?.getSerializable(BundleConstans.FROM) as IyziCoLoginScreenType?)
                ?: IyziCoLoginScreenType.NORMAL

        if (iyziCoLoginScreenType == IyziCoLoginScreenType.CHANGE_MAIL || iyziCoLoginScreenType == IyziCoLoginScreenType.TRY_AGAIN) {
            root.iyzico_fragment_login_email_editText.focus()
        }
        controller.init()

        if (iyziCoLoginScreenType == IyziCoLoginScreenType.CHANGE_PHONE) {
            root.iyzico_fragment_login_email_editText.disableBorder(IyziCoResourcesConstans.IyziCoEmail)
        }
    }

    override fun initUICashOut() {
        super.initUICashOut()
        setFirstCondition()
        root.iyzico_fragment_login_email_editText.clearFocus()
        root.iyzico_fragment_login_phone_editText.clearFocus()
        root.iyzico_fragment_login_phone_editText.gone()
    }

    override fun initUISettlement() {
        super.initUISettlement()
        setFirstCondition()
        root.iyzico_fragment_login_phone_editText.focus()
    }

    override fun reCreateCashOut() {
        super.reCreateCashOut()
        showTopItem()
    }

    override fun initUIPayWithIyziCo() {
        super.initUIPayWithIyziCo()
        startPayWithIyziCoTimer()
        showStickLayout()
        showTopItem()
        if (iyziCoLoginScreenType == IyziCoLoginScreenType.TRY_AGAIN) {
            setTimerAgain()
        }
        root.iyzico_fragment_login_phone_editText.gone()
    }

    override fun initUITopup() {
        super.initUITopup()
        root.iyzico_fragment_login_phone_editText.gone()
    }

    override fun initUIRefund() {
        super.initUIRefund()
        setFirstCondition()
        root.iyzico_fragment_login_phone_editText.focus()
    }


    override fun reCreate(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        super.reCreate(inflater, container, savedInstanceState)
        if (iyziCoLoginScreenType == IyziCoLoginScreenType.CHANGE_PHONE) {
            showTopItem()
        }
        root.iyzico_fragment_login_email_editText.focus()

    }


    override fun reCreateTopup() {
        super.reCreateTopup()
        showTopItem()
        //openKeyboard()
    }

    override fun reCreatePayWithIyziCo() {
        super.reCreatePayWithIyziCo()
        showStickLayout()
        showTopItem()

        // openKeyboard()


    }

    override fun reCreateRefund() {
        super.reCreateRefund()
        root.iyzico_fragment_login_phone_editText.gone()
    }

    fun phoneChangeCheck() {
        if (screenType == IyziCoLoginType.PHONE_CHANGE) {
            clearStack()
        }
    }

    private fun setFirstCondition() {
        with(root) {
            iyzico_fragment_login_cashout_information.show()
            iyzico_fragment_login_email_editText.disableBorder(IyziCoResourcesConstans.IyziCoEmail)
            iyzico_fragment_login_phone_editText.editText.setText(IyziCoResourcesConstans.IyziPhoneNumber.editPhoneNumber())
            iyzico_fragment_login_phone_editText.editText.phoneSelect()
            iyzico_fragment_login_phone_editText.fillEditTextDesign()
        }
    }


    fun pwiInit() {
        controller.pwiInitialize(object : UIResponseCallBack<IyziCoPWIResponse>(this) {
            override fun onSuccess(response: IyziCoPWIResponse?) {
                super.onSuccess(response)
                getIyziCoActivity()?.initialRequestId = response?.checkoutToken
                memberExist = response?.memberExist ?: false

                loginControllBusiness()
            }
        })
    }

    fun loginControllBusiness(){
        if(iyziCoLoginScreenType == IyziCoLoginScreenType.NORMAL){
            if (memberExist && firstTime) {
                openMemberLogin()
            } else if (!memberExist && firstTime) {
                openLogin()
            } else if (!firstTime && memberExist) {
                openOtp()
            } else {
                openSecondry()
            }
        }else{

            if(memberExist){
                openOtp()
            }else{
                openSecondry()
            }

        }
    }
    fun openMemberLogin(){
        firstTime = false
        removeTimer()
        navigate(IyziCoMemberLoginFragment.newInstance(), false)
    }

    fun openLogin(){
        firstTime = false
        if (IyziCoConfig.IYZI_CO_SDK_TYPE == IyziCoSDKType.PAY_WITH_IYZI_CO || IyziCoConfig.IYZI_CO_SDK_TYPE == IyziCoSDKType.TOPUP) {
            root.iyzico_fragment_login_email_editText.focus()
        }
    }

    fun openOtp(){
        sendOtp(
            IyziCoResourcesConstans.IyziPhoneNumber.editPhoneNumber()
                .transformPhoneforService()
        )
    }

    fun openSecondry(){
        navigate(IyziCoSecondaryLoginFragment.newInstance(), true)

    }
    fun cashoutInit() {
        controller.cashoutInit(
            IyziCoResourcesConstans.IyziCoEmail,
            IyziCoResourcesConstans.IyziCoWalletPrice, IyziCoCurrentType.TRY.type,
            object : UIResponseCallBack<IyziCoInitResponse>(this) {
                override fun onSuccess(response: IyziCoInitResponse?) {
                    super.onSuccess(response)
                    response?.let {
                        getIyziCoActivity()?.initialRequestId = response.initialRequestId
                        memberExist = response.memberExist
                        //gecici olarak eklendi pwı initialize eklenince silinecek
                        loginControllBusiness()
                    }
                }
            })
    }

    override fun clickListener() {
        super.clickListener()
        root.iyzico_fragment_login_cashout_information_textView.spannableExtension(
            110,
            150,
            R.color.iyzico_secondary_dark_blue
        ) {
            root.iyzico_fragment_login_cashout_information_textView.setOnClickListener {
                navigate(
                    IyziCoSupportFragment.newInstance(IyziCoSupportScreenType.SUPPORT),
                    true
                )
            }
        }
        root.iyzico_fragment_login_term_textView.let {
            it.spannableExtension(
                27,
                49,
                R.color.iyzico_secondary_dark_blue
            ) {
                it.setOnClickListener {
                    navigate(
                        IyziCoSupportFragment.newInstance(IyziCoSupportScreenType.PRIVACY_POLICY),
                        true
                    )
                }
            }
        }

        root.iyzico_fragment_login_email_editText.editTextValueListener {
            mailValidation(it)
        }
        root.iyzico_fragment_login_phone_editText.editTextValueListener {
            phoneValidation(it)
        }

        root.iyzico_login_fragment_contunie_button.clickListener {
            IyziCoResourcesConstans.IyziCoEmail = iyzico_fragment_login_email_editText.text
            if (root.iyzico_login_fragment_contunie_button.buttonEnabled) {
                when (IyziCoConfig.IYZI_CO_SDK_TYPE) {
                    IyziCoSDKType.TOPUP -> {
                        topUpInit(root.iyzico_fragment_login_email_editText.text, "DEPOSIT")
                    }
                    IyziCoSDKType.PAY_WITH_IYZI_CO -> {
                        pwiInit()
                    }
                    else -> {
                        sendOtp(
                            IyziCoResourcesConstans.IyziPhoneNumber.editPhoneNumber()
                                .transformPhoneforService()
                        )
                    }
                }
            }
        }

    }

    fun nestedScroolView() {
        root.iyzicoNestedScrollView.doThis {
            if (!root.iyzico_fragment_login_phone_editText.hasFocus()) {
                hideKeyboard()
                IyziCoResourcesConstans.IyziCoEmail?.let {
                    root.iyzico_fragment_login_email_editText.apply {
                        clearFocus()
                    }
                }
                IyziCoResourcesConstans.IyziPhoneNumber?.let {
                    root.iyzico_fragment_login_phone_editText.apply {
                        clearFocus()
                    }
                }
            }
        }
    }

    fun emailandPhoneCheck() {
        IyziCoResourcesConstans.IyziCoEmail?.let {
            root.iyzico_fragment_login_email_editText.apply {
                editText.setText(it)
                fillEditTextDesign()
                editText.setSelection(it.length)
                //clearBorder()
            }
            mailValidation(it)
        }
        IyziCoResourcesConstans.IyziPhoneNumber?.let {
            root.iyzico_fragment_login_phone_editText.apply {
                editText.setText(it.editPhoneNumber())
                fillEditTextDesign()
                editText.phoneSelect()
                clearBorder()
            }
        }
    }

    private fun mailValidation(mail: String) {
        if (mail.isEmail()) {
            root.iyzico_login_fragment_contunie_button.buttonEnabled()
        } else {
            root.iyzico_login_fragment_contunie_button.buttonDisable()
        }
    }

    fun phoneValidation(phone: String) {
        if (!phone.isNotPhoneNumber()) {
            root.iyzico_login_fragment_contunie_button.buttonEnabled()
        } else {
            root.iyzico_login_fragment_contunie_button.buttonDisable()
        }
    }

    private fun sendOtp(phoneNumber: String) {
        if (memberExist) {
            loginInitialize(phoneNumber)
        } else {
            when (IyziCoConfig.IYZI_CO_SDK_TYPE) {
                IyziCoSDKType.TOPUP -> {
                    navigate(IyziCoSecondaryLoginFragment.newInstance(), true)
                }
                IyziCoSDKType.PAY_WITH_IYZI_CO -> {
                    navigate(IyziCoSecondaryLoginFragment.newInstance(), true)

                }
                IyziCoSDKType.CASH_OUT -> {
                    navigate(IyziCoSecondaryLoginFragment.newInstance(), false)
                }
                else -> {
                    registerInitialize(phoneNumber)
                }
            }

        }
    }

    fun topUpInit(email: String, type: String) {
        controller.initTransactionForTopUp(
            email,
            type,
            object : UIResponseCallBack<IyziCoInitResponse>(this) {
                override fun onSuccess(response: IyziCoInitResponse?) {
                    super.onSuccess(response)
                    response?.let {
                        memberExist = it.memberExist
                        getIyziCoActivity()?.initialRequestId = it.initialRequestId
                        loginControllBusiness()

                    }
                }
            })
    }

    private fun loginInitialize(phoneNumber: String) {
        controller.loginUser(IyziCoConfig.CLIENT_IP,
            root.iyzico_fragment_login_email_editText.text,
            IyziCoLoginChannelType.THIRD_PARTY_APP.type,
            phoneNumber,
            object : UIResponseCallBack<IyziCoLoginResponse>(this) {})
    }

    private fun registerInitialize(phoneNumber: String) {
        controller.registerUser(
            IyziCoResourcesConstans.IyziCoUserName,
            IyziCoResourcesConstans.IyziCoUserSurName,
            root.iyzico_fragment_login_email_editText.text,
            phoneNumber,
            IyziCoLoginChannelType.THIRD_PARTY_APP.type,
            object : UIResponseCallBack<IyziCoLoginResponse>(this) {})
    }

    fun goOtp(
        referanceCode: String,
        memberId: String,
        phoneNumber: String,
        gsmVerified: Boolean
    ) {
        navigate(
            IyziCoSMSFragment.newInstance(
                referanceCode,
                memberId,
                phoneNumber = phoneNumber.convertServiceMaskedPhoneNumber(),
                gsmVerified = gsmVerified
            ), true
        )
    }

    fun invalidPhone() {
        root.iyzico_fragment_login_phone_editText.errorBorder(getString(R.string.iyzico_common_invalid))
        showSDKError(getString(R.string.iyzico_common_phone_error))
    }

    fun getPhone(): String {
        return root.iyzico_fragment_login_phone_editText.text
    }

    companion object {
        var iyziCoLoginScreenType = IyziCoLoginScreenType.NORMAL
        fun newInstance(loginType: IyziCoLoginScreenType): IyziCoLoginFragment {
            val fragment = IyziCoLoginFragment()
            val bundle = Bundle()
            bundle.putSerializable(BundleConstans.FROM, loginType)
            fragment.arguments = bundle
            return fragment
        }
    }
}