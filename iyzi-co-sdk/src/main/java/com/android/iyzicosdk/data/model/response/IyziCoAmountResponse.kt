package com.android.iyzicosdk.data.model.response


import com.google.gson.annotations.SerializedName

internal data class IyziCoAmountResponse(
    @SerializedName("amount")
    val amount: String,
    @SerializedName("balanceAmount")
    val balanceAmount: String,
    @SerializedName("depositStatus")
    val depositStatus: String
)