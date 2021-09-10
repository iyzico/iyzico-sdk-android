package com.android.iyzicosdk.ui.secondary_login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Keep
import com.android.iyzicosdk.R
import com.android.iyzicosdk.core.IyziCoBaseFragment
import com.android.iyzicosdk.data.UIResponseCallBack
import com.android.iyzicosdk.data.model.response.IyziCoLoginResponse
import com.android.iyzicosdk.data.model.response.IyziCoPWIResponse
import com.android.iyzicosdk.ui.email.IyziCoLoginFragment
import com.android.iyzicosdk.ui.sms.IyziCoSMSFragment
import com.android.iyzicosdk.util.config.IyziCoConfig
import com.android.iyzicosdk.util.constants.BundleConstans
import com.android.iyzicosdk.util.constants.IyziCoResourcesConstans
import com.android.iyzicosdk.util.enums.IyziCoLoginChannelType
import com.android.iyzicosdk.util.enums.IyziCoLoginScreenType
import com.android.iyzicosdk.util.enums.IyziCoSDKType
import com.android.iyzicosdk.util.enums.IyziCoSupportScreenType
import com.android.iyzicosdk.util.extensions.*
import com.android.iyzicosdk.util.extensions.convertServiceMaskedPhoneNumber
import com.android.iyzicosdk.util.extensions.isNotPhoneNumber
import kotlinx.android.synthetic.main.iyzico_fragment_secondary_login.view.*


@Keep
internal class IyziCoSecondaryLoginFragment : IyziCoBaseFragment() {
    override val layoutRes: Int = R.layout.iyzico_fragment_secondary_login
    var controller = IyziCoSecondaryLoginFragmentController.newInstance(this)
    var userId = ""
    var referanceCode = ""
    var verification: Boolean = false
    var loginType = IyziCoLoginScreenType.NORMAL

    override fun initUI(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        super.initUI(inflater, container, savedInstanceState)
        controller.init()
        loginType = (arguments?.getSerializable(BundleConstans.FROM) as IyziCoLoginScreenType?)
            ?: IyziCoLoginScreenType.NORMAL
        userId = arguments?.getString(BundleConstans.USER_UID) ?: ""
        verification = arguments?.getBoolean(BundleConstans.GSM_VERIFIED) ?: false
        referanceCode = arguments?.getString(BundleConstans.LOGIN_RESPONSE_REFERANCE) ?: ""
        root.iyzico_fragment_secondary_login_phone_editText.editText.setText("")
        phoneValidation(root.iyzico_fragment_secondary_login_phone_editText.text)

        //  if (loginType == IyziCoLoginScreenType.CHANGE_PHONE)


    }

    fun setContract() {
        root.iyzico_communication_permission.clickListener {
            navigate(
                IyziCoSupportFragment.newInstance(IyziCoSupportScreenType.COMMUNICATION),
                true
            )
        }
        root.iyzico_kvkk_permission.clickListener {
            navigate(
                IyziCoSupportFragment.newInstance(IyziCoSupportScreenType.KVKK_PWI),
                true
            )
        }
        root.iyzico_user_aggrement_permission.clickListener {
            navigate(
                IyziCoSupportFragment.newInstance(IyziCoSupportScreenType.USER_AGREMENT_PWI),
                true
            )
        }
    }

    private fun phoneValidation(phone: String) {
        if (!phone.isNotPhoneNumber()) {
            root.iyzico_secondary_login_fragment_contunie_button.buttonEnabled()
        } else {
            root.iyzico_secondary_login_fragment_contunie_button.buttonDisable()
        }
    }

    override fun reCreatePayWithIyziCo() {
        super.reCreatePayWithIyziCo()
        showStickLayout()
        showTopItem()
        //root.iyzico_fragment_secondary_login_phone_editText.focus()
        openKeyboard()
    }

    fun setEditTexts() {
      //  root.iyzico_fragment_secondary_login_phone_editText.focus()
        root.iyzico_fragment_secondary_login_email_editText.apply {
            disableBorder(IyziCoResourcesConstans.IyziCoEmail)
        }

    }

    override fun initUIPayWithIyziCo() {
        root.iyzico_fragment_secondary_login_email_editText.showImageView()
        super.initUIPayWithIyziCo()
    }

    override fun initUITopup() {
        super.initUITopup()
        root.iyzico_fragment_secondary_login_email_editText.showImageView()
    }

