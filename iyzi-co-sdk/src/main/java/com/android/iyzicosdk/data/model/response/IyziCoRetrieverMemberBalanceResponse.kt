package com.android.iyzicosdk.data.model.response


import com.google.gson.annotations.SerializedName

internal data class IyziCoRetrieverMemberBalanceResponse(
    @SerializedName("amount")
    internal var amount: String,
    @SerializedName("currencyCode")
    var currencyCode: String,
    @SerializedName("lastUpdatedDate")
    var lastUpdatedDate: String,
    @SerializedName("provisionAmount")
    var provisionAmount: String
)