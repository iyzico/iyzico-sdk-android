package com.android.iyzicosdk.data.model.request


import com.google.gson.annotations.SerializedName

data class IyziCoInquirePaymentCard(
    @SerializedName("cardHolderName")
    val cardHolderName: String,
    @SerializedName("cardNumber")
    val cardNumber: String,
    @SerializedName("cvc")
    val cvc: String,
    @SerializedName("expireMonth")
    val expireMonth: String,
    @SerializedName("expireYear")
    val expireYear: String
)