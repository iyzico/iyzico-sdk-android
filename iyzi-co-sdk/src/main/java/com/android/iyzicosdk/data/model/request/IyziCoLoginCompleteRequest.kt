package com.android.iyzicosdk.data.model.request


import com.google.gson.annotations.SerializedName

internal data class IyziCoLoginCompleteRequest(
    @SerializedName("clientIp")
    val clientIp: String,
    @SerializedName("loginChannel")
    val loginChannel: String,
    @SerializedName("loginSmsVerification")
    val loginSmsVerification: IyziCoLoginSmsVerification
)