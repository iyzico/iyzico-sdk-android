package com.android.iyzicosdk.ui.amount_transfer

import IyziCoAmountTransferFragment
import com.android.iyzicosdk.callback.IyziCoServiceCallback
import com.android.iyzicosdk.core.Iyzico
import com.android.iyzicosdk.data.UIResponseCallBack
import com.android.iyzicosdk.data.model.request.IyziCoAmountCompleteRequest
import com.android.iyzicosdk.data.model.response.IyziCoAmountResponse
import com.android.iyzicosdk.data.model.response.IyziCoRetrieverMemberBalanceResponse
import com.android.iyzicosdk.data.repository.IyziCoRepository
import com.android.iyzicosdk.ui.info.IyziCoInfoFragment
import com.android.iyzicosdk.util.constants.BundleConstans
import com.android.iyzicosdk.util.enums.IyziCoDepositStatus
import com.android.iyzicosdk.util.enums.IyziCoInfoScreenType
import com.android.iyzicosdk.util.extensions.convertAmount


internal class IyziCoAmountFragmentController constructor(private var baseFragment: IyziCoAmountTransferFragment) {

    companion object {
        fun newInstance(baseFragmentAmountTransferFragment: IyziCoAmountTransferFragment) =
            IyziCoAmountFragmentController(baseFragmentAmountTransferFragment)
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

    fun amountBalanceComplete(
        amount: String,
        currentType: String,
        initialRequestId: String,
        uiCallBack: UIResponseCallBack<IyziCoAmountResponse>
    ) {
        baseFragment.showLoadingAnimation()
        if (amount.equals(BundleConstans.ZERO_MONEY)) {
            baseFragment.errorEmptyAmount()
        } else {
            iyziCoRepository.amountcomplete(
                IyziCoAmountCompleteRequest(
                    amount,
                    currentType,
                    initialRequestId
                ), object : IyziCoServiceCallback<IyziCoAmountResponse> {
                    override fun onSuccess(data: IyziCoAmountResponse?) {
                        data?.let {
                            uiCallBack.onSuccess(it)
                            baseFragment.showIyziCoBalance(it?.balanceAmount?.convertAmount())
                            baseFragment.getIyziCoActivity()
                                ?.setCompleteAmount(
                                    it.depositStatus,
                                    amount
                                )
                            if (it.depositStatus == IyziCoDepositStatus.WAITING_FOR_PROVISION.toString()) {
                                baseFragment.navigate(
                                    IyziCoInfoFragment.newInstance(
                                        IyziCoInfoScreenType.CASHOUT_WAİT,
                                        it?.balanceAmount?.convertAmount()
                                    )
                                )
                            } else {
                                baseFragment.navigate(
                                    IyziCoInfoFragment.newInstance(
                                        IyziCoInfoScreenType.TRANSFER_CONFIRMATION,
                                        it?.balanceAmount?.convertAmount()
                                    )
                                )

                            }
                        }
                    }

                    override fun onError(code: Int, message: String) {
                        uiCallBack.onError(code, message)
                    }
                })
        }
    }

}