    override fun clickListener() {
        super.clickListener()

        root.iyzico_fragment_secondary_login_email_editText.clickListenerImg {
            navigate(IyziCoLoginFragment.newInstance(IyziCoLoginScreenType.CHANGE_MAIL))
           // hideKeyboard()
        }
        root.iyzico_user_aggrement_permission.checkBoxClick { view, b ->
            root.fragment_secondary_login_warning_icon.gone()
            root.iyzico_secondary_login_error_textview.gone()
        }
        //devam et butonuna tıklandığında yapılacak işlemi gösterir.
        root.iyzico_secondary_login_fragment_contunie_button.clickListener {
            if (isEveryThinkValid()) {
                if (loginType == IyziCoLoginScreenType.CHANGE_PHONE) {
                    controller.gsmUpdate(
                        userId,
                        referanceCode,
                        root.iyzico_fragment_secondary_login_phone_editText.text.transformPhoneforService(),
                        IyziCoLoginChannelType.THIRD_PARTY_APP.type,
                        object : UIResponseCallBack<IyziCoLoginResponse>(this) {
                            override fun onSuccess(response: IyziCoLoginResponse?) {
                                super.onSuccess(response)
                                goOtp(
                                    response?.referenceCode ?: "",
                                    response?.memberUserId ?: "",
                                    response?.maskedGsmNumber ?: "",
                                    response?.gsmVerified ?: false
                                )
                            }
                        })
                    //initializeUser()
                } else {
                    controller.registerUser(
                        IyziCoResourcesConstans.IyziCoUserName,
                        IyziCoResourcesConstans.IyziCoUserSurName,
                        root.iyzico_fragment_secondary_login_email_editText.text,
                        root.iyzico_fragment_secondary_login_phone_editText.text.transformPhoneforService(),
                        IyziCoLoginChannelType.THIRD_PARTY_APP.type,
                        root.iyzico_user_aggrement_permission.condition,
                        root.iyzico_kvkk_permission.condition,
                        root.iyzico_communication_permission.condition,
                        object : UIResponseCallBack<IyziCoLoginResponse>(this) {
                            override fun onSuccess(response: IyziCoLoginResponse?) {
                                super.onSuccess(response)
                                IyziCoResourcesConstans.IyziPhoneNumber =
                                    root.iyzico_fragment_secondary_login_phone_editText.text
                            }
                        })
                }
            }
        }
        root.iyzico_fragment_secondary_login_phone_editText.editTextValueListener {
            phoneValidation(it)
        }
    }

    private fun initializeUser() {
        var mvpView = this
        if (IyziCoConfig.IYZI_CO_SDK_TYPE == IyziCoSDKType.PAY_WITH_IYZI_CO) {
            controller.pwiInitialize(object : UIResponseCallBack<IyziCoPWIResponse>(this) {
                override fun onSuccess(response: IyziCoPWIResponse?) {
                    super.onSuccess(response)
                    getIyziCoActivity()?.initialRequestId = response?.checkoutToken
                    controller.login(
                        root.iyzico_fragment_secondary_login_email_editText.text,
                        root.iyzico_fragment_secondary_login_phone_editText.text.transformPhoneforService(),
                        object : UIResponseCallBack<IyziCoLoginResponse>(mvpView) {})
                }
            })
        }
    }

    private fun isEveryThinkValid(): Boolean {
        if (!root.iyzico_secondary_login_fragment_contunie_button.buttonEnabled) {
            return false
        } else if (!root.iyzico_user_aggrement_permission.isClicked) {
            setCheckBoxError()
            return false
        } else return true
    }

    private fun setCheckBoxError() {
        root.fragment_secondary_login_warning_icon.show()
        root.iyzico_secondary_login_error_textview.show()
        root.iyzico_user_aggrement_permission.setError()
        hideKeyboard()
    }

    // register olduktan sonra otp sayfasına gider
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
                phoneNumber.convertServiceMaskedPhoneNumber(),
                gsmVerified = gsmVerified
            ), false
        )
    }

    companion object {
        fun newInstance(
            loginType: IyziCoLoginScreenType = IyziCoLoginScreenType.NORMAL,
            referanceCode: String = "",
            userid: String = "",
            gsmVerified: Boolean = false
        ): IyziCoSecondaryLoginFragment {
            val fragment = IyziCoSecondaryLoginFragment()
            val bundle = Bundle()
            bundle.putSerializable(BundleConstans.FROM, loginType)
            bundle.putString(BundleConstans.USER_UID, userid)
            bundle.putString(BundleConstans.LOGIN_RESPONSE_REFERANCE, referanceCode)
            bundle.putBoolean(BundleConstans.GSM_VERIFIED, gsmVerified)

            fragment.arguments = bundle

            return fragment
        }
    }
}