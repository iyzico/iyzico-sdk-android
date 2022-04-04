package com.android.iyzicosdk.data.model.request


import com.google.gson.annotations.SerializedName

data class IyziCoNewCardInquireRequest(
    @SerializedName("conversationId")
    val conversationId: String,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("locale")
    val locale: String,
    @SerializedName("paidPrice")
    val paidPrice: Double,
    @SerializedName("paymentCard")
    val iyziCoInquirePaymentCard: IyziCoInquirePaymentCard,
    @SerializedName("paymentChannel")
    val paymentChannel: String
)