package com.android.iyzicosdk.ui.info

import android.content.Intent
import android.net.Uri
import com.android.iyzicosdk.callback.IyziCoServiceCallback
import com.android.iyzicosdk.data.UIResponseCallBack
import com.android.iyzicosdk.data.model.response.IyziCoRetrieverMemberBalanceResponse
import com.android.iyzicosdk.data.repository.IyziCoRepository
import com.android.iyzicosdk.ui.remittance_information.IyziCoRemittanceInformationBottomSheet
import com.android.iyzicosdk.util.constants.IyziCoUrlConstants
import com.android.iyzicosdk.util.constants.TextMessages
import com.android.iyzicosdk.util.enums.IyziCoBottomSheetType
import com.android.iyzicosdk.util.enums.IyziCoInfoScreenType
import com.android.iyzicosdk.util.enums.ResultCode

internal class IyziCoInfoFragmentController constructor(private var baseFragment: IyziCoInfoFragment) {
    companion object {
        fun newInstance(baseFragmentInfoFragment: IyziCoInfoFragment) =
            IyziCoInfoFragmentController(baseFragmentInfoFragment)
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
                uiCallBack.onError(code, message)
            }

        })
    }

    fun setUi(screenType: IyziCoInfoScreenType) {
        initScreen(screenType)
        baseFragment.hideKeyboard()
        baseFragment.clearStack()
    }

    fun clickSecondary(screenType: IyziCoInfoScreenType,iban:String="",companyDetail:String="",price:String="",explain:String="",companyName:String="",bankTransferId:Int=0) {
        when (screenType) {
            IyziCoInfoScreenType.PAYMENT -> {
                IyziCoRemittanceInformationBottomSheet.show(
                    baseFragment.getIyziCoActivity()?.supportFragmentManager,
                    IyziCoBottomSheetType.TRANSFER_SEE_INFO,iban,companyDetail,explain,companyName,bankTransferId
                )
            }
            IyziCoInfoScreenType.SETTLEMENT_SUCCESS -> {
                baseFragment.callbackError(ResultCode.FAIL, TextMessages.SUCCESS)
                baseFragment.finish()
            }

            IyziCoInfoScreenType.ERROR -> {
                baseFragment.callbackError(ResultCode.FAIL, TextMessages.ERROR)
                baseFragment.finish()
            }
        }
    }

    fun goPlayStore() {
        val intent = Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(IyziCoUrlConstants.playStore));
        baseFragment.startActivity(intent)
    }

    private fun initScreen(screenType: IyziCoInfoScreenType) {
        when (screenType) {
            IyziCoInfoScreenType.TRANSFER -> {
                baseFragment.initForTransfer()
            }
            IyziCoInfoScreenType.PAYMENT -> {
                baseFragment.initForPayment()
            }
            IyziCoInfoScreenType.TRANSFER_CONFIRMATION -> {
                baseFragment.initForTransferConfirmation()
            }
            IyziCoInfoScreenType.ERROR -> {
                baseFragment.initForError()
            }
            IyziCoInfoScreenType.THREE_D -> {
                baseFragment.initForThreeD()
            }
            IyziCoInfoScreenType.TRANSFER_CONFIRMATION_TO_TOPUP -> {
                baseFragment.initforTransferConfirmationTopup()
            }
            IyziCoInfoScreenType.SETTLEMENT_SUCCESS -> {
                baseFragment.initUISettlementSuccess()
            }
            IyziCoInfoScreenType.REFAUND_SUCCESS -> {
                baseFragment.initforRefaundSuccess()
            }
            IyziCoInfoScreenType.CASHOUT_WAİT->{
                baseFragment.initCashoutWait()
            }
            IyziCoInfoScreenType.TOPUP_WAİT->{
                baseFragment.initTopupWait()
            }
        }
    }
}