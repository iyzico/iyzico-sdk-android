package com.android.iyzicosdk.data.model.request


import com.google.gson.annotations.SerializedName

internal data class IyziCoPwiMixPaymentRequest(
    @SerializedName("memberBalanceAmount")
    val memberBalanceAmount: Double,
    @SerializedName("paymentCard")
    val iyziCoPaymentCard: IyziCoPaymentCard,
    @SerializedName("paymentChannel")
    val paymentChannel: String,
    @SerializedName("memberToken")
    val memberToken: String?
)