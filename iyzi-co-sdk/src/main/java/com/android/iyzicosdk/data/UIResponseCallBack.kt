package com.android.iyzicosdk.data

import com.android.iyzicosdk.callback.IyziCoServiceCallback
import com.android.iyzicosdk.core.IyziCoBaseFragment


internal abstract class UIResponseCallBack<R> constructor(
    private val mvpView: IyziCoBaseFragment?
) : IyziCoServiceCallback<R> {

    override fun onSuccess(response: R?) {
        mvpView?.hideLoadingAnimation()
    }

    override fun onError(errorCode: Int, errorMessage: String) {
        mvpView?.hideLoadingAnimation()
        mvpView?.createErrorPopup(errorMessage)

    }
}