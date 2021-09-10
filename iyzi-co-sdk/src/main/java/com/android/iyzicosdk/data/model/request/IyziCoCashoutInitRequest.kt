package com.android.iyzicosdk.data.model.request


import com.google.gson.annotations.SerializedName
internal data class IyziCoCashoutInitRequest(
    @SerializedName("amount")
    var amount: String,
    @SerializedName("currencyType")
    var currencyType: String,
    @SerializedName("email")
    var email: String
)