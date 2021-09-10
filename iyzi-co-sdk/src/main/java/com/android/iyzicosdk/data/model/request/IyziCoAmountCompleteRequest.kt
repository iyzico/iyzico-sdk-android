package com.android.iyzicosdk.data.model.request


import com.google.gson.annotations.SerializedName

internal data class IyziCoAmountCompleteRequest(
    @SerializedName("amount")
    val amount: String,
    @SerializedName("currencyType")
    val currencyType: String,
    @SerializedName("initialRequestId")
    val initialRequestId: String
)