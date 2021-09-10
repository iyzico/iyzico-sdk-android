package com.android.iyzicosdk.data.model.response


import com.google.gson.annotations.SerializedName

internal data class IyziCoMemberBalance(
    @SerializedName("amount")
    val amount: String,
    @SerializedName("currencyCode")
    val currencyCode: String,
    @SerializedName("provisionAmount")
    val provisionAmount: String
)