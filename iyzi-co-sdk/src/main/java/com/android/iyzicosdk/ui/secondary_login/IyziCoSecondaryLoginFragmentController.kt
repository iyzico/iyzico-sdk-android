package com.android.iyzicosdk.ui.secondary_login

import com.android.iyzicosdk.callback.IyziCoServiceCallback
import com.android.iyzicosdk.data.UIResponseCallBack
import com.android.iyzicosdk.data.model.request.*
import com.android.iyzicosdk.data.model.response.IyziCoLoginResponse
import com.android.iyzicosdk.data.model.response.IyziCoPWIResponse
import com.android.iyzicosdk.data.repository.IyziCoRepository
import com.android.iyzicosdk.ui.sms.IyziCoSMSFragment
import com.android.iyzicosdk.util.config.IyziCoConfig
import com.android.iyzicosdk.util.constants.IyziCoResourcesConstans
import com.android.iyzicosdk.util.enums.IyziCoLoginChannelType
import com.android.iyzicosdk.util.enums.IyziCoSDKType

internal class IyziCoSecondaryLoginFragmentController constructor(private var baseFragmentIyziCo: IyziCoSecondaryLoginFragment) {
    companion object {
        fun newInstance(baseFragmentIyziCo: IyziCoSecondaryLoginFragment) =
            IyziCoSecondaryLoginFragmentController(baseFragmentIyziCo)
    }


    private var iyziCoRepository = IyziCoRepository.getIyziCoRepository()
    private var merchantSecretKey = ""
    private var merchantApiKey = ""

    fun login(mail: String, phone: String, uiCallback: UIResponseCallBack<IyziCoLoginResponse>) {
        clearMerchantKeys()
        iyziCoRepository.login(
            IyziCoLoginRequest(
                IyziCoConfig.CLIENT_IP,
                mail,
                IyziCoLoginChannelType.THIRD_PARTY_APP.type
            ),
            object : IyziCoServiceCallback<IyziCoLoginResponse> {
                override fun onSuccess(data: IyziCoLoginResponse?) {
                    uiCallback.onSuccess(data)
                    setMerchantKeys()
                    data?.let {
                        it.gsmNumber?.let {
                            IyziCoResourcesConstans.IyziPhoneNumber =  it.substring(3,it.length)
                        }
                        if (data.gsmVerified) {
                            baseFragmentIyziCo.goOtp(
                                it.referenceCode,
                                it.memberUserId,
                                it.maskedGsmNumber,
                                it.gsmVerified
                            )
                        } /*else {
                            gsmUpdate(
                                it.memberUserId,
                                it.referenceCode,
                                phone,
                                IyziCoLoginChannelType.THIRD_PARTY_APP.type
                            )
                        }*/
                    }
                }

                override fun onError(code: Int, message: String) {
                    uiCallback.onError(code, message)
                    setMerchantKeys()
                }
            })

    }

    fun init() {
        baseFragmentIyziCo.setContract()
        baseFragmentIyziCo.showCloseButton()
        baseFragmentIyziCo.setEditTexts()
    }

    fun pwiInitialize(
        uiCallback: UIResponseCallBack<IyziCoPWIResponse>
    ) {
        var iyziCoInitializePayWithIyziCoRequest = IyziCoPWIİnitializeRequest(
            IyziCoResourcesConstans.IYZICO_BASKET_ID,
            IyziCoResourcesConstans.iyzicoBasketItemList,
            IyziCoBillingAddress(
                IyziCoResourcesConstans.IYZICO_BILLING_ADRESS,
                IyziCoResourcesConstans.IYZICO_SHIPPING_CITY,
                IyziCoResourcesConstans.IYZICO_BILLING_CONTACT_NAME,
                IyziCoResourcesConstans.IYZICO_BILLING_COUNTRY
            ),
            IyziCoBuyer(
                IyziCoResourcesConstans.IYZICO_BUYER_CITY,
                IyziCoResourcesConstans.IYZICO_BUYER_COUNTRY,
                IyziCoResourcesConstans.IyziCoEmail,
                IyziCoResourcesConstans.IyziPhoneNumber,
                IyziCoResourcesConstans.IYZICO_ID,
                IyziCoResourcesConstans.IYZICO_BUYER_IDENTITY_NUMBER,
                IyziCoResourcesConstans.IYZICO_BUYER_IP,
                IyziCoResourcesConstans.IyziCoUserName ?: "",
                IyziCoResourcesConstans.IYZICO_REGISTRATION_ADRESS,
                IyziCoResourcesConstans.IyziCoUserSurName ?: ""
            ),
            IyziCoResourcesConstans.IYZICO_CALLBACK_URL,
            IyziCoResourcesConstans.IYZICO_CURRECY,
            IyziCoResourcesConstans.IYZICO_ENABLED_INSTALLMENTS.toList(),
            MobileDeviceInfoDto("Apple", "Apple 10", "IOS-13", "v1.0.1"),
            IyziCoResourcesConstans.IYZICO_PAID_PRICE.toString(),
            IyziCoResourcesConstans.IYZICO_PAYMENT_GROUP,
            IyziCoResourcesConstans.IYZICO_PAYMENT_SOURCE,
            IyziCoResourcesConstans.IyziCoProductPrice,
            IyziCoShippingAddress(
                IyziCoResourcesConstans.IYZICO_SHIPPING_ADDRESS,
                IyziCoResourcesConstans.IYZICO_SHIPPING_CITY,
                IyziCoResourcesConstans.IYZICO_SHIPPING_CONTACT_NAME,
                IyziCoResourcesConstans.IYZICO_SHIPPING_COUNTRY
            )
        )
        baseFragmentIyziCo.showLoadingAnimation()
        iyziCoRepository.pwiInitializeService(iyziCoInitializePayWithIyziCoRequest,
            object : IyziCoServiceCallback<IyziCoPWIResponse> {
                override fun onSuccess(data: IyziCoPWIResponse?) {
                    data?.let {
                        uiCallback.onSuccess(it)
                        IyziCoResourcesConstans.IyziCoXToken = it.checkoutToken ?: ""
                    }
                }

                override fun onError(code: Int, message: String) {
                    uiCallback.onError(code, message)
                }
            })
    }

