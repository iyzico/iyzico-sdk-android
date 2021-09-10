package com.android.iyzicosdk.data.model.request


import com.google.gson.annotations.SerializedName

internal data class IyziCoLoginSmsVerification(
    @SerializedName("userUuid")
    val userUuid: String,
    @SerializedName("referenceCode")
    val referenceCode: String,
    @SerializedName("verificationCode")
    val verificationCode: String
)