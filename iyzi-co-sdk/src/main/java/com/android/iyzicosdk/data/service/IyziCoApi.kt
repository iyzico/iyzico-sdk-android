package com.android.iyzicosdk.data.service


import com.android.iyzicosdk.data.model.request.*
import com.android.iyzicosdk.data.model.request.IyziCoAmountCompleteRequest
import com.android.iyzicosdk.data.model.request.IyziCoLoginCompleteRequest
import com.android.iyzicosdk.data.model.request.IyziCoLoginRequest
import com.android.iyzicosdk.data.model.request.IyziCoLoginResendRequest
import com.android.iyzicosdk.data.model.request.IyziCoRegisterRequest
import com.android.iyzicosdk.data.model.response.*
import com.android.iyzicosdk.data.model.response.IyziCoLoginResponse
import com.android.iyzicosdk.data.model.response.IyziCoLoginResponseComplete
import com.android.iyzicosdk.data.repository.base.IyziCoBaseResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

internal interface IyziCoApi {

    @POST("api/v1/members/third-party/otp-quick-login-initialize")
    fun loginInitialize(@Body loginRequest: IyziCoLoginRequest): Call<IyziCoBaseResponse<IyziCoLoginResponse>>

    @POST("api/v1/members/third-party/login-complete")
    fun loginComplete(@Body loginCompleteRequest: IyziCoLoginCompleteRequest): Call<IyziCoBaseResponse<IyziCoLoginResponseComplete>>

    @POST("api/v1/members/third-party/quick-register")
    fun registerInitialize(@Body registerRequest: IyziCoRegisterRequest): Call<IyziCoBaseResponse<IyziCoLoginResponse>>

    @POST("api/v2/members/login/sms-resend")
    fun resendSms(@Body loginResendRequest: IyziCoLoginResendRequest): Call<IyziCoBaseResponse<IyziCoLoginResponse>>

    @POST("api/v1/funds/complete-cashout-to-balance")
    fun balanceComplete(@Body amountCompleteRequest: IyziCoAmountCompleteRequest): Call<IyziCoBaseResponse<IyziCoAmountResponse>>

    @POST("api/v1/funds/initialize-cashout-to-balance")
    fun cashoutInit(@Body cashoutInitRequest: IyziCoCashoutInitRequest): Call<IyziCoBaseResponse<IyziCoInitResponse>>

    @GET("api/v1/funds/balances")
    fun retrieverMemberBalance(): Call<IyziCoBaseResponse<IyziCoRetrieverMemberBalanceResponse>>

    @GET("api/v2/members/cards")
    fun retriverMemberCards(): Call<IyziCoBaseResponse<IyziCoRetrieveMemberCardsResponse>>

    @GET("api/v2/funds/protected-bank-accounts")
    fun getBanks(): Call<IyziCoBaseResponse<IyziCoBanksResponse>>

    @POST("api/v1/funds/third-party/init-transaction")
    fun initTransaction(@Body iyziCoInitResponse: IyziCoInitializeTransactionRequest): Call<IyziCoBaseResponse<IyziCoInitResponse>>

    @POST("api/v1/funds/deposits/third-party/with-registered-card")
    fun createDepositRegisteredCard(@Body createDepositRegisteredCardRequest: IyziCoCreateDepositRegisteredCardRequest): Call<IyziCoBaseResponse<IyziCoCreateDepositCardResponse>>

    @POST("api/v2/members/login/gsm-update")
    fun gsmUpdate(@Body gsmUpdateRequest: IyziCoGsmUpdateRequest): Call<IyziCoBaseResponse<IyziCoLoginResponse>>

    @POST("api/v1/funds/deposits/third-party/with-new-card")
    fun depositWithNewCard(@Body iyziCoCreateDepositNewCardResponse: IyziCoCreateDepositNewCardRequest): Call<IyziCoBaseResponse<IyziCoCreateDepositCardResponse>>

    @POST("api/v1/pay-with-iyzico/third-party/checkout/retrieve")
    fun pwiRetriveService(@Body iyziCoPWIRetrieveRequest: IyziCoPWIRetriveRequest): Call<IyziCoBaseResponse<IyziCoPWIRetriveResponse>>

    @POST("api/v1/pay-with-iyzico/third-party/checkout/init")
    fun pwiInitializeService(@Body iyziCoInitializePayWithIyziCoRequest: IyziCoPWIİnitializeRequest): Call<IyziCoBaseResponse<IyziCoPWIResponse>>

    @POST("api/v1/pay-with-iyzico/third-party/auth")
    fun pwiPayWithBalance(@Body iyziCoPwiPayWithBalanceRequest: IyziCoPayWithBalanceRequest): Call<IyziCoPayWithBalanceResponse>

    @POST("api/v1/pay-with-iyzico/third-party/auth/mix-payment")
    fun pwiMixPayment(@Body iyziCopwiMixPaymentRequest: IyziCoPwiMixPaymentRequest): Call<IyziCoPWIResponse>

    @POST("api/v1/pay-with-iyzico/third-party/instalment")
    fun pwiRetrieveInstallments(@Body retrieveInstalmentRequest: IyziCoRetriveInstalmentRequest): Call<IyziCoPwiRetrieveInstalmentsResponse>

    @POST("api/v1/pay-with-iyzico/third-party/auth/initialize3ds/ecom")
    fun pwiPayWith3D(@Body paywithWithCardRequest: IyziCopayWithCardRequest): Call<IyziCoPWIResponse>

    @POST("api/v1/pay-with-iyzico/third-party/auth/ecom")
    fun pwiPayWithCard(@Body paywithWithCardRequest: IyziCopayWithCardRequest): Call<IyziCoPWIResponse>

    @POST("api/v1/pay-with-iyzico/third-party/bank-transfer/initialize")
    fun pwiBankTransferInitialize(@Body bankTransferInitiaalize: IyziCoBankTransferInitiaalize):Call<IyziCoPWIResponse>

    @POST("api/v1/pay-with-iyzico/third-party/bank-transfer/notify")
    fun pwiBankTransferInitializeNotify(@Body bankTransferPaymenNotifyRequest: BankTransferPaymenNotifyRequest):Call<IyziCoPWIResponse>
}