    private fun clearMerchantKeys() {
        merchantApiKey = IyziCoConfig.MERCHANT_API_KEY
        merchantSecretKey = IyziCoConfig.MERCHANT_SECRET_KEY
        IyziCoConfig.MERCHANT_API_KEY = ""
        IyziCoConfig.MERCHANT_SECRET_KEY = ""
    }

    private fun setMerchantKeys() {
        IyziCoConfig.MERCHANT_API_KEY = merchantApiKey
        IyziCoConfig.MERCHANT_SECRET_KEY = merchantSecretKey
    }

    fun gsmUpdate(
        userUuid: String,
        referenceCode: String,
        gsmNumber: String,
        channelType: String,
        uiCallback: UIResponseCallBack<IyziCoLoginResponse>

    ) {
        baseFragmentIyziCo.showLoadingAnimation()
        clearMerchantKeys()
        iyziCoRepository.gsmUpdate(
            IyziCoGsmUpdateRequest(
                userUuid,
                referenceCode,
                gsmNumber,
                channelType
            ), object : IyziCoServiceCallback<IyziCoLoginResponse> {
                override fun onSuccess(data: IyziCoLoginResponse?) {
                    data.let {
                        uiCallback.onSuccess(data)
                        setMerchantKeys()
                        baseFragmentIyziCo.hideLoadingAnimation()
                        baseFragmentIyziCo.navigate(
                            IyziCoSMSFragment.newInstance(
                                it?.referenceCode ?: "",
                                it?.memberUserId ?: "",
                                it?.maskedGsmNumber ?: "",
                                it?.gsmVerified ?: true
                            )
                        )
                    }
                }

                override fun onError(code: Int, message: String) {
                    setMerchantKeys()
                    uiCallback.onError(code, message)
                }
            })
    }

    fun registerUser(
        name: String?,
        surname: String?,
        email: String,
        phoneNumber: String,
        registerChannel: String,
        outlineAgreementStatus: String? = null,
        pdppPermission: String? = null,
        communicationsPermission: String? = null,
        uiCallback: UIResponseCallBack<IyziCoLoginResponse>
    ) {
        var registeOutlineAgreementStatus: String? = null
        var registecommunicationsPermission: String? = null
        var registecpdppPermission: String? = null
        if (!outlineAgreementStatus.isNullOrEmpty()) {
            registeOutlineAgreementStatus = "ACCEPTED"
        }
        if (!communicationsPermission.isNullOrEmpty()) {
            registecommunicationsPermission = "PERMITTED"
        }
        if (!pdppPermission.isNullOrEmpty()) {
            registecpdppPermission = "PERMITTED_ON_REGISTER"
        }

        baseFragmentIyziCo.showLoadingAnimation()
        clearMerchantKeys()
        iyziCoRepository.register(
            IyziCoRegisterRequest(
                name,
                surname,
                email,
                phoneNumber,
                registerChannel, registeOutlineAgreementStatus,
                registecpdppPermission, registecommunicationsPermission
            ), object : IyziCoServiceCallback<IyziCoLoginResponse> {
                override fun onSuccess(data: IyziCoLoginResponse?) {
                    if (IyziCoConfig.IYZI_CO_SDK_TYPE == IyziCoSDKType.PAY_WITH_IYZI_CO) {
                        setMerchantKeys()
                    }
                    data?.let {
                        uiCallback.onSuccess(data)
                        IyziCoResourcesConstans.IyziPhoneNumber = phoneNumber.substring(3,phoneNumber.length)
                        baseFragmentIyziCo.goOtp(
                            it.referenceCode,
                            it.memberUserId,
                            it.maskedGsmNumber,
                            it.gsmVerified
                        )
                    }
                }

                override fun onError(code: Int, message: String) {
                    uiCallback.onError(code, message)
                    if (IyziCoConfig.IYZI_CO_SDK_TYPE == IyziCoSDKType.PAY_WITH_IYZI_CO) {
                        setMerchantKeys()
                    }

                }
            })
    }
}
