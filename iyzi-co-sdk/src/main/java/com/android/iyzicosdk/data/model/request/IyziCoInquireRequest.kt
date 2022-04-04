package com.android.iyzicosdk.data.model.request

data class IyziCoInquireRequest(
    val conversationId: String,
    val currency: String,
    val locale: String,
    val paidPrice: Double,
    val paymentCard: IyziCoCardToken,
    val paymentChannel: String
)