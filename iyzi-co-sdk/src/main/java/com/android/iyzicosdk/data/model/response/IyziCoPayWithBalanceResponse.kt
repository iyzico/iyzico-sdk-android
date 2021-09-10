package com.android.iyzicosdk.data.model.response


import com.google.gson.annotations.SerializedName

internal data class IyziCoPayWithBalanceResponse(
    @SerializedName("callbackUrl")
    val callbackUrl: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("systemTime")
    val systemTime: Long,
    @SerializedName("token")
    val token: String
)