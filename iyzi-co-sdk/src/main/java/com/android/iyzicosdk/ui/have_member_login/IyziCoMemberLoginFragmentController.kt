package com.android.iyzicosdk.ui.have_member_login

import com.android.iyzicosdk.callback.IyziCoServiceCallback
import com.android.iyzicosdk.data.UIResponseCallBack
import com.android.iyzicosdk.data.model.request.IyziCoCashoutInitRequest
import com.android.iyzicosdk.data.model.request.IyziCoInitializeTransactionRequest
import com.android.iyzicosdk.data.model.request.IyziCoLoginRequest
import com.android.iyzicosdk.data.model.response.IyziCoInitResponse
import com.android.iyzicosdk.data.model.response.IyziCoLoginResponse
import com.android.iyzicosdk.data.repository.IyziCoRepository
import com.android.iyzicosdk.ui.email.IyziCoLoginFragment
import com.android.iyzicosdk.util.config.IyziCoConfig
import com.android.iyzicosdk.util.extensions.isNotPhoneNumber

internal class IyziCoMemberLoginFragmentController constructor(private var baseFragment: IyziCoMemberLoginFragment) {

    companion object {
        fun newInstance(baseFragment: IyziCoMemberLoginFragment) =
            IyziCoMemberLoginFragmentController(baseFragment)
    }

    private var iyziCoRepository = IyziCoRepository.getIyziCoRepository()
    private var merchantSecretKey = ""
    private var merchantApiKey = ""

    fun loginUser(
        clientIp: String,
        email: String,
        loginChannel: String,
        uiCallback: UIResponseCallBack<IyziCoLoginResponse>
    ) {
        baseFragment.showLoadingAnimation()
        clearMerchantKeys()
        iyziCoRepository.login(
            IyziCoLoginRequest(clientIp, email, loginChannel),
            object : IyziCoServiceCallback<IyziCoLoginResponse> {

                override fun onError(code: Int, message: String) {
                    uiCallback.onError(code, message)
                    setMerchantKeys()
                }
                override fun onSuccess(data: IyziCoLoginResponse?) {
                    data?.let {
                        uiCallback.onSuccess(it)
                        setMerchantKeys()
                    }
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
