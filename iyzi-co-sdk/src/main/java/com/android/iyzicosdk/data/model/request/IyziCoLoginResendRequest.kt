package com.android.iyzicosdk.data.model.request


import com.google.gson.annotations.SerializedName

internal data class IyziCoLoginResendRequest(
    @SerializedName("channelType")
    val channelType: String,
    @SerializedName("userUuid")
    val memberUserId: String,
    @SerializedName("referenceCode")
    val referenceCode: String
)