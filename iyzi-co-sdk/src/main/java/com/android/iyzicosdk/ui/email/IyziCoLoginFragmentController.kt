package com.android.iyzicosdk.ui.email

import com.android.iyzicosdk.callback.IyziCoServiceCallback
import com.android.iyzicosdk.data.UIResponseCallBack
import com.android.iyzicosdk.data.model.request.*
import com.android.iyzicosdk.data.model.request.IyziCoCashoutInitRequest
import com.android.iyzicosdk.data.model.request.IyziCoLoginRequest
import com.android.iyzicosdk.data.model.request.IyziCoRegisterRequest
import com.android.iyzicosdk.data.model.response.IyziCoPWIResponse
import com.android.iyzicosdk.data.model.response.IyziCoInitResponse
import com.android.iyzicosdk.data.model.response.IyziCoLoginResponse
import com.android.iyzicosdk.data.repository.IyziCoRepository
import com.android.iyzicosdk.util.config.IyziCoConfig
import com.android.iyzicosdk.util.constants.IyziCoResourcesConstans
import com.android.iyzicosdk.util.enums.IyziCoLoginScreenType
import com.android.iyzicosdk.util.enums.IyziCoSDKType
import com.android.iyzicosdk.util.enums.IyziCoTransactionType
import com.android.iyzicosdk.util.extensions.isNotPhoneNumber

internal class IyziCoLoginFragmentController constructor(private var baseFragment: IyziCoLoginFragment) {

    companion object {
        fun newInstance(baseFragment: IyziCoLoginFragment) =
            IyziCoLoginFragmentController(baseFragment)
    }

    private var iyziCoRepository = IyziCoRepository.getIyziCoRepository()
    private var merchantSecretKey = ""
    private var merchantApiKey = ""

    fun init() {
        baseFragment.phoneChangeCheck()
        baseFragment.emailandPhoneCheck()
        baseFragment.showCloseButton()
        initializeAll()
        baseFragment.nestedScroolView()
    }

    fun initializeAll() {
        when (IyziCoLoginFragment.iyziCoLoginScreenType) {
            IyziCoLoginScreenType.CHANGE_MAIL -> {
                baseFragment.memberExist = true
            }
            IyziCoLoginScreenType.CHANGE_PHONE -> {
                baseFragment.memberExist = true
            }
            IyziCoLoginScreenType.NORMAL -> {
                when (IyziCoConfig.IYZI_CO_SDK_TYPE) {
                    IyziCoSDKType.CASH_OUT -> {
                        baseFragment.cashoutInit()
                    }
                    IyziCoSDKType.SETTLEMENT -> {
                        baseFragment.cashoutInit()
                    }
                    IyziCoSDKType.REFUND -> {
                        baseFragment.cashoutInit()
                    }
                    IyziCoSDKType.TOPUP -> {
                        baseFragment.topUpInit(
                            IyziCoResourcesConstans.IyziCoEmail,
                            IyziCoTransactionType.DEPOSIT.type
                        )
                    }
                    IyziCoSDKType.PAY_WITH_IYZI_CO -> {
                        baseFragment.pwiInit()
                    }
                }
            }
        }
    }

    fun initTransactionForTopUp(
        email: String, type: String, uiCallback: UIResponseCallBack<IyziCoInitResponse>
    ) {
        iyziCoRepository.initTransaction(IyziCoInitializeTransactionRequest(email, type),
            object : IyziCoServiceCallback<IyziCoInitResponse> {
                override fun onSuccess(data: IyziCoInitResponse?) {
                    data?.let {
                        uiCallback.onSuccess(it)
                    }
                }

                override fun onError(code: Int, message: String) {
                    uiCallback.onError(code, message)
                }
            })
    }

