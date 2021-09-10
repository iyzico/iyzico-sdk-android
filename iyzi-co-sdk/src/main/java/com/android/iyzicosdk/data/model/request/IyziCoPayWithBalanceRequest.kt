package com.android.iyzicosdk.data.model.request


import com.google.gson.annotations.SerializedName

internal data class IyziCoPayWithBalanceRequest(
    @SerializedName("paymentChannel")
    val paymentChannel: String
)