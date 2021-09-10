package com.android.iyzicosdk.data.model.request


import com.google.gson.annotations.SerializedName

internal data class IyziCoCreateDepositRegisteredCardRequest(
    @SerializedName("amount")
    var amount: String?,
    @SerializedName("cardToken")
    var cardToken: String?,
    @SerializedName("channelType")
    var channelType: String?,
    @SerializedName("clientIp")
    var clientIp: String?,
    @SerializedName("currencyCode")
    var currencyCode: String?,
    @SerializedName("initialRequestId")
    var initialRequestId: String?
)