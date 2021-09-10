package com.android.iyzicosdk.data.model.request


import com.google.gson.annotations.SerializedName

internal data class IyziCoCreateDepositNewCardRequest(
    @SerializedName("amount")
    val amount: String,
    @SerializedName("cardHolderName")
    val cardHolderName: String,
    @SerializedName("cardNumber")
    val cardNumber: String,
    @SerializedName("channelType")
    val channelType: String,
    @SerializedName("clientIp")
    val clientIp: String,
    @SerializedName("currencyCode")
    val currencyCode: String,
    @SerializedName("cvc")
    val cvc: String,
    @SerializedName("expireMonth")
    val expireMonth: String,
    @SerializedName("expireYear")
    val expireYear: String,
    @SerializedName("initialRequestId")
    val initialRequestId: String
)