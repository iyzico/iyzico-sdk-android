package com.android.iyzicosdk.data.repository

import com.android.iyzicosdk.callback.IyziCoServiceCallback
import com.android.iyzicosdk.core.IyziCoHttp
import com.android.iyzicosdk.data.model.request.*
import com.android.iyzicosdk.data.model.request.IyziCoAmountCompleteRequest
import com.android.iyzicosdk.data.model.request.IyziCoLoginCompleteRequest
import com.android.iyzicosdk.data.model.request.IyziCoLoginRequest
import com.android.iyzicosdk.data.model.request.IyziCoLoginResendRequest
import com.android.iyzicosdk.data.model.request.IyziCoRegisterRequest
import com.android.iyzicosdk.data.model.response.*
import com.android.iyzicosdk.data.model.response.IyziCoLoginResponse
import com.android.iyzicosdk.data.model.response.IyziCoLoginResponseComplete
import com.android.iyzicosdk.data.repository.base.IyziCoBaseRepository
import com.android.iyzicosdk.data.service.IyziCoApi


internal class IyziCoRepository : IyziCoBaseRepository() {

    private var api: IyziCoApi = IyziCoHttp()
        .getApi()


    fun login(
        loginRequest: IyziCoLoginRequest,
        iyziCoServiceCallback: IyziCoServiceCallback<IyziCoLoginResponse>
    ) {
        execute(api.loginInitialize(loginRequest), iyziCoServiceCallback)
    }

    fun loginComplete(
        loginCompleteRequest: IyziCoLoginCompleteRequest,
        iyziCoServiceCallback: IyziCoServiceCallback<IyziCoLoginResponseComplete>
    ) {
        execute(api.loginComplete(loginCompleteRequest), iyziCoServiceCallback)
    }

    fun resendSms(
        loginResendRequest: IyziCoLoginResendRequest,
        iyziCoServiceCallback: IyziCoServiceCallback<IyziCoLoginResponse>
    ) {
        execute(api.resendSms(loginResendRequest), iyziCoServiceCallback)
    }

    fun initTransaction(
        iyziCoInitializeTransactionRequest: IyziCoInitializeTransactionRequest,
        iyziCoServiceCallback: IyziCoServiceCallback<IyziCoInitResponse>
    ) {
        execute(api.initTransaction(iyziCoInitializeTransactionRequest), iyziCoServiceCallback)
    }

    fun register(
        request: IyziCoRegisterRequest,
        iyziCoServiceCallback: IyziCoServiceCallback<IyziCoLoginResponse>
    ) {
        execute(api.registerInitialize(request), iyziCoServiceCallback)
    }

    fun amountcomplete(
        amountCompleteRequest: IyziCoAmountCompleteRequest,
        iyziCoServiceCallback: IyziCoServiceCallback<IyziCoAmountResponse>
    ) {
        execute(api.balanceComplete(amountCompleteRequest), iyziCoServiceCallback)
    }

    fun cashoutInit(
        request: IyziCoCashoutInitRequest,
        iyziCoServiceCallback: IyziCoServiceCallback<IyziCoInitResponse>
    ) {
        execute(api.cashoutInit(request), iyziCoServiceCallback)
    }

    fun retrieverMemberBalance(iyziCoServiceCallback: IyziCoServiceCallback<IyziCoRetrieverMemberBalanceResponse>) {
        execute(api.retrieverMemberBalance(), iyziCoServiceCallback)
    }

    fun retriverMemberCards(iyziCoServiceCallback: IyziCoServiceCallback<IyziCoRetrieveMemberCardsResponse>) {
        execute(api.retriverMemberCards(), iyziCoServiceCallback)
    }

    fun getBanks(iyziCoServiceCallback: IyziCoServiceCallback<IyziCoBanksResponse>) {
        execute(api.getBanks(), iyziCoServiceCallback)
    }

    fun createDepositRegisteredCard(
        iyziCoCreateDepositRegisteredCardRequest: IyziCoCreateDepositRegisteredCardRequest,
        iyziCoServiceCallback: IyziCoServiceCallback<IyziCoCreateDepositCardResponse>
    ) {
        execute(
            api.createDepositRegisteredCard(iyziCoCreateDepositRegisteredCardRequest),
            iyziCoServiceCallback
        )
    }

