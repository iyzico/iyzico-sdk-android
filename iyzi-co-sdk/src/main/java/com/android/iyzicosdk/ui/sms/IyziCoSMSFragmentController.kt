package com.android.iyzicosdk.ui.sms

import com.android.iyzicosdk.callback.IyziCoServiceCallback
import com.android.iyzicosdk.data.UIResponseCallBack
import com.android.iyzicosdk.data.model.request.IyziCoLoginCompleteRequest
import com.android.iyzicosdk.data.model.request.IyziCoLoginResendRequest
import com.android.iyzicosdk.data.model.request.IyziCoLoginSmsVerification
import com.android.iyzicosdk.data.model.response.IyziCoLoginResponse
import com.android.iyzicosdk.data.model.response.IyziCoLoginResponseComplete
import com.android.iyzicosdk.data.repository.IyziCoRepository
import com.android.iyzicosdk.util.config.IyziCoConfig

internal class IyziCoSMSFragmentController constructor(private var baseFragment: IyziCoSMSFragment) {
    private var iyziCoRepository = IyziCoRepository.getIyziCoRepository()
    private var merchantSecretKey = ""
    private var merchantApiKey = ""

    fun init() {
        baseFragment.getSmsArguments()
        baseFragment.initRemainingTime()
        baseFragment.showBackButton()
    }

    fun complateLogin(
        clientIp: String,
        loginChannel: String,
        memberUserId: String,
        referenceCode: String,
        verificationCode: String,
        uiCallback: UIResponseCallBack<IyziCoLoginResponseComplete>
    ) {
        baseFragment.showLoadingAnimation()
        clearMerchantKeys()
        iyziCoRepository.loginComplete(
            IyziCoLoginCompleteRequest(
                clientIp, loginChannel,
                IyziCoLoginSmsVerification(memberUserId, referenceCode, verificationCode)
            ),
            object : IyziCoServiceCallback<IyziCoLoginResponseComplete> {
                override fun onSuccess(response: IyziCoLoginResponseComplete?) {
                    response?.let {
                        setMerchantKeys()
                        IyziCoConfig.IYZI_CO_AUTHORIZATION_KEY = response.accessToken
                        uiCallback.onSuccess(it)
                    }
                }

                override fun onError(code: Int, message: String) {
                    setMerchantKeys()
                    uiCallback.onError(code, message)
                    baseFragment.clearEdittexs()
                }
            })
    }

    fun reSendSms(
        channelType: String,
        memberUserId: String,
        referenceCode: String,
        uiCallback: UIResponseCallBack<IyziCoLoginResponse>
    ) {
        baseFragment.showLoadingAnimation()
        clearMerchantKeys()
        iyziCoRepository.resendSms(
            IyziCoLoginResendRequest(
                channelType,
                referenceCode,
                memberUserId
            ), object : IyziCoServiceCallback<IyziCoLoginResponse> {
                override fun onSuccess(data: IyziCoLoginResponse?) {
                    data?.let {
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

    companion object {
        fun newInstance(baseFragment: IyziCoSMSFragment) =
            IyziCoSMSFragmentController(baseFragment)
    }
}