package com.android.iyzicosdk.data.model.request


import com.google.gson.annotations.SerializedName

internal data class IyziCoGsmUpdateRequest(
    @SerializedName("userUuid")
    var userUuid: String?,
    @SerializedName("referenceCode")
    var referenceCode: String?,
    @SerializedName("gsmNumber")
    var gsmNumber: String?,
    @SerializedName("channelType")
    var channelType: String?
)