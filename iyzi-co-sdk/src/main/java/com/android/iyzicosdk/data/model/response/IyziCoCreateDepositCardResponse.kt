package com.android.iyzicosdk.data.model.response


import com.google.gson.annotations.SerializedName

internal data class IyziCoCreateDepositCardResponse(
    @SerializedName("amount")
    var amount: String?,
    @SerializedName("balanceAmount")
    var balanceAmount: String?,
    @SerializedName("currencyCode")
    var currencyCode: String?,
    @SerializedName("depositStatus")
    var depositStatus: String?,
    @SerializedName("depositType")
    var depositType: String?
)