package com.android.iyzicosdk.data.model.response


import com.google.gson.annotations.SerializedName

internal data class IyziCoCheckoutDetail(
    @SerializedName("bankTransferAccounts")
    val bankTransferAccounts: List<IyziCoBankItem>,
    @SerializedName("bankTransferEnabled")
    val bankTransferEnabled: Boolean,
    @SerializedName("bankTransferRedirectUrl")
    val bankTransferRedirectUrl: String,
    @SerializedName("baseUrl")
    val baseUrl: String,
    @SerializedName("bkmEnabled")
    val bkmEnabled: Boolean,
    @SerializedName("buyerName")
    val buyerName: String,
    @SerializedName("buyerProtectionEnabled")
    val buyerProtectionEnabled: Boolean,
    @SerializedName("buyerSurname")
    val buyerSurname: String,
    @SerializedName("cancelUrl")
    val cancelUrl: String,
    @SerializedName("creditCardEnabled")
    val creditCardEnabled: Boolean,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("enabledApmTypes")
    val enabledApmTypes: List<String>,
    @SerializedName("force3Ds")
    val force3Ds: Boolean,
    @SerializedName("fundEnabled")
    val fundEnabled: Boolean,
    @SerializedName("gsmNumber")
    val gsmNumber: String,
    @SerializedName("hide3DS")
    val hide3DS: Boolean,
    @SerializedName("locale")
    val locale: String,
    @SerializedName("merchantGatewayBaseUrl")
    val merchantGatewayBaseUrl: String,
    @SerializedName("merchantInfo")
    val merchantInfo: String,
    @SerializedName("metadata")
    val metadata: Metadata,
    @SerializedName("payWithIyzicoEnabled")
    val payWithIyzicoEnabled: Boolean,
    @SerializedName("payWithIyzicoUsed")
    val payWithIyzicoUsed: Boolean,
    @SerializedName("paymentWithNewCardEnabled")
    val paymentWithNewCardEnabled: Boolean,
    @SerializedName("price")
    val price: Double,
    @SerializedName("registerCardEnabled")
    val registerCardEnabled: Boolean,
    @SerializedName("storeNewCardEnabled")
    val storeNewCardEnabled: Boolean,
    @SerializedName("subscriptionPaymentEnabled")
    val subscriptionPaymentEnabled: Boolean,
    @SerializedName("token")
    val token: String,
    @SerializedName("ucsEnabled")
    val ucsEnabled: Boolean,
    @SerializedName("plusInstallmentResponseList")
    val plusInstallmentResponseList: List<IyziCoPlusInstallmentResponse>? = null
)