    fun gsmUpdate(
        gsmUpdateRequest: IyziCoGsmUpdateRequest,
        iyziCoServiceCallback: IyziCoServiceCallback<IyziCoLoginResponse>
    ) {
        execute(api.gsmUpdate(gsmUpdateRequest), iyziCoServiceCallback)
    }

    fun createDepositNewCard(
        iyziCoCreateDepositNewCardRequest: IyziCoCreateDepositNewCardRequest,
        iyziCoServiceCallback: IyziCoServiceCallback<IyziCoCreateDepositCardResponse>
    ) {
        execute(api.depositWithNewCard(iyziCoCreateDepositNewCardRequest), iyziCoServiceCallback)
    }

    fun pwiRetriveService(
        iyziCoPWIRetriveRequest: IyziCoPWIRetriveRequest,
        iyziCoServiceCallback: IyziCoServiceCallback<IyziCoPWIRetriveResponse>
    ) {
        execute(api.pwiRetriveService(iyziCoPWIRetriveRequest), iyziCoServiceCallback)
    }

    fun pwiInitializeService(
        iyziCoInitializePayWithIyziCoRequest: IyziCoPWIİnitializeRequest,
        callback: IyziCoServiceCallback<IyziCoPWIResponse>
    ) {
        execute(api.pwiInitializeService(iyziCoInitializePayWithIyziCoRequest), callback)

    }

    fun pwiPayWithBalance(
        iyziCoPwiPayWithBalanceRequest: IyziCoPayWithBalanceRequest,
        callback: IyziCoServiceCallback<IyziCoPayWithBalanceResponse>
    ) {
        executeWithoutData(api.pwiPayWithBalance(iyziCoPwiPayWithBalanceRequest), callback)
    }

    fun payWithMixpayment(
        iyCoMixPaymentRequest: IyziCoPwiMixPaymentRequest,
        callback: IyziCoServiceCallback<IyziCoPWIResponse>
    ) {
        executeWithoutData(api.pwiMixPayment(iyCoMixPaymentRequest), callback)
    }

    fun pwiRetrieveInstallments(
        iyziCoRetriveInstalmentRequest: IyziCoRetriveInstalmentRequest,
        callback: IyziCoServiceCallback<IyziCoPwiRetrieveInstalmentsResponse>
    ) {
        executeWithoutData(api.pwiRetrieveInstallments(iyziCoRetriveInstalmentRequest), callback)
    }

    fun payWithThreeD(
        payWithWithThreeDRequest: IyziCopayWithCardRequest,
        callback: IyziCoServiceCallback<IyziCoPWIResponse>
    ) {
        executeWithoutData(api.pwiPayWith3D(payWithWithThreeDRequest), callback)
    }

    fun payWithCard(
        payWithCardRequest: IyziCopayWithCardRequest,
        callback: IyziCoServiceCallback<IyziCoPWIResponse>
    ) {
        executeWithoutData(api.pwiPayWithCard(payWithCardRequest), callback)
    }

    fun pwiBankTransferInitialize(
        iyziCoBankTransferInitialize: IyziCoBankTransferInitiaalize,
        callback: IyziCoServiceCallback<IyziCoPWIResponse>
    ) {
        executeWithoutData(api.pwiBankTransferInitialize(iyziCoBankTransferInitialize), callback)

    }

    fun pwiBankTransferInitializeNotify(
        bankTransferPaymenNotifyRequest: BankTransferPaymenNotifyRequest,
        callback: IyziCoServiceCallback<IyziCoPWIResponse>
    ) {
        executeWithoutData(
            api.pwiBankTransferInitializeNotify(bankTransferPaymenNotifyRequest),
            callback
        )
    }


    fun pwiInquire(
        inquireRequest: IyziCoInquireRequest,
        callback: IyziCoServiceCallback<IyziCoInquireResponse>
    ) {
        executeWithoutData(api.pwiInquire(inquireRequest), callback)
    }

    fun pwiInquireWithNewCard(
        iyziCoNewCardInquireRequest: IyziCoNewCardInquireRequest,
        callback: IyziCoServiceCallback<IyziCoInquireResponse>
    ) {
        executeWithoutData(api.pwiInquireNewCard(iyziCoNewCardInquireRequest), callback)
    }

    companion object {
        fun getIyziCoRepository() = IyziCoRepository()
    }

}