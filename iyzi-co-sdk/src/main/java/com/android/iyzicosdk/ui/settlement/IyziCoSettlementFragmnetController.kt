package com.android.iyzicosdk.ui.settlement

import com.android.iyzicosdk.callback.IyziCoServiceCallback
import com.android.iyzicosdk.data.UIResponseCallBack
import com.android.iyzicosdk.data.model.response.IyziCoRetrieverMemberBalanceResponse
import com.android.iyzicosdk.data.repository.IyziCoRepository
import com.android.iyzicosdk.ui.email.IyziCoLoginFragment
import com.android.iyzicosdk.util.constants.BundleConstans

internal class IyziCoSettlementFragmnetController constructor(private var baseFragment: IyziCoSettlementFragment) {

    companion object {
        fun newInstance(baseFragment: IyziCoSettlementFragment) =
            IyziCoSettlementFragmnetController(baseFragment)
    }

    private var iyziCoRepository = IyziCoRepository.getIyziCoRepository()

    fun getRetrieverMemberBalance(uiCallBack: UIResponseCallBack<String>) {
        baseFragment.showLoadingAnimation()
        iyziCoRepository.retrieverMemberBalance(object :
            IyziCoServiceCallback<IyziCoRetrieverMemberBalanceResponse> {
            override fun onSuccess(data: IyziCoRetrieverMemberBalanceResponse?) {
                data?.let {
                    it.amount?.let {
                        uiCallBack.onSuccess(it)
                    }
                }
            }

            override fun onError(code: Int, message: String) {
                baseFragment.hideLoadingAnimation()
                baseFragment.showIyziCoBalance(BundleConstans.ZERO_MONEY)
            }

        })
    }
}