    fun loginUser(
        clientIp: String,
        email: String,
        loginChannel: String,
        phoneNumber: String,
        uiCallback: UIResponseCallBack<IyziCoLoginResponse>
    ) {
        clearMerchantKeys()
        if (phoneNumber.isNotPhoneNumber()) {
            baseFragment.invalidPhone()
        } else {
            baseFragment.showLoadingAnimation()
            iyziCoRepository.login(
                IyziCoLoginRequest(clientIp, email, loginChannel),
                object : IyziCoServiceCallback<IyziCoLoginResponse> {
                    override fun onSuccess(data: IyziCoLoginResponse?) {
                        setMerchantKeys()
                        data?.let {

                            it.gsmNumber?.let {
                                IyziCoResourcesConstans.IyziPhoneNumber = it.substring(3,it.length)
                            }
                            if (it.gsmVerified) {
                                baseFragment.goOtp(
                                    it.referenceCode,
                                    it.memberUserId,
                                    it.maskedGsmNumber,
                                    it.gsmVerified
                                )
                            } else {
                                when (IyziCoConfig.IYZI_CO_SDK_TYPE) {
                                    IyziCoSDKType.SETTLEMENT -> {
                                        baseFragment.goOtp(
                                            it.referenceCode,
                                            it.memberUserId,
                                            it.maskedGsmNumber,
                                            it.gsmVerified
                                        )
                                    }
                                    IyziCoSDKType.REFUND -> {
                                        baseFragment.goOtp(
                                            it.referenceCode,
                                            it.memberUserId,
                                            it.maskedGsmNumber,
                                            it.gsmVerified
                                        )
                                    }
                                    else -> {
                                        baseFragment.goOtp(
                                            it.referenceCode,
                                            it.memberUserId,
                                            it.maskedGsmNumber,
                                            it.gsmVerified
                                        )

                                    }
                                }
                            }
                            uiCallback.onSuccess(it)
                        }
                    }

                    override fun onError(code: Int, message: String) {
                        uiCallback.onError(code, message)
                        setMerchantKeys()
                    }
                })
        }
    }

    fun gsmUpdate(
        userUuid: String,
        referenceCode: String,
        gsmNumber: String,
        channelType: String,
        uiCallback: UIResponseCallBack<IyziCoLoginResponse>
    ) {
        baseFragment.showLoadingAnimation()
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
                        setMerchantKeys()
                        uiCallback.onSuccess(it)
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
        uiCallback: UIResponseCallBack<IyziCoLoginResponse>
    ) {
        if (phoneNumber.isNotPhoneNumber()) {
            baseFragment.invalidPhone()
        } else {
            baseFragment.showLoadingAnimation()
            clearMerchantKeys()
            iyziCoRepository.register(
                IyziCoRegisterRequest(
                    name,
                    surname,
                    email,
                    phoneNumber,
                    registerChannel
                ), object : IyziCoServiceCallback<IyziCoLoginResponse> {
                    override fun onSuccess(data: IyziCoLoginResponse?) {
                        setMerchantKeys()
                        data?.let {
                            uiCallback.onSuccess(data)
                            baseFragment.goOtp(
                                it.referenceCode,
                                it.memberUserId,
                                it.maskedGsmNumber,
                                it.gsmVerified
                            )
                        }
                    }

                    override fun onError(code: Int, message: String) {
                        uiCallback.onError(code, message)
                        setMerchantKeys()

                    }
                })
        }
    }

    fun cashoutInit(
        email: String,
        amount: String,
        currentType: String,
        uiCallback: UIResponseCallBack<IyziCoInitResponse>
    ) {
        baseFragment.showLoadingAnimation()
        iyziCoRepository.cashoutInit(IyziCoCashoutInitRequest(amount, currentType, email),
            object : IyziCoServiceCallback<IyziCoInitResponse> {
                override fun onSuccess(data: IyziCoInitResponse?) {
                    data?.let {
                        uiCallback.onSuccess(it)
                    }
                }

                override fun onError(code: Int, message: String) {
                    uiCallback.onError(code, message)
                }

            })
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
            MobileDeviceInfoDto(
                android.os.Build.DEVICE,
                android.os.Build.MODEL,
                System.getProperty("os.version"),
                android.os.Build.VERSION.SDK
            ),
            IyziCoResourcesConstans.IYZICO_PAID_PRICE.toString(),
            IyziCoResourcesConstans.IYZICO_PAYMENT_GROUP,
            IyziCoResourcesConstans.IYZICO_PAYMENT_SOURCE,
            IyziCoResourcesConstans.IyziCoProductPrice,
            IyziCoShippingAddress(
                IyziCoResourcesConstans.IYZICO_SHIPPING_ADDRESS,
                IyziCoResourcesConstans.IYZICO_SHIPPING_CITY,
                IyziCoResourcesConstans.IYZICO_SHIPPING_CONTACT_NAME,
                IyziCoResourcesConstans.IYZICO_SHIPPING_COUNTRY
            ),
            "sekban-test"
        )
        baseFragment.showLoadingAnimation()
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


}