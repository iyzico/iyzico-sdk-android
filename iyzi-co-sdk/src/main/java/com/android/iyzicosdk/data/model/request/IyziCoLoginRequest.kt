package com.android.iyzicosdk.data.model.request


import com.google.gson.annotations.SerializedName

internal data class IyziCoLoginRequest(
    @SerializedName("clientIp")
    var clientIp: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("loginChannel")
    var loginChannel: String
)