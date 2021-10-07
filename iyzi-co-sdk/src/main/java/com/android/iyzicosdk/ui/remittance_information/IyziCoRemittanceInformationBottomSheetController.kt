package com.android.iyzicosdk.ui.remittance_information

import com.android.iyzicosdk.callback.IyziCoServiceCallback
import com.android.iyzicosdk.data.UIResponseCallBack
import com.android.iyzicosdk.data.model.request.BankTransferPaymenNotifyRequest
import com.android.iyzicosdk.data.model.response.IyziCoPWIResponse
import com.android.iyzicosdk.data.model.response.IyziCoRetrieverMemberBalanceResponse
import com.android.iyzicosdk.data.repository.IyziCoRepository
import com.android.iyzicosdk.ui.info.IyziCoInfoFragment
import com.android.iyzicosdk.ui.info.IyziCoInfoFragmentController
import com.android.iyzicosdk.util.constants.IyziCoResourcesConstans
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

internal class IyziCoRemittanceInformationBottomSheetController constructor(private var baseFragment: IyziCoRemittanceInformationBottomSheet) {
    companion object {
        fun newInstance(baseFragmentInfoFragment: IyziCoRemittanceInformationBottomSheet) =
            IyziCoRemittanceInformationBottomSheetController(baseFragmentInfoFragment)
    }

    private var iyziCoRepository = IyziCoRepository.getIyziCoRepository()

    fun pwBankTransferNotifty(id: String) {
        IyziCoResourcesConstans.IyziCOxTokenUse = true
        iyziCoRepository.pwiBankTransferInitializeNotify(BankTransferPaymenNotifyRequest(id),
            object :
                IyziCoServiceCallback<IyziCoPWIResponse> {
                override fun onSuccess(data: IyziCoPWIResponse?) {
                    baseFragment.openInformationPage()
                }

                override fun onError(code: Int, message: String) {
                    error(message)
                }

            })
    }
}